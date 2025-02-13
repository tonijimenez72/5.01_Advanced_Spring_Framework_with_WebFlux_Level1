package cat.itacademy.s05.t01.n01.S05T01N01.controller;

import cat.itacademy.s05.t01.n01.S05T01N01.dto.NewPlayerNameRequest;
import cat.itacademy.s05.t01.n01.S05T01N01.exception.custom.RankingIsEmptyException;
import cat.itacademy.s05.t01.n01.S05T01N01.model.Player;
import cat.itacademy.s05.t01.n01.S05T01N01.service.PlayerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.naming.NameNotFoundException;

@RestController
public class PlayerController {

    private final PlayerService playerService;

    @Autowired
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @PutMapping("/player/{playerId}")
    public Mono<Player> updatePlayerName(@PathVariable Long playerId, @Valid @RequestBody NewPlayerNameRequest request) {
        return playerService.updatePlayerName(playerId, request.getPlayerName());
    }

    @GetMapping("/ranking")
    public Flux<Player> getRanking() {
        return playerService.getRanking()
                .switchIfEmpty(Flux.error(new RankingIsEmptyException("Ranking is empty")));
    }
}
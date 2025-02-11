package cat.itacademy.s05.t01.n01.S05T01N01.controller;

import cat.itacademy.s05.t01.n01.S05T01N01.model.Player;
import cat.itacademy.s05.t01.n01.S05T01N01.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @PutMapping("/player/{playerId}")
    public Mono<Player> updatePlayerName(@PathVariable Long playerId, @RequestBody Player player) {
        return playerService.updatePlayerName(playerId, player.getName());
    }

    @GetMapping("/ranking")
    public Flux<Player> getRanking() {
        return playerService.getRanking();
    }
}
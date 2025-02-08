package cat.itacademy.s05.t01.n01.S05T01N01.controller;

import cat.itacademy.s05.t01.n01.S05T01N01.model.Player;
import cat.itacademy.s05.t01.n01.S05T01N01.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/players")
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @GetMapping
    public Flux<Player> getAllPlayers() {
        return playerService.getAllPlayers();
    }

    @GetMapping("/{id}")
    public Mono<Player> getPlayerById(@PathVariable String id) {
        return playerService.getPlayerById(id);
    }

    @PostMapping
    public Mono<Player> savePlayer(@RequestBody Player player) {
        return playerService.savePlayer(player);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deletePlayerById(@PathVariable String id) {
        return playerService.deletePlayerById(id);
    }
}
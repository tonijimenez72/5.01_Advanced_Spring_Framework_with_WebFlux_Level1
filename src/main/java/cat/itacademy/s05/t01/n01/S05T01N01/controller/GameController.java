package cat.itacademy.s05.t01.n01.S05T01N01.controller;

import cat.itacademy.s05.t01.n01.S05T01N01.model.Game;
import cat.itacademy.s05.t01.n01.S05T01N01.model.Player;
import cat.itacademy.s05.t01.n01.S05T01N01.service.GameService;
import cat.itacademy.s05.t01.n01.S05T01N01.service.GameplayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/game")
public class GameController {

    @Autowired
    private GameService gameService;

    @Autowired
    private GameplayService gameplayService;

    @PostMapping("/new")
    public Mono<Game> createNewGame(@RequestBody Game game) {
        return gameService.saveGame(game);
    }

    @GetMapping
    public Flux<Game> getAllGames() {
        return gameService.getAllGames();
    }

    @GetMapping("/{id}")
    public Mono<Game> getGameById(@PathVariable String id) {
        return gameService.getGameById(id);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteGameById(@PathVariable String id) {
        return gameService.deleteGameById(id);
    }

    @PostMapping("/{id}/addPlayer")
    public Mono<Game> addPlayerToGame(@PathVariable String id, @RequestBody Player player) {
        return gameService.addPlayerToGame(id, player);
    }

    @PostMapping("/{id}/addPlayers")
    public Mono<Game> addPlayersToGame(@PathVariable String id, @RequestBody List<Player> players) {
        return gameService.addPlayersToGame(id, players);
    }

    @PostMapping("/{gameId}/dealCards")
    public Mono<Game> dealCards(@PathVariable String gameId) {
        return gameplayService.dealCards(gameId);
    }

    @GetMapping("/{gameId}/result")
    public Mono<String> getGameResult(@PathVariable String gameId) {
        return gameplayService.getGameResult(gameId);
    }

    @PostMapping("/{gameId}/player/{playerId}/stand")
    public Mono<Player> playerStands(@PathVariable String gameId, @PathVariable String playerId) {
        return gameplayService.playerStands(gameId, playerId);
    }

}
package cat.itacademy.s05.t01.n01.S05T01N01.controller;

import cat.itacademy.s05.t01.n01.S05T01N01.dto.NewGameRequest;
import cat.itacademy.s05.t01.n01.S05T01N01.dto.PlayMoveRequest;
import cat.itacademy.s05.t01.n01.S05T01N01.exception.custom.GameNotFoundException;
import cat.itacademy.s05.t01.n01.S05T01N01.model.Game;
import cat.itacademy.s05.t01.n01.S05T01N01.service.GameService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.naming.NameNotFoundException;

@RestController
@RequestMapping("/game")
public class GameController {

    private final GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/new")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Game> createGame(@Valid @RequestBody NewGameRequest newGameRequest) {
        return gameService.createGame(newGameRequest.getPlayerName());
    }

    @GetMapping("/all")
    public Flux<Game> getAllGames() {
        return gameService.getAllGames();
    }

    @GetMapping("/{id}")
    public Mono<Game> getGame(@PathVariable String id) {
        return gameService.getGame(id);
    }

    @PostMapping("/{id}/play")
    public Mono<Game> playMove(@PathVariable String id, @Valid @RequestBody PlayMoveRequest playMoveRequest) {
        return gameService.playMove(id, playMoveRequest.getMove());
    }

    @DeleteMapping("/{id}/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteGame(@PathVariable String id) {
        return gameService.deleteGame(id);
    }
}
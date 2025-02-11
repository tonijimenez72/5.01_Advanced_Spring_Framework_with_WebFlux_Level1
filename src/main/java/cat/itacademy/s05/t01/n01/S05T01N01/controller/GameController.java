package cat.itacademy.s05.t01.n01.S05T01N01.controller;

import cat.itacademy.s05.t01.n01.S05T01N01.dto.NewGameRequest;
import cat.itacademy.s05.t01.n01.S05T01N01.dto.PlayMoveRequest;
import cat.itacademy.s05.t01.n01.S05T01N01.model.GameState;
import cat.itacademy.s05.t01.n01.S05T01N01.service.GameService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/game")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/new")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<GameState> createGame(@RequestBody NewGameRequest request) {
        return gameService.startGame(request.getPlayerName());
    }

    @GetMapping("/{id}")
    public Mono<GameState> getGame(@PathVariable("id") String gameId) {
        return gameService.getGame(gameId);
    }

    @GetMapping
    public Flux<GameState> getAllGames() {
        return gameService.getAllGames();
    }


    @PostMapping("/{id}/play")
    public Mono<GameState> play(@PathVariable("id") String gameId, @RequestBody PlayMoveRequest playRequest) {
        String move = playRequest.getMove();
        if ("HIT".equalsIgnoreCase(move)) {
            return gameService.hit(gameId);
        } else if ("STAND".equalsIgnoreCase(move)) {
            return gameService.stand(gameId);
        } else {
            return Mono.error(new UnsupportedOperationException("Moviment no suportat: " + move));
        }
    }

    @DeleteMapping("/{id}/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteGame(@PathVariable("id") String gameId) {
        return gameService.deleteGame(gameId);
    }
}
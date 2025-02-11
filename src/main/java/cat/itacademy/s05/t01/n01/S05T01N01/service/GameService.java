package cat.itacademy.s05.t01.n01.S05T01N01.service;

import cat.itacademy.s05.t01.n01.S05T01N01.model.GameState;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GameService {
    Mono<GameState> startGame(String playerName);
    Mono<GameState> hit(String gameId);
    Mono<GameState> stand(String gameId);
    Mono<GameState> getGame(String gameId);
    Flux<GameState> getAllGames();
    Mono<Void> deleteGame(String gameId);
}
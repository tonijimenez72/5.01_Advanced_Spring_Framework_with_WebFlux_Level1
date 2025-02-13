package cat.itacademy.s05.t01.n01.S05T01N01.service;

import cat.itacademy.s05.t01.n01.S05T01N01.enums.PlayerMove;
import cat.itacademy.s05.t01.n01.S05T01N01.model.Game;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GameService {
    Mono<Game> createGame(String playerName);
    Flux<Game> getAllGames();
    Mono<Game> getGame(String gameId);
    Mono<Game> playMove(String gameId, PlayerMove move);
    Mono<Void> deleteGame(String gameId);
}
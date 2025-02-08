package cat.itacademy.s05.t01.n01.S05T01N01.service;

import cat.itacademy.s05.t01.n01.S05T01N01.model.Game;
import cat.itacademy.s05.t01.n01.S05T01N01.model.Player;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface GameService {
    Flux<Game> getAllGames();
    Mono<Game> getGameById(String id);
    Mono<Game> saveGame(Game game);
    Mono<Void> deleteGameById(String id);
    Mono<Game> addPlayerToGame(String gameId, Player player);
    Mono<Game> addPlayersToGame(String gameId, List<Player> players);
    Flux<Player> getPlayersByGameId(String gameId);
}
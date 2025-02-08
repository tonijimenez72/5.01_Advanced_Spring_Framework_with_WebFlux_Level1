package cat.itacademy.s05.t01.n01.S05T01N01.service;

import cat.itacademy.s05.t01.n01.S05T01N01.model.Game;
import cat.itacademy.s05.t01.n01.S05T01N01.model.Player;
import reactor.core.publisher.Mono;

public interface GameplayService {
    Mono<Game> dealCards(String gameId);
    Mono<String> getGameResult(String gameId);
    Mono<Player> playerHits(String gameId, String playerId);
    Mono<Player> playerStands(String gameId, String playerId);
    Mono<Player> croupierPlays(String gameId);
}
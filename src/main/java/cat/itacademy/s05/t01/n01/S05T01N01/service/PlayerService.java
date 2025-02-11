package cat.itacademy.s05.t01.n01.S05T01N01.service;

import cat.itacademy.s05.t01.n01.S05T01N01.model.Player;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PlayerService {
    Mono<Player> createPlayer(Player player);
    Mono<Player> updatePlayerName(Long playerId, String newName);
    Mono<Player> getPlayerById(Long playerId);
    Flux<Player> getRanking();
    Mono<Void> deletePlayer(Long playerId);
}
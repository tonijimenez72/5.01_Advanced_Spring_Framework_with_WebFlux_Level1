package cat.itacademy.s05.t01.n01.S05T01N01.service;

import cat.itacademy.s05.t01.n01.S05T01N01.model.Player;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PlayerService {
    Mono<Player> updatePlayerName(Long playerId, String newName);
    Flux<Player> getRanking();
    Mono<Player> findByName(String name);
    Mono<Player> save(Player player);
}
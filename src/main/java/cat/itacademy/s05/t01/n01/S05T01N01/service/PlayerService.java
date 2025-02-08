package cat.itacademy.s05.t01.n01.S05T01N01.service;

import cat.itacademy.s05.t01.n01.S05T01N01.model.Player;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PlayerService {
    Flux<Player> getAllPlayers();
    Mono<Player> getPlayerById(String id);
    Mono<Player> savePlayer(Player player);
    Mono<Void> deletePlayerById(String id);
}
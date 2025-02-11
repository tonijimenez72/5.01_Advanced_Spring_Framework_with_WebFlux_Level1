package cat.itacademy.s05.t01.n01.S05T01N01.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import  cat.itacademy.s05.t01.n01.S05T01N01.model.Player;
import reactor.core.publisher.Mono;

@Repository
public interface PlayerRepository extends ReactiveCrudRepository<Player, Long> {
    Mono<Player> findByName(String name);
}

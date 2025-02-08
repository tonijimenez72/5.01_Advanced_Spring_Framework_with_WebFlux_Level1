package cat.itacademy.s05.t01.n01.S05T01N01.repository;

import cat.itacademy.s05.t01.n01.S05T01N01.model.Player;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface PlayerRepository extends ReactiveMongoRepository<Player, String> {
    Flux<Player> findByName(String name);
}
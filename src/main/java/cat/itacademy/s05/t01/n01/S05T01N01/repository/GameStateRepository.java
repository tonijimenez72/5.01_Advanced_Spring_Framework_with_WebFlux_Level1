package cat.itacademy.s05.t01.n01.S05T01N01.repository;

import cat.itacademy.s05.t01.n01.S05T01N01.model.GameState;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface GameStateRepository extends ReactiveMongoRepository<GameState, String> {
    Mono<GameState> findByGameId(String gameId);
}
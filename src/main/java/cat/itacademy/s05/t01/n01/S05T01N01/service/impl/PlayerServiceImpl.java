package cat.itacademy.s05.t01.n01.S05T01N01.service.impl;

import cat.itacademy.s05.t01.n01.S05T01N01.exception.custom.PlayerNotFoundException;
import cat.itacademy.s05.t01.n01.S05T01N01.exception.custom.RankingIsEmptyException;
import cat.itacademy.s05.t01.n01.S05T01N01.model.Player;
import cat.itacademy.s05.t01.n01.S05T01N01.repository.PlayerRepository;
import cat.itacademy.s05.t01.n01.S05T01N01.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.naming.NameNotFoundException;

@Service
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;

    @Autowired
    public PlayerServiceImpl(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
    public Mono<Player> updatePlayerName(Long playerId, String newName) {
        return playerRepository.findById(playerId)
                .switchIfEmpty(Mono.error(new PlayerNotFoundException("Player not found with id: " + playerId)))
                .flatMap(player -> {
                    player.setName(newName);
                    return playerRepository.save(player);
                });
    }

    @Override
    public Flux<Player> getRanking() {
        return playerRepository.findAll()
                .switchIfEmpty(Flux.error(new RankingIsEmptyException("Ranking is empty")));
    }

    @Override
    public Mono<Player> findByName(String name) {
        return playerRepository.findByName(name)
                .switchIfEmpty(Mono.defer(() -> {
                    Player newPlayer = new Player(null, name, 0);
                    return playerRepository.save(newPlayer);
                }));
    }


    @Override
    public Mono<Player> save(Player player) {
        return playerRepository.save(player);
    }
}
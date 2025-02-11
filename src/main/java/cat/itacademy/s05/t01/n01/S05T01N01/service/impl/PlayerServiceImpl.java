package cat.itacademy.s05.t01.n01.S05T01N01.service.impl;

import cat.itacademy.s05.t01.n01.S05T01N01.model.Player;
import cat.itacademy.s05.t01.n01.S05T01N01.repository.PlayerRepository;
import cat.itacademy.s05.t01.n01.S05T01N01.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PlayerServiceImpl implements PlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    @Override
    public Mono<Player> createPlayer(Player player) {
        return playerRepository.save(player);
    }

    @Override
    public Mono<Player> updatePlayerName(Long playerId, String newName) {
        return playerRepository.findById(playerId)
                .flatMap(existingPlayer -> {
                    existingPlayer.setName(newName);
                    return playerRepository.save(existingPlayer);
                });
    }

    @Override
    public Mono<Player> getPlayerById(Long playerId) {
        return playerRepository.findById(playerId);
    }

    @Override
    public Flux<Player> getRanking() {
        return playerRepository.findAll();
    }

    @Override
    public Mono<Void> deletePlayer(Long playerId) {
        return playerRepository.deleteById(playerId);
    }
}
package cat.itacademy.s05.t01.n01.S05T01N01.service;

import cat.itacademy.s05.t01.n01.S05T01N01.model.Player;
import cat.itacademy.s05.t01.n01.S05T01N01.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
class PlayerServiceImpl implements PlayerService {
    @Autowired
    private PlayerRepository playerRepository;

    @Override
    public Flux<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

    @Override
    public Mono<Player> getPlayerById(String id) {
        return playerRepository.findById(id);
    }

    @Override
    public Mono<Player> savePlayer(Player player) {
        return playerRepository.save(player);
    }

    @Override
    public Mono<Void> deletePlayerById(String id) {
        return playerRepository.deleteById(id);
    }
}


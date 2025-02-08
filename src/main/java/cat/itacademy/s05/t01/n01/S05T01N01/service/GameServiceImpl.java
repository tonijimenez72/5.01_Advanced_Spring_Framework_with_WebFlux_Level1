package cat.itacademy.s05.t01.n01.S05T01N01.service;

import cat.itacademy.s05.t01.n01.S05T01N01.model.Game;
import cat.itacademy.s05.t01.n01.S05T01N01.model.Player;
import cat.itacademy.s05.t01.n01.S05T01N01.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
class GameServiceImpl implements GameService {
    private static final int MAX_PLAYERS = 7;

    @Autowired
    private GameRepository gameRepository;

    @Override
    public Flux<Game> getAllGames() {
        return gameRepository.findAll();
    }

    @Override
    public Mono<Game> getGameById(String id) {
        return gameRepository.findById(id);
    }

    @Override
    public Mono<Game> saveGame(Game game) {
        Player croupier = new Player("Croupier", true);
        game.getPlayers().add(croupier);
        return gameRepository.save(game);
    }

    @Override
    public Mono<Void> deleteGameById(String id) {
        return gameRepository.deleteById(id);
    }

    @Override
    public Mono<Game> addPlayerToGame(String gameId, Player player) {
        return gameRepository.findById(gameId)
                .flatMap(game -> {
                    if (game.getPlayers().size() >= MAX_PLAYERS) {
                        return Mono.error(new IllegalStateException("Maximum number of players reached"));
                    }
                    game.getPlayers().add(player);
                    return gameRepository.save(game);
                });
    }

    @Override
    public Mono<Game> addPlayersToGame(String gameId, List<Player> players) {
        return gameRepository.findById(gameId)
                .flatMap(game -> {
                    game.getPlayers().addAll(players);
                    return gameRepository.save(game);
                });
    }


    @Override
    public Flux<Player> getPlayersByGameId(String gameId) {
        return gameRepository.findById(gameId)
                .flatMapMany(game -> Flux.fromIterable(game.getPlayers()));
    }
}

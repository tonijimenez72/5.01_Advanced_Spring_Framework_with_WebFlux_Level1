package cat.itacademy.s05.t01.n01.S05T01N01.service.impl;

import cat.itacademy.s05.t01.n01.S05T01N01.model.Game;
import cat.itacademy.s05.t01.n01.S05T01N01.model.Player;
import cat.itacademy.s05.t01.n01.S05T01N01.repository.GameRepository;
import cat.itacademy.s05.t01.n01.S05T01N01.service.GameplayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class GameplayServiceImpl implements GameplayService {

    @Autowired
    private GameRepository gameRepository;

    @Override
    public Mono<Game> dealCards(String gameId) {
        return gameRepository.findById(gameId)
                .flatMap(game -> {
                    List<Player> players = game.getPlayers();
                    players.forEach(player -> {
                        player.addCard(game.getDeck().drawCard());
                        player.addCard(game.getDeck().drawCard());
                    });
                    return gameRepository.save(game);
                });
    }

    @Override
    public Mono<String> getGameResult(String gameId) {
        return gameRepository.findById(gameId)
                .map(game -> {
                    for (Player player : game.getPlayers()) {
                        if (player.getTotal() == 21) {
                            game.setStatus("Completed");
                            gameRepository.save(game).subscribe();
                            return player.isCroupier()
                                    ? "Croupier wins with Blackjack!"
                                    : player.getName() + " wins with Blackjack!";
                        }
                    }

                    return "No initial Blackjack. Game is in progress.";
                });
    }


    @Override
    public Mono<Player> playerHits(String gameId, String playerId) {
        return gameRepository.findById(gameId)
                .flatMap(game -> {
                    Player player = game.getPlayers().stream()
                            .filter(p -> p.getName().equals(playerId))
                            .findFirst()
                            .orElseThrow(() -> new IllegalArgumentException("Player not found"));
                    player.addCard(game.getDeck().drawCard());
                    return gameRepository.save(game).thenReturn(player);
                });
    }

    @Override
    public Mono<Player> playerStands(String gameId, String playerId) {
        return gameRepository.findById(gameId)
                .flatMap(game -> game.getPlayers().stream()
                        .filter(p -> p.getName().equals(playerId))
                        .findFirst()
                        .map(Mono::just)
                        .orElseThrow(() -> new IllegalArgumentException("Player not found")));
    }

    @Override
    public Mono<Player> croupierPlays(String gameId) {
        return gameRepository.findById(gameId)
                .flatMap(game -> {
                    Player croupier = game.getPlayers().stream()
                            .filter(Player::isCroupier)
                            .findFirst()
                            .orElseThrow(() -> new IllegalArgumentException("Croupier not found"));
                    while (croupier.getTotal() < 17) {
                        croupier.addCard(game.getDeck().drawCard());
                    }
                    return gameRepository.save(game).thenReturn(croupier);
                });
    }
}
package cat.itacademy.s05.t01.n01.S05T01N01.service.impl;

import cat.itacademy.s05.t01.n01.S05T01N01.builder.GameBuilder;
import cat.itacademy.s05.t01.n01.S05T01N01.enums.GameStatus;
import cat.itacademy.s05.t01.n01.S05T01N01.enums.PlayerMove;
import cat.itacademy.s05.t01.n01.S05T01N01.exception.custom.DeckIsEmptyException;
import cat.itacademy.s05.t01.n01.S05T01N01.exception.custom.GameAlreadyEndedException;
import cat.itacademy.s05.t01.n01.S05T01N01.exception.custom.GameNotFoundException;
import cat.itacademy.s05.t01.n01.S05T01N01.exception.custom.InvalidMoveException;
import cat.itacademy.s05.t01.n01.S05T01N01.model.Card;
import cat.itacademy.s05.t01.n01.S05T01N01.model.Game;
import cat.itacademy.s05.t01.n01.S05T01N01.repository.GameRepository;
import cat.itacademy.s05.t01.n01.S05T01N01.service.GameService;
import cat.itacademy.s05.t01.n01.S05T01N01.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class GameServiceImpl implements GameService {
    private final GameRepository gameRepository;
    private final PlayerService playerService;

    @Autowired
    public GameServiceImpl(GameRepository gameRepository, PlayerService playerService) {
        this.gameRepository = gameRepository;
        this.playerService = playerService;
    }

    @Override
    public Mono<Game> createGame(String playerName) {
        Game game = new GameBuilder()
                .withPlayerName(playerName)
                .initializeGame()
                .build();
        return gameRepository.save(game);
    }

    @Override
    public Flux<Game> getAllGames() {
        return gameRepository.findAll();
    }

    @Override
    public Mono<Game> getGame(String gameId) {
        return gameRepository.findById(gameId)
                .switchIfEmpty(Mono.error(new GameNotFoundException("Game not found with id: " + gameId)));
    }

    @Override
    public Mono<Game> playMove(String gameId, PlayerMove move) {
        return gameRepository.findById(gameId)
                .switchIfEmpty(Mono.error(new GameNotFoundException("Game not found with id: " + gameId)))
                .flatMap(game -> {
                    if (game.getStatus() == GameStatus.FINISHED) {
                        return Mono.error(new GameAlreadyEndedException("Game already ended with id: " + gameId));
                    }
                    if (move == null) {
                        return Mono.error(new InvalidMoveException("Move cannot be null."));
                    }
                    List<Card> remainingDeck = game.getRemainingDeck();
                    switch (move) {
                        case HIT:
                            if (remainingDeck == null || remainingDeck.isEmpty()) {
                                return Mono.error(new DeckIsEmptyException("Deck is empty."));
                            }
                            game.getPlayerHand().addCard(remainingDeck.remove(0));
                            if (game.getPlayerHand().calculateTotal() >= 21) {
                                game.setStatus(GameStatus.FINISHED);
                            }
                            break;
                        case STAND:
                            playCroupier(game);
                            game.setStatus(GameStatus.FINISHED);
                            break;
                        default:
                            return Mono.error(new InvalidMoveException("Move not recognized."));
                    }
                    return finalizeGame(game);
                });
    }

    @Override
    public Mono<Void> deleteGame(String gameId) {
        return gameRepository.deleteById(gameId);
    }

    private void playCroupier(Game game) {
        List<Card> remainingDeck = game.getRemainingDeck();
        if (game.getCroupierHand().calculateTotal() < 17 && remainingDeck != null && !remainingDeck.isEmpty()) {
            game.getCroupierHand().addCard(remainingDeck.remove(0));
            if (game.getCroupierHand().calculateTotal() < 21) {
                playCroupier(game);
            }
        }
    }

    private String getWinner(Game game) {
        int playerScore = game.getPlayerHand().calculateTotal();
        int croupierScore = game.getCroupierHand().calculateTotal();

        if (game.getPlayerHand().isBust() || ((playerScore < croupierScore) && (croupierScore <= 21))) {
            return "croupier";
        }
        if (game.getCroupierHand().isBust() || ((playerScore > croupierScore) && (playerScore <= 21))) {
            return "player";
        }
        return "tie";
    }

    private Mono<Void> setGameResult(Game game) {
        return playerService.findByName(game.getPlayerName())
                .flatMap(player -> {
                    if ("player".equals(game.getWinner())) {
                        player.addVictory();
                    }
                    return playerService.save(player);
                })
                .then();
    }

    private Mono<Game> finalizeGame(Game game) {
        String winner = getWinner(game);

        game.setWinner(winner);
        return setGameResult(game)
                .then(gameRepository.save(game));
    }
}

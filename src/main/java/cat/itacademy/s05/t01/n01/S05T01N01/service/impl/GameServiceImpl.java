package cat.itacademy.s05.t01.n01.S05T01N01.service.impl;

import cat.itacademy.s05.t01.n01.S05T01N01.enums.GameStatus;
import cat.itacademy.s05.t01.n01.S05T01N01.enums.PlayerMove;
import cat.itacademy.s05.t01.n01.S05T01N01.exception.GameAlreadyEndedException;
import cat.itacademy.s05.t01.n01.S05T01N01.model.Card;
import cat.itacademy.s05.t01.n01.S05T01N01.model.GameState;
import cat.itacademy.s05.t01.n01.S05T01N01.model.Player;
import cat.itacademy.s05.t01.n01.S05T01N01.repository.GameStateRepository;
import cat.itacademy.s05.t01.n01.S05T01N01.repository.PlayerRepository;
import cat.itacademy.s05.t01.n01.S05T01N01.service.GameService;
import cat.itacademy.s05.t01.n01.S05T01N01.utils.CardUtils;
import cat.itacademy.s05.t01.n01.S05T01N01.utils.CroupierUtils;
import cat.itacademy.s05.t01.n01.S05T01N01.utils.GameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.UUID;

@Service
public class GameServiceImpl implements GameService {

    @Autowired
    private GameStateRepository gameStateRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Override
    public Mono<GameState> startGame(String playerName) {
        return playerRepository.findByName(playerName)
                .switchIfEmpty(Mono.defer(() -> {
                    Player newPlayer = new Player();
                    newPlayer.setName(playerName);
                    newPlayer.setPlayerWinsCounter(0);
                    return playerRepository.save(newPlayer);
                }))
                .flatMap(existingPlayer -> {
                    GameState gameState = createInitialGameState(playerName);
                    return gameStateRepository.save(gameState);
                });
    }

    @Override
    public Mono<GameState> hit(String gameId) {
        return gameStateRepository.findByGameId(gameId)
                .flatMap(gameState -> {
                    if (gameState.getStatus() != GameStatus.IN_PROGRESS) {
                        return Mono.error(new GameAlreadyEndedException("The game is already ended."));
                    }
                    Card newCard = CardUtils.drawCard();
                    gameState.getPlayer().getCards().add(newCard);
                    gameState.getPlayer().setScore(CardUtils.calculateScore(gameState.getPlayer().getCards()));
                    GameUtils.addAction(gameState, "player", PlayerMove.HIT, newCard);

                    int newScore = gameState.getPlayer().getScore();
                    if (newScore > 21) {
                        return finishGame(gameState, "croupier")
                                .flatMap(gs -> gameStateRepository.save(gs));
                    } else if (newScore == 21) {
                        return finishGame(gameState, "player")
                                .flatMap(gs -> gameStateRepository.save(gs));
                    }
                    return gameStateRepository.save(gameState);
                });
    }

    @Override
    public Mono<GameState> stand(String gameId) {
        return gameStateRepository.findByGameId(gameId)
                .flatMap(gameState -> {
                    if (gameState.getStatus() != GameStatus.IN_PROGRESS) {
                        return Mono.error(new GameAlreadyEndedException("The game is already ended."));
                    }
                    GameUtils.addAction(gameState, "player", PlayerMove.STAND, null);
                    CroupierUtils.processCroupierTurn(gameState);
                    determineWinner(gameState);
                    return finishGame(gameState, gameState.getWinner())
                            .flatMap(gs -> gameStateRepository.save(gs));
                });
    }

    @Override
    public Mono<GameState> getGame(String gameId) {
        return gameStateRepository.findByGameId(gameId);
    }

    @Override
    public Flux<GameState> getAllGames() {
        return gameStateRepository.findAll();
    }

    @Override
    public Mono<Void> deleteGame(String gameId) {
        return gameStateRepository.findByGameId(gameId)
                .flatMap(gameState -> gameStateRepository.delete(gameState));
    }

    private GameState createInitialGameState(String playerName) {
        GameState gameState = new GameState();
        gameState.setGameId(UUID.randomUUID().toString());
        gameState.setStatus(GameStatus.IN_PROGRESS);
        gameState.setRound(1);
        gameState.setActions(new ArrayList<>());

        GameState.PlayerState playerState = new GameState.PlayerState();
        playerState.setName(playerName);
        playerState.setCards(new ArrayList<>());
        playerState.getCards().add(CardUtils.drawCard());
        playerState.getCards().add(CardUtils.drawCard());
        playerState.setScore(CardUtils.calculateScore(playerState.getCards()));

        GameState.CroupierState croupierState = new GameState.CroupierState();
        croupierState.setCards(new ArrayList<>());
        croupierState.getCards().add(CardUtils.drawCard());
        croupierState.setScore(CardUtils.calculateScore(croupierState.getCards()));

        gameState.setPlayer(playerState);
        gameState.setCroupier(croupierState);
        return gameState;
    }

    private void determineWinner(GameState gameState) {
        int playerScore = gameState.getPlayer().getScore();
        int croupierScore = gameState.getCroupier().getScore();
        if (croupierScore > 21 || playerScore > croupierScore) {
            gameState.setWinner("player");
        } else if (playerScore == croupierScore) {
            gameState.setWinner("draw");
        } else {
            gameState.setWinner("croupier");
        }
    }

    private Mono<GameState> finishGame(GameState gameState, String winner) {
        gameState.setStatus(GameStatus.FINISHED);
        gameState.setWinner(winner);
        if ("player".equals(winner)) {
            return savePlayerVictory(gameState.getPlayer().getName())
                    .thenReturn(gameState);
        }
        return Mono.just(gameState);
    }

    private Mono<Void> savePlayerVictory(String playerName) {
        return playerRepository.findByName(playerName)
                .flatMap(player -> {
                    player.setPlayerWinsCounter(player.getPlayerWinsCounter() + 1);
                    return playerRepository.save(player);
                })
                .then();
    }
}
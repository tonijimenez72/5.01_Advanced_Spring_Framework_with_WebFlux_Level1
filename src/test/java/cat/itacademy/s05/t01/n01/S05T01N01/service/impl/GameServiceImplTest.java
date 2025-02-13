package cat.itacademy.s05.t01.n01.S05T01N01.service.impl;

import cat.itacademy.s05.t01.n01.S05T01N01.enums.CardSuit;
import cat.itacademy.s05.t01.n01.S05T01N01.enums.CardValue;
import cat.itacademy.s05.t01.n01.S05T01N01.enums.GameStatus;
import cat.itacademy.s05.t01.n01.S05T01N01.enums.PlayerMove;
import cat.itacademy.s05.t01.n01.S05T01N01.exception.custom.DeckIsEmptyException;
import cat.itacademy.s05.t01.n01.S05T01N01.exception.custom.GameAlreadyEndedException;
import cat.itacademy.s05.t01.n01.S05T01N01.exception.custom.GameNotFoundException;
import cat.itacademy.s05.t01.n01.S05T01N01.exception.custom.InvalidMoveException;
import cat.itacademy.s05.t01.n01.S05T01N01.model.Card;
import cat.itacademy.s05.t01.n01.S05T01N01.model.Game;
import cat.itacademy.s05.t01.n01.S05T01N01.model.Hand;
import cat.itacademy.s05.t01.n01.S05T01N01.model.Player;
import cat.itacademy.s05.t01.n01.S05T01N01.repository.GameRepository;
import cat.itacademy.s05.t01.n01.S05T01N01.service.PlayerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GameServiceImplTest {

    @Mock
    private GameRepository gameRepository;

    @Mock
    private PlayerService playerService;

    @InjectMocks
    private GameServiceImpl gameService;

    private Game gameInProgress;
    private Game finishedGame;
    private List<Card> dummyDeck;

    @BeforeEach
    public void setup() {
        gameInProgress = new Game();
        gameInProgress.setId("game1");
        gameInProgress.setPlayerName("Joana Petita");
        gameInProgress.setStatus(GameStatus.IN_PROGRESS);
        Hand playerHand = new Hand();
        gameInProgress.setPlayerHand(playerHand);
        Hand croupierHand = new Hand();
        gameInProgress.setCroupierHand(croupierHand);
        dummyDeck = new ArrayList<>();
        dummyDeck.add(new Card(CardValue.TWO, CardSuit.HEARTS));
        gameInProgress.setRemainingDeck(new ArrayList<>(dummyDeck));
        finishedGame = new Game();
        finishedGame.setId("game2");
        finishedGame.setPlayerName("Joan Petit");
        finishedGame.setStatus(GameStatus.FINISHED);
        finishedGame.setPlayerHand(new Hand());
        finishedGame.setCroupierHand(new Hand());
        finishedGame.setRemainingDeck(new ArrayList<>(dummyDeck));
    }

    @Test
    public void testCreateGame() {
        Game newGame = new Game();
        newGame.setId("1L");
        newGame.setPlayerName("Joana Petita");
        when(gameRepository.save(any(Game.class))).thenReturn(Mono.just(newGame));
        Mono<Game> createdGameMono = gameService.createGame("Joana Petita");
        StepVerifier.create(createdGameMono)
                .expectNextMatches(game -> "1L".equals(game.getId())
                        && "Joana Petita".equals(game.getPlayerName()))
                .verifyComplete();
        verify(gameRepository, times(1)).save(any(Game.class));
    }

    @Test
    public void testGetGameFound() {
        when(gameRepository.findById("1L")).thenReturn(Mono.just(gameInProgress));
        Mono<Game> gameMono = gameService.getGame("1L");
        StepVerifier.create(gameMono)
                .expectNext(gameInProgress)
                .verifyComplete();
    }

    @Test
    public void testGetGameNotFound() {
        when(gameRepository.findById("game9999")).thenReturn(Mono.empty());
        Mono<Game> gameMono = gameService.getGame("game9999");
        StepVerifier.create(gameMono)
                .expectErrorMatches(throwable ->
                        throwable instanceof GameNotFoundException &&
                                throwable.getMessage().equals("Game not found with id: game9999"))
                .verify();
        verify(gameRepository, times(1)).findById("game9999");
    }

    @Test
    public void testPlayMoveHitSuccess() {
        when(gameRepository.findById("game1")).thenReturn(Mono.just(gameInProgress));
        when(gameRepository.save(any(Game.class))).thenReturn(Mono.just(gameInProgress));
        Player testPlayer = new Player(1L, "Joana Petitar", 0);
        when(playerService.findByName("Joana Petita")).thenReturn(Mono.just(testPlayer));
        when(playerService.save(any(Player.class))).thenReturn(Mono.just(testPlayer));
        Mono<Game> result = gameService.playMove("game1", PlayerMove.HIT);
        StepVerifier.create(result)
                .expectNextMatches(g -> g.getPlayerHand().calculateTotal() > 0)
                .verifyComplete();
        verify(gameRepository, times(1)).save(any(Game.class));
    }

    @Test
    public void testPlayMoveHitDeckEmpty() {
        gameInProgress.setRemainingDeck(new ArrayList<>());
        when(gameRepository.findById("game1")).thenReturn(Mono.just(gameInProgress));
        Mono<Game> result = gameService.playMove("game1", PlayerMove.HIT);
        StepVerifier.create(result)
                .expectErrorMatches(throwable ->
                        throwable instanceof DeckIsEmptyException &&
                                throwable.getMessage().equals("Deck is empty."))
                .verify();
        verify(gameRepository, never()).save(any(Game.class));
    }

    @Test
    public void testPlayMoveGameAlreadyEnded() {
        when(gameRepository.findById("game2")).thenReturn(Mono.just(finishedGame));
        Mono<Game> result = gameService.playMove("game2", PlayerMove.HIT);
        StepVerifier.create(result)
                .expectErrorMatches(throwable ->
                        throwable instanceof GameAlreadyEndedException &&
                                throwable.getMessage().equals("Game already ended with id: game2"))
                .verify();
        verify(gameRepository, never()).save(any(Game.class));
    }

    @Test
    public void testPlayMoveInvalidMoveNull() {
        when(gameRepository.findById("game1")).thenReturn(Mono.just(gameInProgress));
        Mono<Game> result = gameService.playMove("game1", null);
        StepVerifier.create(result)
                .expectErrorMatches(throwable ->
                        throwable instanceof InvalidMoveException &&
                                throwable.getMessage().equals("Move cannot be null."))
                .verify();
        verify(gameRepository, never()).save(any(Game.class));
    }

    @Test
    public void testPlayMoveStandSuccess() {
        List<Card> remaining = new ArrayList<>();
        remaining.add(new Card(CardValue.THREE, CardSuit.CLUBS));
        gameInProgress.setRemainingDeck(remaining);
        gameInProgress.setCroupierHand(new Hand());
        Player testPlayer = new Player(1L, "Joana Petita", 0);
        when(playerService.findByName("Joana Petita")).thenReturn(Mono.just(testPlayer));
        when(playerService.save(any(Player.class))).thenReturn(Mono.just(testPlayer));
        when(gameRepository.findById("game1")).thenReturn(Mono.just(gameInProgress));
        when(gameRepository.save(any(Game.class))).thenReturn(Mono.just(gameInProgress));
        Mono<Game> result = gameService.playMove("game1", PlayerMove.STAND);
        StepVerifier.create(result)
                .expectNextMatches(g -> g.getStatus() == GameStatus.FINISHED)
                .verifyComplete();
        verify(gameRepository, times(1)).save(any(Game.class));
    }

    @Test
    public void testDeleteGame() {
        when(gameRepository.deleteById("game1")).thenReturn(Mono.empty());
        Mono<Void> result = gameService.deleteGame("game1");
        StepVerifier.create(result)
                .verifyComplete();
        verify(gameRepository, times(1)).deleteById("game1");
    }
}
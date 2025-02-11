package cat.itacademy.s05.t01.n01.S05T01N01.service.impl;

import cat.itacademy.s05.t01.n01.S05T01N01.enums.GameStatus;
import cat.itacademy.s05.t01.n01.S05T01N01.exception.GameAlreadyEndedException;
import cat.itacademy.s05.t01.n01.S05T01N01.model.GameState;
import cat.itacademy.s05.t01.n01.S05T01N01.model.Player;
import cat.itacademy.s05.t01.n01.S05T01N01.repository.GameStateRepository;
import cat.itacademy.s05.t01.n01.S05T01N01.repository.PlayerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GameServiceImplTest {

    @Mock
    private GameStateRepository gameStateRepository;

    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private GameServiceImpl gameService;

    private GameState sampleGameState;
    private Player samplePlayer;

    @BeforeEach
    public void setup() {
        // Configurar un GameState de ejemplo.
        sampleGameState = new GameState();
        sampleGameState.setGameId("test-game-id");
        sampleGameState.setStatus(GameStatus.IN_PROGRESS);
        sampleGameState.setRound(1);
        sampleGameState.setActions(new ArrayList<>());

        // Configurar el PlayerState del GameState
        GameState.PlayerState playerState = new GameState.PlayerState();
        playerState.setName("Test Player");
        playerState.setCards(new ArrayList<>());
        // Aquí podrías agregar cartas de ejemplo, por ahora dejamos la lista vacía.
        playerState.setScore(0);
        sampleGameState.setPlayer(playerState);

        // Configurar el CroupierState del GameState
        GameState.CroupierState croupierState = new GameState.CroupierState();
        croupierState.setCards(new ArrayList<>());
        croupierState.setScore(0);
        sampleGameState.setCroupier(croupierState);

        // Configurar un Player de ejemplo.
        samplePlayer = new Player();
        samplePlayer.setId(1L);
        samplePlayer.setName("Test Player");
        samplePlayer.setPlayerWinsCounter(0);
    }

    @Test
    public void testStartGame() {
        // Simula que ya existe el jugador (o se crea si no existe).
        when(playerRepository.findByName("Test Player")).thenReturn(Mono.just(samplePlayer));
        when(gameStateRepository.save(any(GameState.class))).thenReturn(Mono.just(sampleGameState));

        Mono<GameState> result = gameService.startGame("Test Player");

        StepVerifier.create(result)
                .expectNextMatches(gs -> gs.getGameId().equals("test-game-id") &&
                        gs.getStatus() == GameStatus.IN_PROGRESS)
                .verifyComplete();
    }

    @Test
    public void testHitThrowsGameAlreadyEndedException() {
        sampleGameState.setStatus(GameStatus.FINISHED);
        when(gameStateRepository.findByGameId("test-game-id")).thenReturn(Mono.just(sampleGameState));

        Mono<GameState> result = gameService.hit("test-game-id");

        StepVerifier.create(result)
                .expectErrorMatches(ex -> ex instanceof GameAlreadyEndedException &&
                        ex.getMessage().equals("The game is already ended."))
                .verify();
    }

    @Test
    public void testGetGame() {
        when(gameStateRepository.findByGameId("test-game-id")).thenReturn(Mono.just(sampleGameState));

        Mono<GameState> result = gameService.getGame("test-game-id");

        StepVerifier.create(result)
                .expectNext(sampleGameState)
                .verifyComplete();
    }

    @Test
    public void testGetAllGames() {
        when(gameStateRepository.findAll()).thenReturn(Flux.just(sampleGameState));

        Flux<GameState> result = gameService.getAllGames();

        StepVerifier.create(result)
                .expectNext(sampleGameState)
                .verifyComplete();
    }

    @Test
    public void testDeleteGame() {
        when(gameStateRepository.findByGameId("test-game-id")).thenReturn(Mono.just(sampleGameState));
        when(gameStateRepository.delete(sampleGameState)).thenReturn(Mono.empty());

        Mono<Void> result = gameService.deleteGame("test-game-id");

        StepVerifier.create(result)
                .verifyComplete();
    }
}
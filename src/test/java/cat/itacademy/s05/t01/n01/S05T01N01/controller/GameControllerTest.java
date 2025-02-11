package cat.itacademy.s05.t01.n01.S05T01N01.controller;

import cat.itacademy.s05.t01.n01.S05T01N01.dto.NewGameRequest;
import cat.itacademy.s05.t01.n01.S05T01N01.dto.PlayMoveRequest;
import cat.itacademy.s05.t01.n01.S05T01N01.enums.GameStatus;
import cat.itacademy.s05.t01.n01.S05T01N01.model.GameState;
import cat.itacademy.s05.t01.n01.S05T01N01.service.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

@WebFluxTest(controllers = GameController.class)
public class GameControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private GameService gameService;

    private GameState sampleGameState;

    @BeforeEach
    public void setup() {
        // Configuración básica de un GameState de ejemplo.
        sampleGameState = new GameState();
        sampleGameState.setGameId("sample-game-id");
        sampleGameState.setStatus(GameStatus.IN_PROGRESS);
        sampleGameState.setRound(1);
        sampleGameState.setActions(new ArrayList<>());
        // Asumimos que sampleGameState.player y sampleGameState.croupier se configuran internamente.
        // En un caso real, se crearían instancias de PlayerState y CroupierState con datos de prueba.
    }

    @Test
    public void testCreateGame() {
        NewGameRequest request = new NewGameRequest();
        request.setPlayerName("Test Player");

        Mockito.when(gameService.startGame("Test Player")).thenReturn(Mono.just(sampleGameState));

        webTestClient.post()
                .uri("/game/new")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(GameState.class)
                .isEqualTo(sampleGameState);
    }

    @Test
    public void testGetGame() {
        Mockito.when(gameService.getGame("sample-game-id")).thenReturn(Mono.just(sampleGameState));

        webTestClient.get()
                .uri("/game/{id}", "sample-game-id")
                .exchange()
                .expectStatus().isOk()
                .expectBody(GameState.class)
                .isEqualTo(sampleGameState);
    }

    @Test
    public void testGetAllGames() {
        Mockito.when(gameService.getAllGames()).thenReturn(Flux.just(sampleGameState));

        webTestClient.get()
                .uri("/game")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(GameState.class)
                .contains(sampleGameState);
    }

    @Test
    public void testPlayHit() {
        PlayMoveRequest request = new PlayMoveRequest();
        request.setMove("HIT");

        Mockito.when(gameService.hit("sample-game-id")).thenReturn(Mono.just(sampleGameState));

        webTestClient.post()
                .uri("/game/{id}/play", "sample-game-id")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(GameState.class)
                .isEqualTo(sampleGameState);
    }

    @Test
    public void testPlayStand() {
        PlayMoveRequest request = new PlayMoveRequest();
        request.setMove("STAND");

        Mockito.when(gameService.stand("sample-game-id")).thenReturn(Mono.just(sampleGameState));

        webTestClient.post()
                .uri("/game/{id}/play", "sample-game-id")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(GameState.class)
                .isEqualTo(sampleGameState);
    }

    @Test
    public void testDeleteGame() {
        Mockito.when(gameService.deleteGame("sample-game-id")).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/game/{id}/delete", "sample-game-id")
                .exchange()
                .expectStatus().isNoContent();
    }
}
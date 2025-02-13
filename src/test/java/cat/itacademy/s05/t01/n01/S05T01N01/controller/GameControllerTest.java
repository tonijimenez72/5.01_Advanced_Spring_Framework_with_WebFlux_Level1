package cat.itacademy.s05.t01.n01.S05T01N01.controller;

import cat.itacademy.s05.t01.n01.S05T01N01.dto.NewGameRequest;
import cat.itacademy.s05.t01.n01.S05T01N01.dto.PlayMoveRequest;
import cat.itacademy.s05.t01.n01.S05T01N01.enums.PlayerMove;
import cat.itacademy.s05.t01.n01.S05T01N01.model.Game;
import cat.itacademy.s05.t01.n01.S05T01N01.service.GameService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@WebFluxTest(controllers = GameController.class)
@Import(GameControllerTest.Config.class)
public class GameControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private GameService gameService;

    @TestConfiguration
    static class Config {
        @Bean
        public GameService gameService() {
            return Mockito.mock(GameService.class);
        }
    }

    @Test
    public void testCreateGame() {
        NewGameRequest request = new NewGameRequest();
        request.setPlayerName("John Doe");

        Game game = new Game();
        game.setId("123");
        game.setPlayerName("John Doe");

        Mockito.when(gameService.createGame("John Doe")).thenReturn(Mono.just(game));

        webTestClient.post()
                .uri("/game/new")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Game.class)
                .isEqualTo(game);
    }

    @Test
    public void testGetGame() {
        Game game = new Game();
        game.setId("1234");
        game.setPlayerName("Joana Petita");

        Mockito.when(gameService.getGame("1234")).thenReturn(Mono.just(game));

        webTestClient.get()
                .uri("/game/1234")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Game.class)
                .isEqualTo(game);
    }

    @Test
    public void testPlayMove() {
        PlayMoveRequest request = new PlayMoveRequest();
        request.setMove(PlayerMove.HIT);

        Game game = new Game();
        game.setId("1234");
        game.setPlayerName("Joana Petita");

        Mockito.when(gameService.playMove("1234", PlayerMove.HIT)).thenReturn(Mono.just(game));

        webTestClient.post()
                .uri("/game/1234/play")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Game.class)
                .isEqualTo(game);
    }

    @Test
    public void testDeleteGame() {
        Mockito.when(gameService.deleteGame("1234")).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/game/1234/delete")
                .exchange()
                .expectStatus().isNoContent();
    }
}
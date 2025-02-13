package cat.itacademy.s05.t01.n01.S05T01N01.controller;

import cat.itacademy.s05.t01.n01.S05T01N01.dto.NewPlayerNameRequest;
import cat.itacademy.s05.t01.n01.S05T01N01.model.Player;
import cat.itacademy.s05.t01.n01.S05T01N01.service.PlayerService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.beans.factory.annotation.Autowired;

@WebFluxTest(controllers = PlayerController.class)
@Import(PlayerControllerTest.Config.class)
public class PlayerControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private PlayerService playerService;

    @TestConfiguration
    static class Config {
        @Bean
        public PlayerService playerService() {
            return Mockito.mock(PlayerService.class);
        }
    }

    @Test
    public void testUpdatePlayerName() {
        Long playerId = 1L;
        NewPlayerNameRequest request = new NewPlayerNameRequest();
        request.setPlayerName("Jane Doe");

        Player player = new Player();
        player.setId(playerId);
        player.setName("Jane Doe");
        player.setPlayerWinsCounter(100);

        Mockito.when(playerService.updatePlayerName(playerId, "Jane Doe"))
                .thenReturn(Mono.just(player));

        webTestClient.put()
                .uri("/player/{playerId}", playerId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Player.class)
                .isEqualTo(player);
    }

    @Test
    public void testGetRanking() {
        Player player1 = new Player();
        player1.setId(1L);
        player1.setName("Joana Petita");
        player1.setPlayerWinsCounter(2);

        Player player2 = new Player();
        player2.setId(2L);
        player2.setName("Joan Petit");
        player2.setPlayerWinsCounter(1);

        Mockito.when(playerService.getRanking())
                .thenReturn(Flux.just(player1, player2));

        webTestClient.get()
                .uri("/ranking")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Player.class)
                .hasSize(2)
                .contains(player1, player2);
    }
}
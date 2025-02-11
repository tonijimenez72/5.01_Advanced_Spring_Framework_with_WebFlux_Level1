package cat.itacademy.s05.t01.n01.S05T01N01.controller;

import cat.itacademy.s05.t01.n01.S05T01N01.model.Player;
import cat.itacademy.s05.t01.n01.S05T01N01.service.PlayerService;
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

@WebFluxTest(controllers = PlayerController.class)
public class PlayerControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private PlayerService playerService;

    private Player samplePlayer;

    @BeforeEach
    public void setup() {
        samplePlayer = new Player();
        samplePlayer.setId(1L);
        samplePlayer.setName("Test Player");
        samplePlayer.setPlayerWinsCounter(5);
    }

    @Test
    public void testUpdatePlayerName() {
        Player updateRequest = new Player();
        updateRequest.setName("Updated Name");

        Mockito.when(playerService.updatePlayerName(1L, "Updated Name")).thenReturn(Mono.just(samplePlayer));

        webTestClient.put()
                .uri("/player/{playerId}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updateRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Player.class)
                .isEqualTo(samplePlayer);
    }

    @Test
    public void testGetRanking() {
        Mockito.when(playerService.getRanking()).thenReturn(Flux.just(samplePlayer));

        webTestClient.get()
                .uri("/ranking")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Player.class)
                .contains(samplePlayer);
    }
}
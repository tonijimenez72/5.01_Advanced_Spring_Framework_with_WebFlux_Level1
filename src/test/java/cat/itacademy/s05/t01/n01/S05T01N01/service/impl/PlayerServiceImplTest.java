package cat.itacademy.s05.t01.n01.S05T01N01.service.impl;

import cat.itacademy.s05.t01.n01.S05T01N01.model.Player;
import cat.itacademy.s05.t01.n01.S05T01N01.repository.PlayerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PlayerServiceImplTest {

    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private PlayerServiceImpl playerService;

    private Player samplePlayer;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        samplePlayer = new Player();
        samplePlayer.setId(1L);
        samplePlayer.setName("Test Player");
        samplePlayer.setPlayerWinsCounter(0);
    }

    @Test
    public void testCreatePlayer() {
        when(playerRepository.save(any(Player.class))).thenReturn(Mono.just(samplePlayer));

        Mono<Player> result = playerService.createPlayer(samplePlayer);

        StepVerifier.create(result)
                .expectNextMatches(player -> player.getId().equals(1L)
                        && "Test Player".equals(player.getName()))
                .verifyComplete();

        verify(playerRepository, times(1)).save(samplePlayer);
    }

    @Test
    public void testUpdatePlayerName() {
        Player updatedPlayer = new Player();
        updatedPlayer.setId(1L);
        updatedPlayer.setName("Updated Player");
        updatedPlayer.setPlayerWinsCounter(0);

        when(playerRepository.findById(1L)).thenReturn(Mono.just(samplePlayer));
        when(playerRepository.save(any(Player.class))).thenReturn(Mono.just(updatedPlayer));

        Mono<Player> result = playerService.updatePlayerName(1L, "Updated Player");

        StepVerifier.create(result)
                .expectNextMatches(player -> "Updated Player".equals(player.getName()))
                .verifyComplete();

        verify(playerRepository, times(1)).findById(1L);
        verify(playerRepository, times(1)).save(any(Player.class));
    }

    @Test
    public void testGetPlayerById() {
        when(playerRepository.findById(1L)).thenReturn(Mono.just(samplePlayer));

        Mono<Player> result = playerService.getPlayerById(1L);

        StepVerifier.create(result)
                .expectNext(samplePlayer)
                .verifyComplete();

        verify(playerRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetRanking() {
        when(playerRepository.findAll()).thenReturn(Flux.just(samplePlayer));

        Flux<Player> result = playerService.getRanking();

        StepVerifier.create(result)
                .expectNext(samplePlayer)
                .verifyComplete();

        verify(playerRepository, times(1)).findAll();
    }

    @Test
    public void testDeletePlayer() {
        when(playerRepository.deleteById(1L)).thenReturn(Mono.empty());

        Mono<Void> result = playerService.deletePlayer(1L);

        StepVerifier.create(result)
                .verifyComplete();

        verify(playerRepository, times(1)).deleteById(1L);
    }
}
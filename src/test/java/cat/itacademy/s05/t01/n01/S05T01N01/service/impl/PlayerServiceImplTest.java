package cat.itacademy.s05.t01.n01.S05T01N01.service.impl;

import cat.itacademy.s05.t01.n01.S05T01N01.exception.custom.PlayerNotFoundException;
import cat.itacademy.s05.t01.n01.S05T01N01.exception.custom.RankingIsEmptyException;
import cat.itacademy.s05.t01.n01.S05T01N01.model.Player;
import cat.itacademy.s05.t01.n01.S05T01N01.repository.PlayerRepository;
import cat.itacademy.s05.t01.n01.S05T01N01.service.PlayerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PlayerServiceImplTest {

    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private PlayerServiceImpl playerService;

    @Test
    public void testUpdatePlayerName_Success() {
        Player existingPlayer = new Player(1L, "Joan Vell", 0);
        when(playerRepository.findById(1L)).thenReturn(Mono.just(existingPlayer));
        when(playerRepository.save(existingPlayer)).thenReturn(Mono.just(new Player(1L, "Joan Nou", 0)));

        Mono<Player> result = playerService.updatePlayerName(1L, "Joan Nou");

        StepVerifier.create(result)
                .expectNextMatches(player -> "Joan Nou".equals(player.getName()))
                .verifyComplete();

        verify(playerRepository, times(1)).findById(1L);
        verify(playerRepository, times(1)).save(existingPlayer);
    }

    @Test
    public void testUpdatePlayerName_NotFound() {
        when(playerRepository.findById(2L)).thenReturn(Mono.empty());

        Mono<Player> result = playerService.updatePlayerName(2L, "Joan Nou");

        StepVerifier.create(result)
                .expectErrorMatches(throwable ->
                        throwable instanceof PlayerNotFoundException &&
                                throwable.getMessage().equals("Player not found with id: 2"))
                .verify();

        verify(playerRepository, times(1)).findById(2L);
        verify(playerRepository, never()).save(any());
    }

    @Test
    public void testGetRanking_Success() {
        Player player1 = new Player(1L, "Joan Nou", 2);
        Player player2 = new Player(2L, "Joan Vell", 3);
        when(playerRepository.findAll()).thenReturn(Flux.just(player1, player2));

        Flux<Player> result = playerService.getRanking();

        StepVerifier.create(result)
                .expectNext(player1)
                .expectNext(player2)
                .verifyComplete();

        verify(playerRepository, times(1)).findAll();
    }

    @Test
    public void testGetRanking_Empty() {
        when(playerRepository.findAll()).thenReturn(Flux.empty());

        Flux<Player> result = playerService.getRanking();

        StepVerifier.create(result)
                .expectErrorMatches(throwable ->
                        throwable instanceof RankingIsEmptyException &&
                                throwable.getMessage().equals("Ranking is empty"))
                .verify();

        verify(playerRepository, times(1)).findAll();
    }

    @Test
    public void testFindByName_Found() {
        Player player = new Player(1L, "Joan Nou", 0);
        when(playerRepository.findByName("Joan Nou")).thenReturn(Mono.just(player));

        Mono<Player> result = playerService.findByName("Joan Nou");

        StepVerifier.create(result)
                .expectNext(player)
                .verifyComplete();

        verify(playerRepository, times(1)).findByName("Joan Nou");
    }

    @Test
    public void testFindByName_NotFound_CreatesNew() {
        when(playerRepository.findByName("Joan Nou")).thenReturn(Mono.empty());
        Player newPlayer = new Player(3L, "Joan Nou", 0);
        when(playerRepository.save(any(Player.class))).thenReturn(Mono.just(newPlayer));

        Mono<Player> result = playerService.findByName("Joan Nou");

        StepVerifier.create(result)
                .expectNextMatches(player -> "Joan Nou".equals(player.getName()) && player.getPlayerWinsCounter() == 0)
                .verifyComplete();

        verify(playerRepository, times(1)).findByName("Joan Nou");
        verify(playerRepository, times(1)).save(any(Player.class));
    }

    @Test
    public void testSave() {
        Player player = new Player(4L, "Joan Nou", 5);
        when(playerRepository.save(player)).thenReturn(Mono.just(player));

        Mono<Player> result = playerService.save(player);

        StepVerifier.create(result)
                .expectNext(player)
                .verifyComplete();

        verify(playerRepository, times(1)).save(player);
    }
}
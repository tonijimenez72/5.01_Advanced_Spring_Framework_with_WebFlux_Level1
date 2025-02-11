package cat.itacademy.s05.t01.n01.S05T01N01.utils;

import cat.itacademy.s05.t01.n01.S05T01N01.enums.PlayerMove;
import cat.itacademy.s05.t01.n01.S05T01N01.model.Card;
import cat.itacademy.s05.t01.n01.S05T01N01.model.GameState;

import java.time.Instant;

public class GameUtils {
    public static void addAction(GameState gameState, String player, PlayerMove move, Card card) {
        GameState.Action action = new GameState.Action();
        action.setActor(player);
        action.setMove(move);
        action.setTimestamp(Instant.now().toString());
        if (card != null) {
            action.setCard(card);
        }
        gameState.getActions().add(action);
    }
}
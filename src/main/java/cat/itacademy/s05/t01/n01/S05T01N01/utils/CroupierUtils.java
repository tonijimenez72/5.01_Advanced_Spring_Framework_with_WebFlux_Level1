package cat.itacademy.s05.t01.n01.S05T01N01.utils;

import cat.itacademy.s05.t01.n01.S05T01N01.enums.PlayerMove;
import cat.itacademy.s05.t01.n01.S05T01N01.model.Card;
import cat.itacademy.s05.t01.n01.S05T01N01.model.GameState;

public class CroupierUtils {
    public static void processCroupierTurn(GameState gameState) {
        while (gameState.getCroupier().getScore() < 17) {
            Card newCard = CardUtils.drawCard();
            gameState.getCroupier().getCards().add(newCard);
            int croupierScore = CardUtils.calculateScore(gameState.getCroupier().getCards());
            gameState.getCroupier().setScore(croupierScore);
            GameUtils.addAction(gameState, "croupier", PlayerMove.HIT, newCard);
        }
    }
}

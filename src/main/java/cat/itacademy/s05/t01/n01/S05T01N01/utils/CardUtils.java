package cat.itacademy.s05.t01.n01.S05T01N01.utils;

import cat.itacademy.s05.t01.n01.S05T01N01.enums.CardSuit;
import cat.itacademy.s05.t01.n01.S05T01N01.enums.CardValue;
import cat.itacademy.s05.t01.n01.S05T01N01.model.Card;

import java.util.List;
import java.util.Random;

public class CardUtils {
    private static final Random RANDOM = new Random();

    public static Card drawCard() {
        CardValue[] values = CardValue.values();
        CardSuit[] suits = CardSuit.values();
        CardValue value = values[RANDOM.nextInt(values.length)];
        CardSuit suit = suits[RANDOM.nextInt(suits.length)];
        Card card = new Card();
        card.setValue(value);
        card.setSuit(suit);
        return card;
    }

    public static int calculateScore(List<Card> cards) {
        int score = 0;
        int aces = 0;
        for (Card card : cards) {
            score += card.getValue().getValue();
            if (card.getValue() == CardValue.ACE) {
                aces++;
            }
        }
        while (score > 21 && aces > 0) {
            score -= 10;
            aces--;
        }
        return score;
    }
}

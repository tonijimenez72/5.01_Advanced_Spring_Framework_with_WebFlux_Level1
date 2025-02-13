package cat.itacademy.s05.t01.n01.S05T01N01.model;

import cat.itacademy.s05.t01.n01.S05T01N01.enums.CardValue;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Hand {
    private List<Card> cards = new ArrayList<>();

    public void addCard(Card card) {
        cards.add(card);
    }

    public int calculateTotal() {
        int total = 0;
        int aceCount = 0;
        for (Card card : cards) {
            total += card.getValue().getValue();
            if (card.getValue() == CardValue.ACE) {
                aceCount++;
            }
        }

        while (total > 21 && aceCount > 0) {
            total -= 10;
            aceCount--;
        }
        return total;
    }

    public boolean isBust() {
        return calculateTotal() > 21;
    }

    @JsonProperty("score")
    public int getScore() {
        return calculateTotal();
    }
}

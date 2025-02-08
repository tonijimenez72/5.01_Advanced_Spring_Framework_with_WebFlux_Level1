package cat.itacademy.s05.t01.n01.S05T01N01.model;

import cat.itacademy.s05.t01.n01.S05T01N01.enums.CardValue;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
public class Hand {
    private final List<Card> cards = new ArrayList<>();

    public void addCard(Card card) {
        cards.add(card);
    }

    public int calculateTotal() {
        int total = cards.stream()
                .mapToInt(card -> card.getValue().getValue())
                .sum();

        long aceCount = cards.stream()
                .filter(card -> card.getValue() == CardValue.ACE)
                .count();

        while (total > 21 && aceCount > 0) {
            total -= 10;
            aceCount--;
        }

        return total;
    }

    public void reset() {
        cards.clear();
    }
}
package cat.itacademy.s05.t01.n01.S05T01N01.model;

import cat.itacademy.s05.t01.n01.S05T01N01.enums.CardSuit;
import cat.itacademy.s05.t01.n01.S05T01N01.enums.CardValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

public class Card {
    private final CardSuit suit;
    private final CardValue value;

    public Card(CardValue value, CardSuit suit) {
        this.value = value;
        this.suit = suit;
    }

    public CardSuit getSuit() {
        return suit;
    }

    public CardValue getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value + " of " + suit;
    }
}

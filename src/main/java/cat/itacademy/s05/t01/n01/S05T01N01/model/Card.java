package cat.itacademy.s05.t01.n01.S05T01N01.model;

import cat.itacademy.s05.t01.n01.S05T01N01.enums.CardSuit;
import cat.itacademy.s05.t01.n01.S05T01N01.enums.CardValue;
import lombok.Data;

@Data
public class Card {
    private CardValue value;
    private CardSuit suit;

    @Override
    public String toString() {
        return value.toString() + " of " + suit.toString();
    }
}

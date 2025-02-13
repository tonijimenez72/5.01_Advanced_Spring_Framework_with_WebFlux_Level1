package cat.itacademy.s05.t01.n01.S05T01N01.builder;

import cat.itacademy.s05.t01.n01.S05T01N01.enums.GameStatus;
import cat.itacademy.s05.t01.n01.S05T01N01.model.Card;
import cat.itacademy.s05.t01.n01.S05T01N01.model.Deck;
import cat.itacademy.s05.t01.n01.S05T01N01.model.Game;
import cat.itacademy.s05.t01.n01.S05T01N01.model.Hand;

import java.util.List;

public class GameBuilder {

    private String playerName;
    private GameStatus status;
    private Hand playerHand;
    private Hand croupierHand;
    private List<Card> remainingDeck;

    public GameBuilder withPlayerName(String playerName) {
        this.playerName = playerName;
        return this;
    }

    public GameBuilder initializeGame() {
        Deck deck = new Deck();
        this.playerHand = new Hand();
        this.croupierHand = new Hand();

        playerHand.addCard(deck.drawCard());
        playerHand.addCard(deck.drawCard());

        croupierHand.addCard(deck.drawCard());
        croupierHand.addCard(deck.drawCard());

        this.remainingDeck = deck.getCards();
        this.status = GameStatus.IN_PROGRESS;
        return this;
    }

    public Game build() {
        Game game = new Game();
        game.setPlayerName(playerName);
        game.setStatus(status);
        game.setPlayerHand(playerHand);
        game.setCroupierHand(croupierHand);
        game.setRemainingDeck(remainingDeck);
        return game;
    }
}
package cat.itacademy.s05.t01.n01.S05T01N01.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
public class Game {
    private String id = UUID.randomUUID().toString();
    private List<Player> players = new ArrayList<>();
    private Deck deck = new Deck();
    private String status = "New";

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void start() {
        deck.shuffle();
        status = "In Progress";
    }

    public void end() {
        status = "Finished";
    }
}
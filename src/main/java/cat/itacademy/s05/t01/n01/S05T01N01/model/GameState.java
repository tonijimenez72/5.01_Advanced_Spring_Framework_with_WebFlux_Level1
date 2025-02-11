package cat.itacademy.s05.t01.n01.S05T01N01.model;

import cat.itacademy.s05.t01.n01.S05T01N01.enums.*;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "game_states")
public class GameState {
    @Id
    private String id;
    private String gameId;
    private GameStatus status;
    private int round;
    private PlayerState player;
    private CroupierState croupier;
    private List<Action> actions;
    private String winner;

    @Data
    public static class PlayerState {
        private String name;
        private List<Card> cards;
        private int score;
    }

    @Data
    public static class CroupierState {
        private List<Card> cards;
        private int score;
    }

    @Data
    public static class Action {
        private String actor;
        private PlayerMove move;
        private Card card;
        private String timestamp;
    }
}

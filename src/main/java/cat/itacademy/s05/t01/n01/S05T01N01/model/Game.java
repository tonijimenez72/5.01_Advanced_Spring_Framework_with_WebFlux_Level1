package cat.itacademy.s05.t01.n01.S05T01N01.model;

import cat.itacademy.s05.t01.n01.S05T01N01.enums.GameStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "games")
@JsonPropertyOrder({"id", "playerName", "status", "playerHand", "croupierHand", "winner", "remainingDeck"})
public class Game {
    @Id
    private String id;
    private String playerName;
    private GameStatus status;
    private Hand playerHand;
    private Hand croupierHand;
    private String winner;
    private List<Card> remainingDeck;

}

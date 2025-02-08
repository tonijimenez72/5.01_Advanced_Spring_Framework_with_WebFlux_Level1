package cat.itacademy.s05.t01.n01.S05T01N01.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Getter
@ToString
@NoArgsConstructor // Requerido por JPA/R2DBC
@Table("player") // Tabla en la base de datos
public class Player {
    @Id
    private String id;
    private String name;
    private final Hand hand = new Hand();
    private boolean isCroupier;

    public Player(String name, boolean isCroupier) {
        this.id = UUID.randomUUID().toString();
        this.name = isCroupier ? "Croupier" : name;
        this.isCroupier = isCroupier;
    }

    public void addCard(Card card) {
        hand.addCard(card);
    }

    public int getTotal() {
        return hand.calculateTotal();
    }

    public void resetHand() {
        hand.reset();
    }
}
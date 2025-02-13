package cat.itacademy.s05.t01.n01.S05T01N01.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("player")
public class Player {
    @Id
    private Long id;
    private String name;
    private int playerWinsCounter;

    public int addVictory(){
        return playerWinsCounter++;
    }
}
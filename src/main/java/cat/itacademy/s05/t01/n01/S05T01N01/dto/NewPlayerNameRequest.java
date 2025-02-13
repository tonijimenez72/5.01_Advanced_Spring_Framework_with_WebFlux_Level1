package cat.itacademy.s05.t01.n01.S05T01N01.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class NewPlayerNameRequest {
    @NotBlank(message = "Player name cannot be empty.")
    private String playerName;
}
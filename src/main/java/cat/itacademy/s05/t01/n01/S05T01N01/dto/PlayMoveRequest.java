package cat.itacademy.s05.t01.n01.S05T01N01.dto;

import cat.itacademy.s05.t01.n01.S05T01N01.enums.PlayerMove;
import lombok.Data;

@Data
public class PlayMoveRequest {
    private PlayerMove move;
}
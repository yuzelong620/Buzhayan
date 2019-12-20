package com.kratos.game.herphone.player.message;

import com.kratos.game.herphone.player.domain.Player;
import lombok.Data;

@Data
public class ResExplorationPlayer {
    private String nickName;
    private String avatarUrl;
    private int rank;
    private int exploration;

    public ResExplorationPlayer() {}

    public ResExplorationPlayer(Player player, int rank, int exploration) {
        this.rank = rank;
        this.nickName = player.decodeName();
        this.avatarUrl = player.getAvatarUrl();
        this.exploration = exploration;
    }
}

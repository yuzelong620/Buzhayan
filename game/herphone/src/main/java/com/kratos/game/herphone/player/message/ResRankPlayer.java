package com.kratos.game.herphone.player.message;

import com.kratos.game.herphone.player.domain.Player;
import lombok.Data;

@Data
public class ResRankPlayer {
    private String playerId;
    private String nickName;
    private String avatarUrl;
    private int rank;
    private int achievement;
    private Long lastAddAchievementTime;

    public ResRankPlayer() {}

    public ResRankPlayer(Player player, int rank) {
        this.playerId = player.getId().toString();
        this.rank = rank;
        this.nickName = player.decodeName();
        this.avatarUrl = player.getAvatarUrl();
        this.achievement = player.getAchievement();
        this.lastAddAchievementTime = player.getLastAddAchievementTime();
    }
}

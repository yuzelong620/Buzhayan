package com.kratos.game.herphone.game.chose.message;

import com.kratos.game.herphone.player.message.ResRankPlayer;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ResEachExplorationRank {
    private Map<Integer, List<ResRankPlayer>> rank;
}

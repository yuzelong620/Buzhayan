package com.kratos.game.herphone.game.chose.service;

import com.kratos.engine.framework.crud.ICrudService;
import com.kratos.game.herphone.game.chose.domain.ChosenOption;
import com.kratos.game.herphone.game.chose.message.ReqPlayerChose;
import com.kratos.game.herphone.game.chose.message.ResEachExplorationRank;
import com.kratos.game.herphone.game.chose.message.ResGameProgress;

public interface ChosenOptionService extends ICrudService<Long, ChosenOption> {
    /**
     * 做出选择
     * @param request 参数
     */
    void chose(ReqPlayerChose request);

    /**
     * 获取所有游戏完成度
     * @return 游戏完成度
     */
    ResGameProgress getAllProgress();

    /**
     * 查询所有游戏探索度排行榜
     * @return 所有游戏探索度排行榜
     */
    ResEachExplorationRank getAllRank();

    /**
     * 查询单个游戏的探索度
     * @param gameId 游戏id
     * @return 探索度
     */
    Long getProgress(int gameId);
    
}

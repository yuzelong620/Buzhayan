package com.kratos.game.herphone.score.service;

import com.kratos.engine.framework.crud.ICrudService;
import com.kratos.game.herphone.score.domain.Score;

import java.util.Map;


public interface ScoreService extends ICrudService<Long, Score> {

    /**
     * 获取评分
     * @param gameId 游戏id
     * @return 评分
     */
    Integer getScore(int gameId);

    /**
     * 评分
     * @param gameId 游戏id
     * @param score 分数
     */
    void rate(int gameId, Integer score);

    /**
     * 获取所有游戏的评分
     * @return 游戏评分
     */
    Map<Integer, String> getAllScore();
    
    Map<Integer, Integer> getScorePeopleNum();
}

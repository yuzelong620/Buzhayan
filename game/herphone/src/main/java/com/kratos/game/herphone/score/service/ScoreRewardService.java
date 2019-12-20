package com.kratos.game.herphone.score.service;
public interface ScoreRewardService {
	/**
	 * 判断领取评分奖励
	 * @param gameid
	 * @return int state 0未领取 1已领取
	 * */
	int jugeGetScoreReward(int gameId);
	int selectScoreReward(int gameId);
}

package com.kratos.game.herphone.score.service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.kratos.engine.framework.common.utils.IdGenerator;
import com.kratos.engine.framework.common.utils.JedisUtils;
import com.kratos.engine.framework.crud.BaseCrudService;
import com.kratos.engine.framework.crud.Param;
import com.kratos.game.herphone.cache.AppCache;
import com.kratos.game.herphone.player.PlayerContext;
import com.kratos.game.herphone.player.domain.Player;
import com.kratos.game.herphone.scheduled.ScheduledService;
import com.kratos.game.herphone.score.domain.Score;

import lombok.extern.log4j.Log4j;

@Log4j
@Component
public class ScoreServiceImpl extends BaseCrudService<Long, Score> implements ScoreService {
	@Autowired
	private ScheduledService scheduledService;

    @Override
    public Integer getScore(int gameId) {
        Player player = PlayerContext.getPlayer();
        List<Score> scores = findByParams(Param.equal("gameId", gameId), Param.equal("playerId", player.getId()));
        if(scores != null && !scores.isEmpty()) {
            return scores.get(0).getScore();
        }
        return null;
    }

    @Override
    public void rate(int gameId, Integer score) {
        Player player = PlayerContext.getPlayer();
        Score scoreBean = new Score();
        scoreBean.setId(IdGenerator.getNextId());
        scoreBean.setGameId(gameId);
        scoreBean.setPlayerId(player.getId());
        scoreBean.setRateTime(System.currentTimeMillis());
        scoreBean.setScore(score);
        this.cacheAndPersist(scoreBean.getId(), scoreBean);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<Integer, String> getAllScore() {
        Long expiration = JedisUtils.getInstance().get(AppCache.gameScoreExpiration, Long.class);
        if(expiration != null) {
            if(System.currentTimeMillis() < expiration) {
                return JedisUtils.getInstance().get(AppCache.gameScore, Map.class);
            }
        }
        log.error("getAllScore()方法未从redis里获取到对象");
        return scheduledService.getAllScore();
    }

	@SuppressWarnings("unchecked")
	@Override
	public Map<Integer, Integer> getScorePeopleNum() {
		Map<Integer, Integer> resmap = JedisUtils.getInstance().get(AppCache.scorePeopleNum, HashMap.class);
		if (resmap == null) {
			resmap = new HashMap<>();
			Query playerNum = em.createNativeQuery("SELECT game_id,COUNT(player_id) FROM score where score != 0 group by game_id");
			List<Object[]> ResultList = playerNum.getResultList();
			
			if (ResultList == null ||ResultList.size() == 0) {
				return resmap;
			}
			for (Object[] obj : ResultList) {
				resmap.put(Integer.parseInt(obj[0].toString()), Integer.parseInt(obj[1].toString()));
			}
			 JedisUtils.getInstance().set(AppCache.scorePeopleNum, resmap);
		}		
		return resmap;
	}
}

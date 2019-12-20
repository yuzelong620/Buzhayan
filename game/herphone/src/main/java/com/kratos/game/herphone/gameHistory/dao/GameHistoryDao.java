package com.kratos.game.herphone.gameHistory.dao;

import java.util.List;

import org.hutu.mongo.dao.BaseDao;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.kratos.game.herphone.discuss.entity.DiscussEntity;
import com.kratos.game.herphone.gameHistory.entity.GameHistoryEntity;

@Repository
public class GameHistoryDao extends BaseDao<GameHistoryEntity>{
	
	
}

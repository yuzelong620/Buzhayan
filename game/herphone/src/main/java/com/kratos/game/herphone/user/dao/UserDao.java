package com.kratos.game.herphone.user.dao;

import java.util.List;
import java.util.Set;

import org.hutu.mongo.dao.BaseDao;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.kratos.game.herphone.user.entity.UserEntity;
@Repository
public class UserDao extends BaseDao<UserEntity>{
	
	private static final String PLAYERID = "playerId";
	
	public UserEntity findByPlayerId(long playerId) {
		Criteria criteria= Criteria. where("playerId").is(playerId);
		Query query=new Query(criteria);
		UserEntity userEntity = findOne(query);
		return userEntity;
	}
	
	/**
	 * 根据玩家ID查询所有信息
	 */
	public List<UserEntity> findAllByPlayerId(List<String> ranKingDataList) {
		Criteria criteria= Criteria. where("playerId").in(ranKingDataList);
		Query query=new Query(criteria);
		List<UserEntity> userEntity = find(query);
		return userEntity;
	}
	
	/**
	 * 根据玩家ID查询所有信息
	 */
	public List<UserEntity> findAllByPlayerId(Set<Integer> ranKingDataList) {
		Criteria criteria= Criteria. where("playerId").in(ranKingDataList);
		Query query=new Query(criteria);
		return find(query);
	}
	
	/**
	 * 根据玩家playerid查询所有信息
	 */
	public List<UserEntity> findAllByPlayerIds(List<Long> playerid){
		Criteria criteria= Criteria. where("playerId").in(playerid);
		Query query=new Query(criteria);	
		return find(query);
	}
	
	
	/**
	 * 根据手机号查询玩家所有信息
	 */
	public UserEntity findDataByPhone(String phone) {
		return super.findByID(phone);
	}
	
	/**
	 * 删除已绑定手机号
	 */
	public void gmDeletePhone(long playerId) {
		Criteria criteria= Criteria. where(PLAYERID).is(playerId);
		Query query = new Query(criteria);
		super.remove(query);
	}

}

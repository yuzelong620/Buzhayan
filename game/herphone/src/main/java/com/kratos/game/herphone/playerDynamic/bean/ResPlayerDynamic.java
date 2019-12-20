package com.kratos.game.herphone.playerDynamic.bean;

import java.util.ArrayList;
import java.util.List;

import com.kratos.game.herphone.player.domain.Player;
import com.kratos.game.herphone.playerDynamic.entity.PlayerDynamicEntity;

import lombok.Data;

@Data
public class ResPlayerDynamic {
	
	private String playerId;
	private String roleId;
	private String nickName;
	private String headImgUrl;	//头像
	private String sex;			//性别 0未知 1男 2女
	private String signature; //签名
	private String city;	//城市
	private int fansCount;		//粉丝数
	private int attentionCount;		//关注数
	private List<Integer> itemTitle = new ArrayList<Integer>(); //称号列表
	private int achievementDebris;		//成就碎片数
	private int clue;  //线索值
	private int AchievementTags; //显示成就徽章
	private int avatarFrame; //显示头像框
	private int currency; //眨眼星币
	private String exp;//人物经验
	private int level; // 人物等级
	private int state; //与玩家关注状态 0：互相未关注 1:我只关注他 2：他只关注我 3：互相关注
	
	public ResPlayerDynamic(PlayerDynamicEntity playerDynamicEntity) {
		this.playerId = playerDynamicEntity.getPlayerId()+"";
		this.roleId = playerDynamicEntity.getRoleId();
		this.nickName = playerDynamicEntity.getNickName();
		this.headImgUrl = playerDynamicEntity.getHeadImgUrl();
		this.sex = playerDynamicEntity.getSex();
		this.signature = playerDynamicEntity.getSignature();
		this.city = playerDynamicEntity.getCity();
		this.fansCount = playerDynamicEntity.getFansCount();
		this.attentionCount = playerDynamicEntity.getAttentionCount();
		this.itemTitle = playerDynamicEntity.getItemTitle();
		this.AchievementTags = playerDynamicEntity.getAchievementTags();
		this.avatarFrame = playerDynamicEntity.getAvatarFrame();
		this.achievementDebris = playerDynamicEntity.getAchievementDebris();
		this.clue = playerDynamicEntity.getClue();
		this.currency = playerDynamicEntity.getCurrency();
		this.exp = String.valueOf(playerDynamicEntity.getExp());
		this.level = playerDynamicEntity.getLevel();
	}

	public ResPlayerDynamic(PlayerDynamicEntity playerDynamicEntity,int state) {
		this.playerId = playerDynamicEntity.getPlayerId()+"";
		this.roleId = playerDynamicEntity.getRoleId();
		this.nickName = playerDynamicEntity.getNickName();
		this.headImgUrl = playerDynamicEntity.getHeadImgUrl();
		this.sex = playerDynamicEntity.getSex();
		this.signature = playerDynamicEntity.getSignature();
		this.city = playerDynamicEntity.getCity();
		this.fansCount = playerDynamicEntity.getFansCount();
		this.attentionCount = playerDynamicEntity.getAttentionCount();
		this.itemTitle = playerDynamicEntity.getItemTitle();
		this.AchievementTags = playerDynamicEntity.getAchievementTags();
		this.avatarFrame = playerDynamicEntity.getAvatarFrame();
		this.achievementDebris = playerDynamicEntity.getAchievementDebris();
		this.clue = playerDynamicEntity.getClue();
		this.currency = playerDynamicEntity.getCurrency();
		this.exp = String.valueOf(playerDynamicEntity.getExp());
		this.level = playerDynamicEntity.getLevel();
		this.state = state;
	}

	public ResPlayerDynamic() {
		super();
	}
	
	
	
}

package com.kratos.game.herphone.playerDynamic.entity;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.kratos.game.herphone.player.domain.Player;
import com.kratos.game.herphone.playerDynamic.bean.ReqPlayerDynamic;

import lombok.Data;

@Document
@Data
public class PlayerDynamicEntity {
	@Id
	private long playerId;
	@Indexed
	private String roleId; //角色id
	private String nickName;
	private String headImgUrl; //头像
	private String sex;	 //性别 1男 2女0外星人
	private String signature; //签名
	private String city; //城市
	private int fansCount; //粉丝数
	private int attentionCount; //关注数
	private int hotDiscussCount ;//热评数
	@Indexed
	private long setTitleTime;//设置护眼大队时间
	private List<Integer> itemTitle = new ArrayList<Integer>();//称号
    private int achievementTags; //显示成就徽章
    private int avatarFrame; //显示头像框
    int sendDynamicNum; //发表动态数量
    int replyDynamicNum;//回复动态数量
	int sendDiscussNum;//发表评论数量
	int toLikeNum;//点赞数量（点别人的评论，或者回复）
	int recommendBestNum; //推荐神评数量
	int clue;//线索值
	int level; //人物等级
	int achievementDebris;//成就碎片数
	int exp;//人物经验
	int currency; //眨眼星币
	int scenarioNum; //玩过剧本总数
	long goodFeelingNum;//获得好感度总数
	
	public PlayerDynamicEntity(Player player) {
		this.playerId = player.getId();
		this.roleId = player.getRoleId();
		this.sex = player.getGender();
		this.nickName = player.decodeName();
		if (player.getAvatarUrl()!=null) {
			this.headImgUrl = player.getAvatarUrl();
		}
		else {
			this.headImgUrl ="";
		}
		this.signature = "每个人都有属于自己的签名哦！";
		this.city = "";
	}
	
	public PlayerDynamicEntity() {
		super();
	}
	// 修改个人资料
	public void setReqPlayerDynamic(PlayerDynamicEntity playerDynamicEntity,ReqPlayerDynamic reqPlayerDynamic) {	
			playerDynamicEntity.setHeadImgUrl(reqPlayerDynamic.getHeadImgUrl());	
			playerDynamicEntity.setNickName(reqPlayerDynamic.getNickname());				
			playerDynamicEntity.setSex(reqPlayerDynamic.getSex());
			playerDynamicEntity.setSignature(reqPlayerDynamic.getSignature());	
			playerDynamicEntity.setCity(reqPlayerDynamic.getCity());
	}
	
}

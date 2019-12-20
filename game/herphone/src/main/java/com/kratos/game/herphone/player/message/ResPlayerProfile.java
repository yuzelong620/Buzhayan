package com.kratos.game.herphone.player.message;

import com.kratos.game.herphone.player.domain.Player;
import com.kratos.game.herphone.playerDynamic.entity.PlayerDynamicEntity;

import lombok.Data;


@Data
public class ResPlayerProfile {
    private String id;
    private String name;
    private String nickName;
    private String gender;
    private String avatarUrl;
    private Integer achievement;
    private Integer power;
    private Long lastRecoverPowerTime;
    private String wechatOpenid;
    private int weChatOrQQ;		//0未绑定 1绑定微信 2绑定qq
    private Integer costedPower;
    private Boolean takenCostPower20;
    private Boolean takenCostPower100;
    private Boolean takenShare;
    private Integer sharedCount;
    private int isGuest;
    private String releasedAchievements;
    private String roleId;
    private Integer watchAdTask; // 查看广告任务
    private Integer takenWatchAdTask; // 领取查看广告任务
    int extraPowerLimit;
    double extraRecoverRote;
    long noSpeak;	//禁言时间
    int isBlock;
    private int achievementTags = 0; //显示成就徽章
    private int avatarFrame = 0; //显示头像框
    
    public ResPlayerProfile() {
    }

    public ResPlayerProfile(Player player) {
        this.id = player.getId().toString();
        this.name = player.getName();
        this.nickName = player.decodeName();
        this.gender = player.getGender();
        this.avatarUrl = player.getAvatarUrl();
        this.achievement = player.getAchievement();
        this.power = player.getPower();
        this.lastRecoverPowerTime = player.getLastRecoverPowerTime();
        this.wechatOpenid = player.getWechatOpenid();
        this.costedPower = player.getCostedPower();
        this.takenCostPower20 = player.getTakenCostPower20();
        this.takenCostPower100 = player.getTakenCostPower100();
        this.takenShare = player.getTakenShare();
        this.sharedCount = player.getSharedCount();
        this.isGuest = player.getIsGuest();
        this.roleId = player.getRoleId();
        this.releasedAchievements = player.getReleasedAchievements();
        this.watchAdTask = player.getWatchAdTask();
        this.takenWatchAdTask = player.getTakenWatchAdTask();        
        this.extraPowerLimit=player.getExtraPowerLimit();
        this.extraRecoverRote=player.getExtraRecoverRote();
        this.noSpeak = player.getNoSpeakTime();
        this.isBlock = player.getIsBlock();
        if (player.getWechatOpenid() != null) {
        	this.weChatOrQQ = 1;
		}
        if (player.getTencentOpenid() != null) {
        	this.weChatOrQQ = 2;
		}
        
    }
    
    public ResPlayerProfile(Player player,PlayerDynamicEntity playerDynamicEntity) {
        this.id = player.getId().toString();
        this.name = player.getName();
        this.nickName = player.decodeName();
        this.gender = player.getGender();
        this.avatarUrl = player.getAvatarUrl();
        this.achievement = player.getAchievement();
        this.power = player.getPower();
        this.lastRecoverPowerTime = player.getLastRecoverPowerTime();
        this.wechatOpenid = player.getWechatOpenid();
        this.costedPower = player.getCostedPower();
        this.takenCostPower20 = player.getTakenCostPower20();
        this.takenCostPower100 = player.getTakenCostPower100();
        this.takenShare = player.getTakenShare();
        this.sharedCount = player.getSharedCount();
        this.isGuest = player.getIsGuest();
        this.roleId = player.getRoleId();
        this.releasedAchievements = player.getReleasedAchievements();
        this.watchAdTask = player.getWatchAdTask();
        this.takenWatchAdTask = player.getTakenWatchAdTask();        
        this.extraPowerLimit=player.getExtraPowerLimit();
        this.extraRecoverRote=player.getExtraRecoverRote();
        this.noSpeak = player.getNoSpeakTime();
        this.isBlock = player.getIsBlock();
        if(playerDynamicEntity != null){
        	this.achievementTags = playerDynamicEntity.getAchievementTags();
            this.avatarFrame = playerDynamicEntity.getAvatarFrame();
        }
        if (player.getWechatOpenid() != null) {
        	this.weChatOrQQ = 1;
		}
        if (player.getTencentOpenid() != null) {
        	this.weChatOrQQ = 2;
		}
        
    }
    
}

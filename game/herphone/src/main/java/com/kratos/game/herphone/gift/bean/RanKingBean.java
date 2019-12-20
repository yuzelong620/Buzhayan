package com.kratos.game.herphone.gift.bean;

public class RanKingBean {

	String sendGiftPlayerId; //送礼物的玩家ID
	String authorId;//作者ID
	long goodFeeling; //对这个作者的好感度总数
	String nickName;
	String headImgUrl; //头像
	String sex;	 //性别 1男 2女0外星人
	int achievementTags; //显示成就徽章
    int avatarFrame; //显示头像框
	
	public RanKingBean() {
		super();
	}
	public RanKingBean(String sendGiftPlayerId, String authorId, long goodFeeling, String nickName, String headImgUrl,
			String sex,int achievementTags,int avatarFrame) {
		super();
		this.sendGiftPlayerId = sendGiftPlayerId;
		this.authorId = authorId;
		this.goodFeeling = goodFeeling;
		this.nickName = nickName;
		this.headImgUrl = headImgUrl;
		this.sex = sex;
		this.achievementTags = achievementTags;
		this.avatarFrame = avatarFrame;
	}
	public String getSendGiftPlayerId() {
		return sendGiftPlayerId;
	}
	public void setSendGiftPlayerId(String sendGiftPlayerId) {
		this.sendGiftPlayerId = sendGiftPlayerId;
	}
	public String getAuthorId() {
		return authorId;
	}
	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}
	public long getGoodFeeling() {
		return goodFeeling;
	}
	public void setGoodFeeling(long goodFeeling) {
		this.goodFeeling = goodFeeling;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getHeadImgUrl() {
		return headImgUrl;
	}
	public void setHeadImgUrl(String headImgUrl) {
		this.headImgUrl = headImgUrl;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public int getAchievementTags() {
		return achievementTags;
	}
	public void setAchievementTags(int achievementTags) {
		this.achievementTags = achievementTags;
	}
	public int getAvatarFrame() {
		return avatarFrame;
	}
	public void setAvatarFrame(int avatarFrame) {
		this.avatarFrame = avatarFrame;
	}
	
}

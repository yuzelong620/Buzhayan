package com.globalgame.auto.json;
import java.util.List;
import com.mind.core.util.StringIntTuple;
import com.mind.core.util.IntDoubleTuple;
import com.mind.core.util.IntTuple;
import com.mind.core.util.ThreeTuple;
import com.mind.core.util.StringFloatTuple;

/**
*自动生成类
*/
public class GameParams_Json{
	/** 旧货市场物品出现概率::*/
	private List<IntDoubleTuple>	normalGoodsProbability;
	/** id::*/
	private Integer	id;
	/** ios审核免验证手机号::*/
	private List<String>	ios_assessor_phone;
	/** 评论分页的条数::*/
	private Integer	discussPageCount;
	/**  经典评论 取前几名::*/
	private Integer	topLimit;
	/** 经典评论 点赞数量限制::*/
	private Integer	topLimitPraiseNum;
	/** 护眼大队粉丝数量条件::*/
	private Integer	titleFansCount;
	/** 护眼大队热评数量条件::*/
	private Integer	titleHotDiscussCount;
	/** 护眼大队完成游戏数量条件（小时）::*/
	private Integer	titleGameCount;
	/** 护眼大队在线时间条件::*/
	private Integer	titleOnline;
	/** 评论的字数限制::*/
	private Integer	discussContentLimit;
	/** 聊天字数限制::*/
	private Integer	messageContentLimit;
	/** 聊天分页条数::*/
	private Integer	messagePageCount;
	/** 护眼大队称号id::*/
	private Integer	protectEyesTitleId;
	/** 黑名单人数上限::*/
	private Integer	blackListMax;
	/** 广场动态 分页显示条数::*/
	private Integer	dynamicPageCount;
	/** 广场动态 文字长度限制::*/
	private Integer	dynamicTextLimit;
	/** 广场 回复文字长度限制::*/
	private Integer	dynamicReplyTextLimit;
	/** 护眼先锋称号id::*/
	private Integer	pioneerId;
	/** 白名单列表::*/
	private List<String>	whitelistId;
	/** 老用户默认注册时间::*/
	private String	defaultRegisteredTime;
	/** 内测用户奖励物品id::*/
	private Integer	betaUserRewardId;
	/** 老用户奖励标签id::*/
	private Integer	rewardTagId;

	/** 旧货市场物品出现概率::*/
	public List<IntDoubleTuple> getNormalGoodsProbability(){
		return this.normalGoodsProbability;
	}
	/** id::*/
	public Integer getId(){
		return this.id;
	}
	/** ios审核免验证手机号::*/
	public List<String> getIos_assessor_phone(){
		return this.ios_assessor_phone;
	}
	/** 评论分页的条数::*/
	public Integer getDiscussPageCount(){
		return this.discussPageCount;
	}
	/**  经典评论 取前几名::*/
	public Integer getTopLimit(){
		return this.topLimit;
	}
	/** 经典评论 点赞数量限制::*/
	public Integer getTopLimitPraiseNum(){
		return this.topLimitPraiseNum;
	}
	/** 护眼大队粉丝数量条件::*/
	public Integer getTitleFansCount(){
		return this.titleFansCount;
	}
	/** 护眼大队热评数量条件::*/
	public Integer getTitleHotDiscussCount(){
		return this.titleHotDiscussCount;
	}
	/** 护眼大队完成游戏数量条件（小时）::*/
	public Integer getTitleGameCount(){
		return this.titleGameCount;
	}
	/** 护眼大队在线时间条件::*/
	public Integer getTitleOnline(){
		return this.titleOnline;
	}
	/** 评论的字数限制::*/
	public Integer getDiscussContentLimit(){
		return this.discussContentLimit;
	}
	/** 聊天字数限制::*/
	public Integer getMessageContentLimit(){
		return this.messageContentLimit;
	}
	/** 聊天分页条数::*/
	public Integer getMessagePageCount(){
		return this.messagePageCount;
	}
	/** 护眼大队称号id::*/
	public Integer getProtectEyesTitleId(){
		return this.protectEyesTitleId;
	}
	/** 黑名单人数上限::*/
	public Integer getBlackListMax(){
		return this.blackListMax;
	}
	/** 广场动态 分页显示条数::*/
	public Integer getDynamicPageCount(){
		return this.dynamicPageCount;
	}
	/** 广场动态 文字长度限制::*/
	public Integer getDynamicTextLimit(){
		return this.dynamicTextLimit;
	}
	/** 广场 回复文字长度限制::*/
	public Integer getDynamicReplyTextLimit(){
		return this.dynamicReplyTextLimit;
	}
	/** 护眼先锋称号id::*/
	public Integer getPioneerId(){
		return this.pioneerId;
	}
	/** 白名单列表::*/
	public List<String> getWhitelistId(){
		return this.whitelistId;
	}
	/** 老用户默认注册时间::*/
	public String getDefaultRegisteredTime(){
		return this.defaultRegisteredTime;
	}
	/** 内测用户奖励物品id::*/
	public Integer getBetaUserRewardId(){
		return this.betaUserRewardId;
	}
	/** 老用户奖励标签id::*/
	public Integer getRewardTagId(){
		return this.rewardTagId;
	}
	/**旧货市场物品出现概率::*/
	public void setNormalGoodsProbability(List<IntDoubleTuple> normalGoodsProbability){
		this.normalGoodsProbability = normalGoodsProbability;
	}
	/**id::*/
	public void setId(Integer id){
		this.id = id;
	}
	/**ios审核免验证手机号::*/
	public void setIos_assessor_phone(List<String> ios_assessor_phone){
		this.ios_assessor_phone = ios_assessor_phone;
	}
	/**评论分页的条数::*/
	public void setDiscussPageCount(Integer discussPageCount){
		this.discussPageCount = discussPageCount;
	}
	/** 经典评论 取前几名::*/
	public void setTopLimit(Integer topLimit){
		this.topLimit = topLimit;
	}
	/**经典评论 点赞数量限制::*/
	public void setTopLimitPraiseNum(Integer topLimitPraiseNum){
		this.topLimitPraiseNum = topLimitPraiseNum;
	}
	/**护眼大队粉丝数量条件::*/
	public void setTitleFansCount(Integer titleFansCount){
		this.titleFansCount = titleFansCount;
	}
	/**护眼大队热评数量条件::*/
	public void setTitleHotDiscussCount(Integer titleHotDiscussCount){
		this.titleHotDiscussCount = titleHotDiscussCount;
	}
	/**护眼大队完成游戏数量条件（小时）::*/
	public void setTitleGameCount(Integer titleGameCount){
		this.titleGameCount = titleGameCount;
	}
	/**护眼大队在线时间条件::*/
	public void setTitleOnline(Integer titleOnline){
		this.titleOnline = titleOnline;
	}
	/**评论的字数限制::*/
	public void setDiscussContentLimit(Integer discussContentLimit){
		this.discussContentLimit = discussContentLimit;
	}
	/**聊天字数限制::*/
	public void setMessageContentLimit(Integer messageContentLimit){
		this.messageContentLimit = messageContentLimit;
	}
	/**聊天分页条数::*/
	public void setMessagePageCount(Integer messagePageCount){
		this.messagePageCount = messagePageCount;
	}
	/**护眼大队称号id::*/
	public void setProtectEyesTitleId(Integer protectEyesTitleId){
		this.protectEyesTitleId = protectEyesTitleId;
	}
	/**黑名单人数上限::*/
	public void setBlackListMax(Integer blackListMax){
		this.blackListMax = blackListMax;
	}
	/**广场动态 分页显示条数::*/
	public void setDynamicPageCount(Integer dynamicPageCount){
		this.dynamicPageCount = dynamicPageCount;
	}
	/**广场动态 文字长度限制::*/
	public void setDynamicTextLimit(Integer dynamicTextLimit){
		this.dynamicTextLimit = dynamicTextLimit;
	}
	/**广场 回复文字长度限制::*/
	public void setDynamicReplyTextLimit(Integer dynamicReplyTextLimit){
		this.dynamicReplyTextLimit = dynamicReplyTextLimit;
	}
	/**护眼先锋称号id::*/
	public void setPioneerId(Integer pioneerId){
		this.pioneerId = pioneerId;
	}
	/**白名单列表::*/
	public void setWhitelistId(List<String> whitelistId){
		this.whitelistId = whitelistId;
	}
	/**老用户默认注册时间::*/
	public void setDefaultRegisteredTime(String defaultRegisteredTime){
		this.defaultRegisteredTime = defaultRegisteredTime;
	}
	/**内测用户奖励物品id::*/
	public void setBetaUserRewardId(Integer betaUserRewardId){
		this.betaUserRewardId = betaUserRewardId;
	}
	/**老用户奖励标签id::*/
	public void setRewardTagId(Integer rewardTagId){
		this.rewardTagId = rewardTagId;
	}
}
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
public class Achievement_Json{
	/** 编号::*/
	private Integer	id;
	/** 成就名称::*/
	private String	title;
	/** 成就描述::*/
	private String	desc;
	/** 成就id::*/
	private Integer	achievementId;
	/** gameid::*/
	private Integer	gameid;
	/** chatid::*/
	private Integer	chatid;
	/** cid::*/
	private Integer	cid;
	/** 成就点数::*/
	private Integer	achievement;
	/** 是否获得::*/
	private Integer	achieved;
	/** param::*/
	private String	param;

	/** 编号::*/
	public Integer getId(){
		return this.id;
	}
	/** 成就名称::*/
	public String getTitle(){
		return this.title;
	}
	/** 成就描述::*/
	public String getDesc(){
		return this.desc;
	}
	/** 成就id::*/
	public Integer getAchievementId(){
		return this.achievementId;
	}
	/** gameid::*/
	public Integer getGameid(){
		return this.gameid;
	}
	/** chatid::*/
	public Integer getChatid(){
		return this.chatid;
	}
	/** cid::*/
	public Integer getCid(){
		return this.cid;
	}
	/** 成就点数::*/
	public Integer getAchievement(){
		return this.achievement;
	}
	/** 是否获得::*/
	public Integer getAchieved(){
		return this.achieved;
	}
	/** param::*/
	public String getParam(){
		return this.param;
	}
	/**编号::*/
	public void setId(Integer id){
		this.id = id;
	}
	/**成就名称::*/
	public void setTitle(String title){
		this.title = title;
	}
	/**成就描述::*/
	public void setDesc(String desc){
		this.desc = desc;
	}
	/**成就id::*/
	public void setAchievementId(Integer achievementId){
		this.achievementId = achievementId;
	}
	/**gameid::*/
	public void setGameid(Integer gameid){
		this.gameid = gameid;
	}
	/**chatid::*/
	public void setChatid(Integer chatid){
		this.chatid = chatid;
	}
	/**cid::*/
	public void setCid(Integer cid){
		this.cid = cid;
	}
	/**成就点数::*/
	public void setAchievement(Integer achievement){
		this.achievement = achievement;
	}
	/**是否获得::*/
	public void setAchieved(Integer achieved){
		this.achieved = achieved;
	}
	/**param::*/
	public void setParam(String param){
		this.param = param;
	}
}
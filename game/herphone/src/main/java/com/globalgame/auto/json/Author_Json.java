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
public class Author_Json{
	/** 玩家id::*/
	private Integer	id;
	/** 角色id::*/
	private String	roleId;
	/** 作者名字::*/
	private String	nickName;
	/** 探索值::*/
	private Integer	exploration;
	/** 成就值::*/
	private Integer	achievement;
	/** 头像::*/
	private String	avatarUrl;
	/** 性别::*/
	private String	gender;
	/** 作者简介::*/
	private String	authorIntroduction;

	/** 玩家id::*/
	public Integer getId(){
		return this.id;
	}
	/** 角色id::*/
	public String getRoleId(){
		return this.roleId;
	}
	/** 作者名字::*/
	public String getNickName(){
		return this.nickName;
	}
	/** 探索值::*/
	public Integer getExploration(){
		return this.exploration;
	}
	/** 成就值::*/
	public Integer getAchievement(){
		return this.achievement;
	}
	/** 头像::*/
	public String getAvatarUrl(){
		return this.avatarUrl;
	}
	/** 性别::*/
	public String getGender(){
		return this.gender;
	}
	/** 作者简介::*/
	public String getAuthorIntroduction(){
		return this.authorIntroduction;
	}
	/**玩家id::*/
	public void setId(Integer id){
		this.id = id;
	}
	/**角色id::*/
	public void setRoleId(String roleId){
		this.roleId = roleId;
	}
	/**作者名字::*/
	public void setNickName(String nickName){
		this.nickName = nickName;
	}
	/**探索值::*/
	public void setExploration(Integer exploration){
		this.exploration = exploration;
	}
	/**成就值::*/
	public void setAchievement(Integer achievement){
		this.achievement = achievement;
	}
	/**头像::*/
	public void setAvatarUrl(String avatarUrl){
		this.avatarUrl = avatarUrl;
	}
	/**性别::*/
	public void setGender(String gender){
		this.gender = gender;
	}
	/**作者简介::*/
	public void setAuthorIntroduction(String authorIntroduction){
		this.authorIntroduction = authorIntroduction;
	}
}
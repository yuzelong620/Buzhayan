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
public class AchBadge_Json{
	/** 编号::*/
	private Integer	id;
	/** 小标签名称::*/
	private String	littlename;
	/** 属于成就徽章id::*/
	private Integer	fromTagId;
	/** 徽章图片::*/
	private String	achievementimg;

	/** 编号::*/
	public Integer getId(){
		return this.id;
	}
	/** 小标签名称::*/
	public String getLittlename(){
		return this.littlename;
	}
	/** 属于成就徽章id::*/
	public Integer getFromTagId(){
		return this.fromTagId;
	}
	/** 徽章图片::*/
	public String getAchievementimg(){
		return this.achievementimg;
	}
	/**编号::*/
	public void setId(Integer id){
		this.id = id;
	}
	/**小标签名称::*/
	public void setLittlename(String littlename){
		this.littlename = littlename;
	}
	/**属于成就徽章id::*/
	public void setFromTagId(Integer fromTagId){
		this.fromTagId = fromTagId;
	}
	/**徽章图片::*/
	public void setAchievementimg(String achievementimg){
		this.achievementimg = achievementimg;
	}
}
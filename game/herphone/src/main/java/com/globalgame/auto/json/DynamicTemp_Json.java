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
public class DynamicTemp_Json{
	/** 编号::*/
	private Integer	id;
	/** 标签描述::*/
	private String	tagDesc;
	/** 标签id::*/
	private List<Integer>	tags;
	/** 资源json::*/
	private String	resJson;
	/** 点赞数量::*/
	private Integer	praiseNum;
	/** 机器人id::*/
	private Integer	playerId;

	/** 编号::*/
	public Integer getId(){
		return this.id;
	}
	/** 标签描述::*/
	public String getTagDesc(){
		return this.tagDesc;
	}
	/** 标签id::*/
	public List<Integer> getTags(){
		return this.tags;
	}
	/** 资源json::*/
	public String getResJson(){
		return this.resJson;
	}
	/** 点赞数量::*/
	public Integer getPraiseNum(){
		return this.praiseNum;
	}
	/** 机器人id::*/
	public Integer getPlayerId(){
		return this.playerId;
	}
	/**编号::*/
	public void setId(Integer id){
		this.id = id;
	}
	/**标签描述::*/
	public void setTagDesc(String tagDesc){
		this.tagDesc = tagDesc;
	}
	/**标签id::*/
	public void setTags(List<Integer> tags){
		this.tags = tags;
	}
	/**资源json::*/
	public void setResJson(String resJson){
		this.resJson = resJson;
	}
	/**点赞数量::*/
	public void setPraiseNum(Integer praiseNum){
		this.praiseNum = praiseNum;
	}
	/**机器人id::*/
	public void setPlayerId(Integer playerId){
		this.playerId = playerId;
	}
}
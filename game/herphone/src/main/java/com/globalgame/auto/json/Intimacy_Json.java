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
public class Intimacy_Json{
	/** 编号::*/
	private Integer	id;
	/** 剧本名称::*/
	private String	name;
	/** 剧本id::*/
	private Integer	gameid;
	/** chatid::*/
	private Integer	chatid;
	/** cid::*/
	private Integer	cid;
	/** 好感度::*/
	private Integer	value;
	/** 人物id::*/
	private Integer	avatarid;
	/** 组号::*/
	private Integer	number;

	/** 编号::*/
	public Integer getId(){
		return this.id;
	}
	/** 剧本名称::*/
	public String getName(){
		return this.name;
	}
	/** 剧本id::*/
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
	/** 好感度::*/
	public Integer getValue(){
		return this.value;
	}
	/** 人物id::*/
	public Integer getAvatarid(){
		return this.avatarid;
	}
	/** 组号::*/
	public Integer getNumber(){
		return this.number;
	}
	/**编号::*/
	public void setId(Integer id){
		this.id = id;
	}
	/**剧本名称::*/
	public void setName(String name){
		this.name = name;
	}
	/**剧本id::*/
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
	/**好感度::*/
	public void setValue(Integer value){
		this.value = value;
	}
	/**人物id::*/
	public void setAvatarid(Integer avatarid){
		this.avatarid = avatarid;
	}
	/**组号::*/
	public void setNumber(Integer number){
		this.number = number;
	}
}
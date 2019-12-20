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
public class Clue_Json{
	/** 编号::*/
	private Integer	id;
	/** 描述::*/
	private String	desc;
	/** gameid::*/
	private Integer	gameid;
	/** chatid::*/
	private Integer	chatid;
	/** cid::*/
	private Integer	cid;
	/** 增加的线索值::*/
	private Integer	value;

	/** 编号::*/
	public Integer getId(){
		return this.id;
	}
	/** 描述::*/
	public String getDesc(){
		return this.desc;
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
	/** 增加的线索值::*/
	public Integer getValue(){
		return this.value;
	}
	/**编号::*/
	public void setId(Integer id){
		this.id = id;
	}
	/**描述::*/
	public void setDesc(String desc){
		this.desc = desc;
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
	/**增加的线索值::*/
	public void setValue(Integer value){
		this.value = value;
	}
}
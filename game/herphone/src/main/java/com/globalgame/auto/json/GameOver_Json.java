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
public class GameOver_Json{
	/** 编号::*/
	private Integer	id;
	/** 名字::*/
	private String	name;
	/** 值::*/
	private List<IntTuple>	value;
	/** 物品类型::*/
	private Integer	type;

	/** 编号::*/
	public Integer getId(){
		return this.id;
	}
	/** 名字::*/
	public String getName(){
		return this.name;
	}
	/** 值::*/
	public List<IntTuple> getValue(){
		return this.value;
	}
	/** 物品类型::*/
	public Integer getType(){
		return this.type;
	}
	/**编号::*/
	public void setId(Integer id){
		this.id = id;
	}
	/**名字::*/
	public void setName(String name){
		this.name = name;
	}
	/**值::*/
	public void setValue(List<IntTuple> value){
		this.value = value;
	}
	/**物品类型::*/
	public void setType(Integer type){
		this.type = type;
	}
}
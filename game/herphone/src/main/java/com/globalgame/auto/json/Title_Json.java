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
public class Title_Json{
	/** 编号::*/
	private Integer	id;
	/** 名字::*/
	private String	name;
	/** 称号类别::*/
	private Integer	type;
	/** 称号加成值::*/
	private Double	value;

	/** 编号::*/
	public Integer getId(){
		return this.id;
	}
	/** 名字::*/
	public String getName(){
		return this.name;
	}
	/** 称号类别::*/
	public Integer getType(){
		return this.type;
	}
	/** 称号加成值::*/
	public Double getValue(){
		return this.value;
	}
	/**编号::*/
	public void setId(Integer id){
		this.id = id;
	}
	/**名字::*/
	public void setName(String name){
		this.name = name;
	}
	/**称号类别::*/
	public void setType(Integer type){
		this.type = type;
	}
	/**称号加成值::*/
	public void setValue(Double value){
		this.value = value;
	}
}
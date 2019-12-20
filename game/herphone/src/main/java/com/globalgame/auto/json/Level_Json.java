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
public class Level_Json{
	/** 序号::*/
	private Integer	id;
	/** 等级::*/
	private Integer	level;
	/** 升级所需经验::*/
	private Integer	exp;

	/** 序号::*/
	public Integer getId(){
		return this.id;
	}
	/** 等级::*/
	public Integer getLevel(){
		return this.level;
	}
	/** 升级所需经验::*/
	public Integer getExp(){
		return this.exp;
	}
	/**序号::*/
	public void setId(Integer id){
		this.id = id;
	}
	/**等级::*/
	public void setLevel(Integer level){
		this.level = level;
	}
	/**升级所需经验::*/
	public void setExp(Integer exp){
		this.exp = exp;
	}
}
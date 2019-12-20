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
public class Item_Json{
	/** 编号::*/
	private Integer	id;
	/** 名字::*/
	private String	name;
	/** 物品类型（附加效果类型）::*/
	private Integer	type;
	/** 值（物品效果附加值）::*/
	private Double	value;
	/** 最大叠加数量::*/
	private Integer	max;
	/** 图片地址::*/
	private String	pic;
	/** 未获得的徽章图片::*/
	private String	no;

	/** 编号::*/
	public Integer getId(){
		return this.id;
	}
	/** 名字::*/
	public String getName(){
		return this.name;
	}
	/** 物品类型（附加效果类型）::*/
	public Integer getType(){
		return this.type;
	}
	/** 值（物品效果附加值）::*/
	public Double getValue(){
		return this.value;
	}
	/** 最大叠加数量::*/
	public Integer getMax(){
		return this.max;
	}
	/** 图片地址::*/
	public String getPic(){
		return this.pic;
	}
	/** 未获得的徽章图片::*/
	public String getNo(){
		return this.no;
	}
	/**编号::*/
	public void setId(Integer id){
		this.id = id;
	}
	/**名字::*/
	public void setName(String name){
		this.name = name;
	}
	/**物品类型（附加效果类型）::*/
	public void setType(Integer type){
		this.type = type;
	}
	/**值（物品效果附加值）::*/
	public void setValue(Double value){
		this.value = value;
	}
	/**最大叠加数量::*/
	public void setMax(Integer max){
		this.max = max;
	}
	/**图片地址::*/
	public void setPic(String pic){
		this.pic = pic;
	}
	/**未获得的徽章图片::*/
	public void setNo(String no){
		this.no = no;
	}
}
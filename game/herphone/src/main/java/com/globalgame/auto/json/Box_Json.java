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
public class Box_Json{
	/** 奖品Id::*/
	private Integer	id;
	/** 奖品名称::*/
	private String	prize;
	/** 宝箱类型::*/
	private Integer	boxKind;
	/** 概率::*/
	private Integer	odds;
	/** 物品Id::*/
	private List<StringIntTuple>	itemId;
	/** 物品类型::*/
	private Integer	type;

	/** 奖品Id::*/
	public Integer getId(){
		return this.id;
	}
	/** 奖品名称::*/
	public String getPrize(){
		return this.prize;
	}
	/** 宝箱类型::*/
	public Integer getBoxKind(){
		return this.boxKind;
	}
	/** 概率::*/
	public Integer getOdds(){
		return this.odds;
	}
	/** 物品Id::*/
	public List<StringIntTuple> getItemId(){
		return this.itemId;
	}
	/** 物品类型::*/
	public Integer getType(){
		return this.type;
	}
	/**奖品Id::*/
	public void setId(Integer id){
		this.id = id;
	}
	/**奖品名称::*/
	public void setPrize(String prize){
		this.prize = prize;
	}
	/**宝箱类型::*/
	public void setBoxKind(Integer boxKind){
		this.boxKind = boxKind;
	}
	/**概率::*/
	public void setOdds(Integer odds){
		this.odds = odds;
	}
	/**物品Id::*/
	public void setItemId(List<StringIntTuple> itemId){
		this.itemId = itemId;
	}
	/**物品类型::*/
	public void setType(Integer type){
		this.type = type;
	}
}
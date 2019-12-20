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
public class CostCurrency_Json{
	/** 商品id::*/
	private Integer	id;
	/** 商品消耗货币::*/
	private Integer	costNum;
	/** 商品名称::*/
	private String	shopName;
	/** 商品介绍::*/
	private String	commodityIntroduction;

	/** 商品id::*/
	public Integer getId(){
		return this.id;
	}
	/** 商品消耗货币::*/
	public Integer getCostNum(){
		return this.costNum;
	}
	/** 商品名称::*/
	public String getShopName(){
		return this.shopName;
	}
	/** 商品介绍::*/
	public String getCommodityIntroduction(){
		return this.commodityIntroduction;
	}
	/**商品id::*/
	public void setId(Integer id){
		this.id = id;
	}
	/**商品消耗货币::*/
	public void setCostNum(Integer costNum){
		this.costNum = costNum;
	}
	/**商品名称::*/
	public void setShopName(String shopName){
		this.shopName = shopName;
	}
	/**商品介绍::*/
	public void setCommodityIntroduction(String commodityIntroduction){
		this.commodityIntroduction = commodityIntroduction;
	}
}
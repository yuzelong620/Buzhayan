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
public class SignInReward_Json{
	/** 编号::*/
	private Integer	id;
	/** 签到天数::*/
	private Integer	day;
	/** 奖励列表::*/
	private List<IntTuple>	list;
	/** 图片路径::*/
	private String	url;

	/** 编号::*/
	public Integer getId(){
		return this.id;
	}
	/** 签到天数::*/
	public Integer getDay(){
		return this.day;
	}
	/** 奖励列表::*/
	public List<IntTuple> getList(){
		return this.list;
	}
	/** 图片路径::*/
	public String getUrl(){
		return this.url;
	}
	/**编号::*/
	public void setId(Integer id){
		this.id = id;
	}
	/**签到天数::*/
	public void setDay(Integer day){
		this.day = day;
	}
	/**奖励列表::*/
	public void setList(List<IntTuple> list){
		this.list = list;
	}
	/**图片路径::*/
	public void setUrl(String url){
		this.url = url;
	}
}
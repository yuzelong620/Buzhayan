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
public class Tag_Json{
	/** 编号::*/
	private Integer	id;
	/** 成就徽章::*/
	private String	name;
	/** 小徽章id::*/
	private List<Integer>	littlename;
	/** 简介::*/
	private String	introduce;
	/** 徽章头像::*/
	private String	head;
	/** 奖励列表::*/
	private List<Integer>	list;

	/** 编号::*/
	public Integer getId(){
		return this.id;
	}
	/** 成就徽章::*/
	public String getName(){
		return this.name;
	}
	/** 小徽章id::*/
	public List<Integer> getLittlename(){
		return this.littlename;
	}
	/** 简介::*/
	public String getIntroduce(){
		return this.introduce;
	}
	/** 徽章头像::*/
	public String getHead(){
		return this.head;
	}
	/** 奖励列表::*/
	public List<Integer> getList(){
		return this.list;
	}
	/**编号::*/
	public void setId(Integer id){
		this.id = id;
	}
	/**成就徽章::*/
	public void setName(String name){
		this.name = name;
	}
	/**小徽章id::*/
	public void setLittlename(List<Integer> littlename){
		this.littlename = littlename;
	}
	/**简介::*/
	public void setIntroduce(String introduce){
		this.introduce = introduce;
	}
	/**徽章头像::*/
	public void setHead(String head){
		this.head = head;
	}
	/**奖励列表::*/
	public void setList(List<Integer> list){
		this.list = list;
	}
}
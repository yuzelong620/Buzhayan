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
public class Admin_Json{
	/** 用户名::*/
	private String	userName;
	/** 密码::*/
	private String	passWord;
	/** 序号::*/
	private Integer	id;

	/** 用户名::*/
	public String getUserName(){
		return this.userName;
	}
	/** 密码::*/
	public String getPassWord(){
		return this.passWord;
	}
	/** 序号::*/
	public Integer getId(){
		return this.id;
	}
	/**用户名::*/
	public void setUserName(String userName){
		this.userName = userName;
	}
	/**密码::*/
	public void setPassWord(String passWord){
		this.passWord = passWord;
	}
	/**序号::*/
	public void setId(Integer id){
		this.id = id;
	}
}
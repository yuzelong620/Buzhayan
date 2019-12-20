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
public class VideoReward_Json{
	/** 编号::*/
	private Integer	id;
	/** 第几天::*/
	private Integer	day;
	/** 视频地址::*/
	private String	videoUrl;
	/** 奖励值::*/
	private Integer	power;
	/** 图片::*/
	private String	pictrue;

	/** 编号::*/
	public Integer getId(){
		return this.id;
	}
	/** 第几天::*/
	public Integer getDay(){
		return this.day;
	}
	/** 视频地址::*/
	public String getVideoUrl(){
		return this.videoUrl;
	}
	/** 奖励值::*/
	public Integer getPower(){
		return this.power;
	}
	/** 图片::*/
	public String getPictrue(){
		return this.pictrue;
	}
	/**编号::*/
	public void setId(Integer id){
		this.id = id;
	}
	/**第几天::*/
	public void setDay(Integer day){
		this.day = day;
	}
	/**视频地址::*/
	public void setVideoUrl(String videoUrl){
		this.videoUrl = videoUrl;
	}
	/**奖励值::*/
	public void setPower(Integer power){
		this.power = power;
	}
	/**图片::*/
	public void setPictrue(String pictrue){
		this.pictrue = pictrue;
	}
}
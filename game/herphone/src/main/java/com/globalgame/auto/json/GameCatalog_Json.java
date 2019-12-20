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
public class GameCatalog_Json{
	/** 编号::*/
	private Integer	id;
	/** 剧本名称::*/
	private String	name;
	/** 封面::*/
	private String	cover;
	/** 是否锁定::*/
	private Integer	locked;
	/** 选项数量::*/
	private Integer	selectionCount;
	/** 新品::*/
	private Integer	isNew;
	/** 版本号::*/
	private Integer	version;
	/** 作者名称::*/
	private String	authorName;
	/** 发布时间::*/
	private String	releaseTime;
	/** 简介::*/
	private String	storyDetails;
	/** 类型::*/
	private String	type;
	/** 作者id::*/
	private Integer	authorid;
	/** 是否连载::*/
	private Integer	toid;
	/** 是否显示::*/
	private Integer	isShow;
	/** 是否完结::*/
	private Integer	finish;
	/** 是否视频本::*/
	private Boolean	videotype;
	/** 作者简介::*/
	private String	authorIntroduction;

	/** 编号::*/
	public Integer getId(){
		return this.id;
	}
	/** 剧本名称::*/
	public String getName(){
		return this.name;
	}
	/** 封面::*/
	public String getCover(){
		return this.cover;
	}
	/** 是否锁定::*/
	public Integer getLocked(){
		return this.locked;
	}
	/** 选项数量::*/
	public Integer getSelectionCount(){
		return this.selectionCount;
	}
	/** 新品::*/
	public Integer getIsNew(){
		return this.isNew;
	}
	/** 版本号::*/
	public Integer getVersion(){
		return this.version;
	}
	/** 作者名称::*/
	public String getAuthorName(){
		return this.authorName;
	}
	/** 发布时间::*/
	public String getReleaseTime(){
		return this.releaseTime;
	}
	/** 简介::*/
	public String getStoryDetails(){
		return this.storyDetails;
	}
	/** 类型::*/
	public String getType(){
		return this.type;
	}
	/** 作者id::*/
	public Integer getAuthorid(){
		return this.authorid;
	}
	/** 是否连载::*/
	public Integer getToid(){
		return this.toid;
	}
	/** 是否显示::*/
	public Integer getIsShow(){
		return this.isShow;
	}
	/** 是否完结::*/
	public Integer getFinish(){
		return this.finish;
	}
	/** 是否视频本::*/
	public Boolean getVideotype(){
		return this.videotype;
	}
	/** 作者简介::*/
	public String getAuthorIntroduction(){
		return this.authorIntroduction;
	}
	/**编号::*/
	public void setId(Integer id){
		this.id = id;
	}
	/**剧本名称::*/
	public void setName(String name){
		this.name = name;
	}
	/**封面::*/
	public void setCover(String cover){
		this.cover = cover;
	}
	/**是否锁定::*/
	public void setLocked(Integer locked){
		this.locked = locked;
	}
	/**选项数量::*/
	public void setSelectionCount(Integer selectionCount){
		this.selectionCount = selectionCount;
	}
	/**新品::*/
	public void setIsNew(Integer isNew){
		this.isNew = isNew;
	}
	/**版本号::*/
	public void setVersion(Integer version){
		this.version = version;
	}
	/**作者名称::*/
	public void setAuthorName(String authorName){
		this.authorName = authorName;
	}
	/**发布时间::*/
	public void setReleaseTime(String releaseTime){
		this.releaseTime = releaseTime;
	}
	/**简介::*/
	public void setStoryDetails(String storyDetails){
		this.storyDetails = storyDetails;
	}
	/**类型::*/
	public void setType(String type){
		this.type = type;
	}
	/**作者id::*/
	public void setAuthorid(Integer authorid){
		this.authorid = authorid;
	}
	/**是否连载::*/
	public void setToid(Integer toid){
		this.toid = toid;
	}
	/**是否显示::*/
	public void setIsShow(Integer isShow){
		this.isShow = isShow;
	}
	/**是否完结::*/
	public void setFinish(Integer finish){
		this.finish = finish;
	}
	/**是否视频本::*/
	public void setVideotype(Boolean videotype){
		this.videotype = videotype;
	}
	/**作者简介::*/
	public void setAuthorIntroduction(String authorIntroduction){
		this.authorIntroduction = authorIntroduction;
	}
}
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
public class AchDebris_Json{
	/** 编号::*/
	private Integer	id;
	/** 碎片名称::*/
	private String	fragmentname;
	/** 碎片描述::*/
	private String	fragmentdescribe;
	/** gameid::*/
	private Integer	gameid;
	/** chatid::*/
	private Integer	chatid;
	/** cid::*/
	private Integer	cid;
	/** 获得碎片数::*/
	private Integer	getfragment;
	/** 属于小徽章id::*/
	private Integer	fromBadgeId;

	/** 编号::*/
	public Integer getId(){
		return this.id;
	}
	/** 碎片名称::*/
	public String getFragmentname(){
		return this.fragmentname;
	}
	/** 碎片描述::*/
	public String getFragmentdescribe(){
		return this.fragmentdescribe;
	}
	/** gameid::*/
	public Integer getGameid(){
		return this.gameid;
	}
	/** chatid::*/
	public Integer getChatid(){
		return this.chatid;
	}
	/** cid::*/
	public Integer getCid(){
		return this.cid;
	}
	/** 获得碎片数::*/
	public Integer getGetfragment(){
		return this.getfragment;
	}
	/** 属于小徽章id::*/
	public Integer getFromBadgeId(){
		return this.fromBadgeId;
	}
	/**编号::*/
	public void setId(Integer id){
		this.id = id;
	}
	/**碎片名称::*/
	public void setFragmentname(String fragmentname){
		this.fragmentname = fragmentname;
	}
	/**碎片描述::*/
	public void setFragmentdescribe(String fragmentdescribe){
		this.fragmentdescribe = fragmentdescribe;
	}
	/**gameid::*/
	public void setGameid(Integer gameid){
		this.gameid = gameid;
	}
	/**chatid::*/
	public void setChatid(Integer chatid){
		this.chatid = chatid;
	}
	/**cid::*/
	public void setCid(Integer cid){
		this.cid = cid;
	}
	/**获得碎片数::*/
	public void setGetfragment(Integer getfragment){
		this.getfragment = getfragment;
	}
	/**属于小徽章id::*/
	public void setFromBadgeId(Integer fromBadgeId){
		this.fromBadgeId = fromBadgeId;
	}
}
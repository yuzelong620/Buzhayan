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
public class DiscussTemplate_Json{
	/** id::*/
	private Integer	id;
	/** 游戏id::*/
	private Integer	gameId;
	/** chatId::*/
	private Integer	chatId;
	/** talkId::*/
	private Integer	talkId;
	/** optionIndex::*/
	private Integer	optionIndex;
	/** 游戏选项描述::*/
	private String	optiondesc;
	/** 评论用户id(机器人)::*/
	private Integer	playerId;
	/** 内容::*/
	private String	content;
	/** 点赞数::*/
	private Integer	likeCount;

	/** id::*/
	public Integer getId(){
		return this.id;
	}
	/** 游戏id::*/
	public Integer getGameId(){
		return this.gameId;
	}
	/** chatId::*/
	public Integer getChatId(){
		return this.chatId;
	}
	/** talkId::*/
	public Integer getTalkId(){
		return this.talkId;
	}
	/** optionIndex::*/
	public Integer getOptionIndex(){
		return this.optionIndex;
	}
	/** 游戏选项描述::*/
	public String getOptiondesc(){
		return this.optiondesc;
	}
	/** 评论用户id(机器人)::*/
	public Integer getPlayerId(){
		return this.playerId;
	}
	/** 内容::*/
	public String getContent(){
		return this.content;
	}
	/** 点赞数::*/
	public Integer getLikeCount(){
		return this.likeCount;
	}
	/**id::*/
	public void setId(Integer id){
		this.id = id;
	}
	/**游戏id::*/
	public void setGameId(Integer gameId){
		this.gameId = gameId;
	}
	/**chatId::*/
	public void setChatId(Integer chatId){
		this.chatId = chatId;
	}
	/**talkId::*/
	public void setTalkId(Integer talkId){
		this.talkId = talkId;
	}
	/**optionIndex::*/
	public void setOptionIndex(Integer optionIndex){
		this.optionIndex = optionIndex;
	}
	/**游戏选项描述::*/
	public void setOptiondesc(String optiondesc){
		this.optiondesc = optiondesc;
	}
	/**评论用户id(机器人)::*/
	public void setPlayerId(Integer playerId){
		this.playerId = playerId;
	}
	/**内容::*/
	public void setContent(String content){
		this.content = content;
	}
	/**点赞数::*/
	public void setLikeCount(Integer likeCount){
		this.likeCount = likeCount;
	}
}
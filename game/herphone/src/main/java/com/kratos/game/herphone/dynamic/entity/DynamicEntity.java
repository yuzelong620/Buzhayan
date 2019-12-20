package com.kratos.game.herphone.dynamic.entity;

import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import com.kratos.game.herphone.dynamic.bean.ResBean;
import lombok.Data;

@Data
@Document
public class DynamicEntity {
	@Id
	private String id;
	
	@Indexed
	String toDynamicId = "";//  回复消息id
	
	@Indexed
	long   toPlayerId;//  当这个值为0时 ,是动态。不是0是评论
	String toPlayerNickName="";
	String toReplyText="";//回复的那一条消息
	@Indexed
	long   fromPlayerId;// 评论玩家id
	String fromNickName = "";// 评论玩家的名字
	String fromAvatarUrl = "";// 头像
	
	List<ResBean> resList;//资源列表
	
	@Indexed
	long createTime;// 创建时间 
	@Indexed
	String groupId;//评论组号，第一个评论剧本条目的id
	@Indexed
	List<Integer> tags;//标签
	@Indexed
	int praiseNum;// 点赞数 
	@Indexed
	int replieNum;// 回复数
	@Indexed
	long lastReplyUpdateTime;//最后回复更新时间--用于判断 是否有最新评论
	String lastReplyId;//最后回复的 消息id
	@Indexed
	int readState;//1已读 ，0未读 
	@Indexed
	int isDelete;//0默认，1已经删除。
	long deleteTime;//刪除時間
	int isBest;//是否为神评
	@Indexed
	long titleTime; //设置神评时间
	
	public DynamicEntity() {
	}

	public DynamicEntity(String id, String toDynamicId, long toPlayerId, String toPlayerNickName, long fromPlayerId,
			String fromNickName, String fromAvatarUrl, List<ResBean> resList, long createTime, String groupId,
			int praiseNum, int replieNum, long lastReplyUpdateTime, int readState, int isDelete,List<Integer> tags,String toReplyText) {
		this.id = id;
		this.toDynamicId = toDynamicId;
		this.toPlayerId = toPlayerId;
		this.toPlayerNickName = toPlayerNickName;
		this.fromPlayerId = fromPlayerId;
		this.fromNickName = fromNickName;
		this.fromAvatarUrl = fromAvatarUrl;
		this.resList = resList;
		this.createTime = createTime;
		this.groupId = groupId;
		this.praiseNum = praiseNum;
		this.replieNum = replieNum;
		this.lastReplyUpdateTime = lastReplyUpdateTime;
		this.readState = readState;
		this.isDelete = isDelete;
		this.tags=tags;
		this.toReplyText=toReplyText;
	}

	
}

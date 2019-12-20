package com.kratos.game.herphone.report.bean;

import java.util.List;

import com.kratos.game.herphone.discuss.entity.DiscussEntity;
import com.kratos.game.herphone.dynamic.bean.ResBean;
import com.kratos.game.herphone.dynamic.entity.DynamicEntity;
import com.kratos.game.herphone.fandom.entity.FandomEntity;
import com.kratos.game.herphone.playerDynamic.entity.PlayerDynamicEntity;
import com.kratos.game.herphone.report.entity.ReportInfoEntity;

import lombok.Data;

@Data
public class ResReportBean {
	private String id;			//举报id
	private String toplayerId;	//被举报人id
	private String toplayer;	//被举报人名字
	private String content;		//评论内容
	private String fromplayer;	//举报人名字
	private int protectEyeTitle; //举报人是否为护眼大队 0否 1是
	private String timestamp;		//举报时间
	private int type;			//举报评论的类型 0剧本1广场2粉圈
	private 	List<ResBean> resList;// 资源列表
	
	
	//剧本评论举报构造
	public ResReportBean(ReportInfoEntity reportInfoEntity,DiscussEntity discussEntity,PlayerDynamicEntity playerDynamicEntity,String timestamp) {
		super();
		this.id = reportInfoEntity.getId();
		this.toplayerId = String.valueOf(discussEntity.getFromPlayerId());
		this.toplayer =discussEntity.getFromNickName() ;
		this.content = discussEntity.getContent();
		this.fromplayer = playerDynamicEntity.getNickName();
		this.protectEyeTitle = reportInfoEntity.getIsTitle();
		this.timestamp = timestamp;
		this.type = reportInfoEntity.getType();
		this.resList = null;
	}
	
	//广场评论举报构造
		public ResReportBean(ReportInfoEntity reportInfoEntity,DynamicEntity dynamicEntity,PlayerDynamicEntity playerDynamicEntity,String timestamp) {
			super();
			this.id = reportInfoEntity.getId();
			this.toplayerId = String.valueOf(dynamicEntity.getFromPlayerId());
			this.toplayer =dynamicEntity.getFromNickName() ;
			this.content = null;
			this.fromplayer = playerDynamicEntity.getNickName();
			this.protectEyeTitle = reportInfoEntity.getIsTitle();
			this.timestamp = timestamp;
			this.type = reportInfoEntity.getType();
			this.resList = dynamicEntity.getResList();
		}
		//粉圈评论举报构造
		public ResReportBean(ReportInfoEntity reportInfoEntity,FandomEntity fandomEntity,PlayerDynamicEntity playerDynamicEntity,String timestamp) {
			super();
			this.id = reportInfoEntity.getId();
			this.toplayerId = String.valueOf(fandomEntity.getFromPlayerId());
			this.toplayer =fandomEntity.getFromPlayerNick() ;
			this.content = fandomEntity.getMessage();
			this.fromplayer = playerDynamicEntity.getNickName();
			this.protectEyeTitle = reportInfoEntity.getIsTitle();
			this.timestamp = timestamp;
			this.type = reportInfoEntity.getType();
			this.resList =null;
		}
		public ResReportBean() {
			super();
		}
	
}

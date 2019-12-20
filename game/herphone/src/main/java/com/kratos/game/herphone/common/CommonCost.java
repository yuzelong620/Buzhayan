package com.kratos.game.herphone.common;

public interface CommonCost {
	/**	
                   消息类型 
	   0文本 
	   1语音 
       2图片 
	   3视频 
	 */
	public enum MessageType{
		/**0文本*/
		text,
		/**1语音*/
        voice, 
		/**2图片 */
		picture,
		/**3视频 */
		video;
		
	}
	public enum ReceiveState{
		/**0未达成*/
		undone,
		/**1已达成未领取*/
        notReceive, 
		/**2已领取 */
        receive,
	}
	/**
	 * 粉圈儿消息类型 
	 *
	 */
    public enum	FandomType{
    	/**
    	 * 0玩家
    	 */
		player,
		/**
		 * 1作者
		 */
		author;
	}
	/**
	 * 是否已经点赞 
	 */
	public enum IsPraise{
		/**0未点赞*/
		no,
		/**1已经点赞*/
		yes;
	}
	public enum DiscussType{
		/**0剧本评论*/
		script,
		/**1广场评论*/
		square,
		/**2粉圈评论*/
		fandom;
	}
	/** 
	 *  动态的资源类型
	 *  @author zxd
	 */
	public enum ResType{
		none,
		/**1文字*/
		text,
		/**2图片 */
		picture,
		/**3视频 */
		video;
		
		/**
		 * 根据索引查找
		 * @param ordinal
		 * @return
		 */
		public static ResType findByType(int ordinal){
			for(ResType type:values()){
				if(type.ordinal()==ordinal){
					return type;
				}
			}
			return none;
		}
	}
	/**
	 * 是否已经删除 
	 *
	 */
	public  enum IsDelete{
		none,
		/**已经删除*/
		deleted;
	}
	/** 处理举报信息状态*/
	public enum DealState{
		/**0未处理*/
		undeal,
		/**1已处理*/
		deal,
		/**2搁置*/
		hold
	}
	
	/**
	 * 处理评论推荐
	 * */
	public enum HandlerState{
		/**0未处理*/
		undeal,
		/**1已处理*/
		deal,
		/**2搁置*/
		hold
	} 
	/**
	 * 是否是热评 
	 */
	public enum IsHot{
		/**0*/
		none,
		/**1*/
		hot;
	}
	public enum IsBest{
		/**0*/
		none,
		/**1*/
		isBest;
	}
	/** 消息 读取状态   */
	public enum ReadState{
		/**0未读*/
		unread,
		/**1已读  */
		read;
	}
	
	/** 物品类型  */
    public enum ItemType{
    	none,
    	/**1增加 体力值 */
    	addPowerValue,
    	/**2增加体力上限 */
    	addPowerMax,
    	/**增加体力恢复速度  */
    	addPowerStep
    }
    
    public static final String APP_ID = "1109515925";
    public static final String SECRET = "HUqWbj3irfpTv4IJ";
    
    /** 物品类型  */
    public enum TitleType{
    	none,   	
    	/**1增加体力上限 */
    	addPowerMax,
    }
    /** 渠道id*/
    public enum ChannelId{
    	/**
    	 * 默认之前QQ参数
    	 */
        none( "1109515925","HUqWbj3irfpTv4IJ"), 
        /**新的qq 参数*/
		qq001("1109546738","DVRkHPOYETm0kByI");
    	 
		String appId;
    	String sectet;
    	 
    	private ChannelId(String appId, String sectet) {
			this.appId = appId;
			this.sectet = sectet;
		}
		public String getAppId() {
			return appId;
		}
		public String getSectet() {
			return sectet;
		}
		/**
		 * 根据名字查找
		 * @param name
		 * @return
		 */
		public static ChannelId findByName(String name){
			for(ChannelId obj:values()){
				if(obj.name().equals(name)){
					return obj;
				}
			}
			return none;
		}
    }
    public enum SendType{
    	/**系统给玩家发消息*/
    	toplayer,
    	/**玩家给系统发消息*/
    	fromplayer
    	
    }
    public enum ContentType{
    	/**内容为文字*/
    	text,
    	/**内容含有链接*/  
    	link
    	
    }
    public enum TackType{
    	none,
    	/**看推荐视频*/
    	watchVideo,
    	/**分享*/
    	dailyShare,
    	/**获得线索值*/
    	clue,
    	/**消耗电量*/
    	consumeElectric,
    	/**好感度*/
    	goodFeeling,
    	/**获得成就碎片*/
    	achievementDebris
    }
    
    /**
     * 阶段任务类型
     * @author Administrator
     *
     */
    public enum StageTaskType{
    	none,
    	/**玩完整的副本*/
    	playGame,
    	/**获得成就碎片*/
    	achievementDebris,
    	/**获得线索值*/
    	clue,
    	/**关注用户*/
    	attentionUser,
    	/**关注作者*/
    	attentionAuthor,
    	/**打赏礼物*/
    	sendPresents
    }
    /**
               * 阶段任务领取状态
     */
    public enum StageTaskGetState{
    	/**没达到需求*/
    	none,
    	/**已领取*/
    	haveGot
    }
    
    /**
               * 任务阶段
     */
    public enum TaskStage{
    	none,
    	/**阶段1*/
    	stageOne,
    	/**阶段2*/
    	stageTwo,
    	/**阶段3*/
    	stageThree
    }
}

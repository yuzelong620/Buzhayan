package com.kratos.game.herphone.discuss.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.globalgame.auto.json.DiscussTemplate_Json;
import com.globalgame.auto.json.GameCatalog_Json;
import com.globalgame.auto.json.GameParams_Json;
import com.kratos.engine.framework.net.socket.exception.BusinessException;
import com.kratos.game.herphone.blackList.entity.BlackListEntity;
import com.kratos.game.herphone.common.BaseService;
import com.kratos.game.herphone.common.CommonCost.IsBest;
import com.kratos.game.herphone.common.CommonCost.IsHot;
import com.kratos.game.herphone.common.CommonCost.ReadState;
import com.kratos.game.herphone.discuss.bean.DiscussAllBean;
import com.kratos.game.herphone.discuss.bean.DiscussFindByGroupIdRes;
import com.kratos.game.herphone.discuss.bean.DiscussListByPageRes;
import com.kratos.game.herphone.discuss.bean.DiscussListRes;
import com.kratos.game.herphone.discuss.bean.DiscussMyListByPageRes;
import com.kratos.game.herphone.discuss.bean.DiscussReplyBean;
import com.kratos.game.herphone.discuss.bean.DiscussReplyListRes;
import com.kratos.game.herphone.discuss.bean.MainDiscussBean;
import com.kratos.game.herphone.discuss.bean.ResReplyDiscussBean;
import com.kratos.game.herphone.discuss.bean.TopDiscussBean;
import com.kratos.game.herphone.discuss.entity.DiscussEntity;
import com.kratos.game.herphone.json.JsonCacheManager;
import com.kratos.game.herphone.json.datacache.DiscussTemplateCache;
import com.kratos.game.herphone.json.datacache.GameCatalogCache;
import com.kratos.game.herphone.json.datacache.GameParamsCache;
import com.kratos.game.herphone.message.bean.DiscussBean;
import com.kratos.game.herphone.player.PlayerContext;
import com.kratos.game.herphone.player.domain.Player;
import com.kratos.game.herphone.player.web.GMDataChange;
import com.kratos.game.herphone.util.StringUtil;
import com.kratos.game.herphone.playerDynamic.entity.PlayerDynamicEntity;
import com.kratos.game.herphone.util.DateUtil;
import lombok.extern.log4j.Log4j;

@Log4j
@Service
public class DiscussService extends BaseService{
	private  static DiscussService instance;
	public DiscussService() {
		instance = this;
	}
	
	public static DiscussService getInstance() {
		return instance;
	}

	public  void init() {
		playerServiceImpl.creatRobot();
		playerServiceImpl.checkGameCatalog();//检查作者id
		DiscussTemplateCache discussTemplateCache = JsonCacheManager.getCache(DiscussTemplateCache.class);
		List<DiscussTemplate_Json> discussTemplate_Jsons = discussTemplateCache.getList();
		DiscussEntity discussEntity = null;
		for (DiscussTemplate_Json json : discussTemplate_Jsons) {
			discussEntity = discussDao.findByID(json.getId().toString());
			if (discussEntity == null) {
				long playerId = json.getPlayerId();
				Player player = playerServiceImpl.get(playerId);
				if (player == null) {
					throw new RuntimeException("你把玩家id传错了！"+playerId);
				}
				sendDiscuss(json.getGameId(), json.getChatId(), json.getTalkId(), json.getOptionIndex(), json.getContent(),player,json.getId().toString());
				discussDao.addLikeNum(json.getId().toString(), json.getLikeCount());
			}
			
			
		}
	}
	
	private String discussDynamicId(int gameId, int chatId, int talkId, int optionIndex){
		return StringUtil.appendString(gameId,chatId,talkId,optionIndex);
	}
	
	private String discussDynamicId(int gameId, int chatId){
		return StringUtil.appendString(gameId,chatId);
	}
	
	public List<DiscussBean> toBean(List<DiscussEntity> list){
		List<DiscussBean> temp=new ArrayList<>();
		if(list!=null){
			for(DiscussEntity obj:list){
				temp.add(new DiscussBean(obj));
			}
		}
		return temp;		
	}
	/**
	 * 
	 * @param list
	 * @param lastReplyTime 最后一个未读消息的更新时间
	 * @return
	 */
	public List<DiscussBean> toMainBean(List<DiscussEntity> list ){
		//设置最新 unreadNum
		int page=1;
		int replyCount =GameParamsCache.getGameParams_Json().getDiscussPageCount();//查询最新
		Player player=PlayerContext.getPlayer();
		Map<String,MainDiscussBean> map=new HashMap<>(); 
		for(DiscussEntity obj:list){
			MainDiscussBean bean=new MainDiscussBean(obj);
			map.put(bean.getId(), bean);
		}
		List<DiscussEntity> replys=discussDao.myReplyListByPage(player.getId(),page,replyCount);
		for(DiscussEntity obj:replys){
			MainDiscussBean bean=map.get(obj.getToDiscussId());
			if(bean==null){
				continue;
			}
			if(bean.getFirstReply()==null){
				bean.setFirstReply(new DiscussBean(obj));
			}
			//增加未读消息数量
			if(obj.getReadState()==ReadState.unread.ordinal()){
				bean.setUnreadNum(bean.getUnreadNum()+1);
			}
		}
		return new ArrayList<>(map.values());
	}

	public DiscussListRes listByIndex(int gameId, int chatId, int talkId,int index) {
		if(gameId==-1||chatId==-1||index==-1||talkId==-1){
			throw new BusinessException("参数错误");
		}
		String dynamicId=discussDynamicId(gameId, chatId,talkId, index);
		int page=1;//当前页数
		int count=GameParamsCache.getGameParams_Json().getDiscussPageCount();//一页显示的条数
		
		int topLimit=GameParamsCache.getGameParams_Json().getTopLimit();//取前几名
		int topLimitPraiseNum=GameParamsCache.getGameParams_Json().getTopLimitPraiseNum();//点赞数限制
		//最新评论
		List<DiscussEntity> recentCommentList= discussDao.findByDynamicId(dynamicId, page, count);
		//精彩评论
		List<DiscussEntity> topCommentList=discussDao.findTop(dynamicId, topLimit, topLimitPraiseNum);
		checkAndSetHot(topCommentList);
//		//过滤 top中的玩家
//		for (int i = recentCommentList.size()-1; i >= 0; i--) {
//			for (DiscussEntity discussEntity : topCommentList) {
//				if (discussEntity.getId().equals(recentCommentList.get(i).getId())) {
//					recentCommentList.remove(i);
//					break;
//				}
//			}
//		}
		Map<Long, PlayerDynamicEntity> titleMap=getItemTitles(recentCommentList,topCommentList);//称号集合
		
		List<TopDiscussBean> recentComment= toTopBean(recentCommentList,titleMap);
		List<TopDiscussBean> topComment= toTopBean(topCommentList,titleMap); 
		
		//设置 是否点过赞
		ArrayList<DiscussBean> temp=new ArrayList<>();
		temp.addAll(recentComment);
		temp.addAll(topComment);
		Player player=PlayerContext.getPlayer();
		checkAndSetPaiseState(temp, player.getId());
		
		return new DiscussListRes(page,count,recentComment, topComment);
	}
    /**
     * 检查和设置 hot状态
     * @param topCommentList
     */
	private void checkAndSetHot(List<DiscussEntity> topCommentList) {
		for(DiscussEntity  obj:topCommentList){
			if(obj.getIsHot()==IsHot.none.ordinal()){
				if(discussDao.updateIsHotState(obj.getId())){
					playerDynamicDao.incHotDiscussCount(obj.getFromPlayerId());
				}
			}
		}
	}
	/**
	 * 获取 称号id
	 */
	private Map<Long,PlayerDynamicEntity> getItemTitles(List<DiscussEntity>... arr){
		List<Long> playerIds=new ArrayList<>();
		for(List<DiscussEntity> list:arr){
			for(DiscussEntity obj:list){
				playerIds.add(obj.getFromPlayerId());
			}
		}
		return playerDynamicService.getItmeTitle(playerIds); 
	}
	
	private List<TopDiscussBean> toTopBean(List<DiscussEntity> list,Map<Long,PlayerDynamicEntity> titleMap){
		List<TopDiscussBean> temp=new ArrayList<>();
		for(DiscussEntity obj:list){
			PlayerDynamicEntity dynamic=titleMap.get(obj.getFromPlayerId());
			if(dynamic==null){
				temp.add(new TopDiscussBean(obj));
			}
			else{
			    temp.add(new TopDiscussBean(obj,titleMap.get(obj.getFromPlayerId())));
			}
		}
		return temp;
	}
	
	private List<TopDiscussBean> toTopBean(List<DiscussEntity> topCommentList){
		Map<Long,PlayerDynamicEntity>  titleMap=getItemTitles(topCommentList);
		return toTopBean(topCommentList, titleMap);
	}

	public DiscussListByPageRes listByPage(int gameId, int chatId,int talkId, int index, int page) {
		if(gameId==-1||chatId==-1||index==-1||page==-1){
			throw new BusinessException("参数错误");
		}
		int count=GameParamsCache.getGameParams_Json().getDiscussPageCount();//一页显示的条数
		String dynamicId=discussDynamicId(gameId, chatId,talkId, index);
		List<DiscussEntity> recentCommentList= discussDao.findByDynamicId(dynamicId, page, count);
		List<DiscussBean> list=new ArrayList<>(toTopBean(recentCommentList)); 
		Player player=PlayerContext.getPlayer();
		checkAndSetPaiseState(list, player.getId());
		
		return new DiscussListByPageRes(page, count,list);
	}
	   /** 根据游戏ID和章节ID查询对应评论
	     * @param req
	     * @return 返回实体类中不包含玩家头像框及徽章信息
	     */
	public DiscussListByPageRes listByPage(int gameId, int chatId, int page) {
		if(gameId==-1||chatId==-1){
			throw new BusinessException("参数错误");
		}
		////一页显示的条数
		int count = 10;
		//int count=GameParamsCache.getGameParams_Json().getDiscussPageCount();//一页显示的条数
		String dynamicId=discussDynamicId(gameId, chatId);
		List<DiscussEntity> recentCommentList= discussDao.findByDynamicId(dynamicId, page, count);
		List<DiscussBean> list=new ArrayList<>(toTopBean(recentCommentList)); 
		Player player=PlayerContext.getPlayer();
		checkAndSetPaiseState(list, player.getId());
		
		return new DiscussListByPageRes(page, count,list);
	}
	

	
	/**
	 * 弹出禁言消息
	 * @param player
	 */
	void  alertNoSpe(Player player){
		long end=player.getNoSpeakTime();
		long currentTime=System.currentTimeMillis();
		if(end>currentTime){
			throw new BusinessException("您已被禁言至： "+DateUtil.getdate_yyyy_MM_dd_HH_MM_SS(player.getNoSpeakTime()));
		}
	}
    //发送评论
	public void sendDiscuss(int gameId, int chatId, int talkId,int index, String content) {
		Player player = PlayerContext.getPlayer();
		alertNoSpe(player);
		if(gameId==-1||chatId==-1||index==-1||talkId==-1){
			throw new BusinessException("参数错误");
		}
		if(content==null||content.trim().equals("")){
			throw new BusinessException("发送内容不能为空");
		}
		int limitContent=GameParamsCache.getGameParams_Json().getDiscussContentLimit();
		if(content.length()>limitContent){
			throw new BusinessException("内容过长");
		}
		String id=UUID.randomUUID().toString().replace("-", "");
		sendDiscuss(gameId,chatId,talkId,index,content,player,id);
		
		//添加发表评论数量
		playerDynamicDao.addSendDiscussNum(player.getId(), 1);
	}
	 //发送评论
		public void sendDiscuss(int gameId, int chatId, String content) {
			Player player = PlayerContext.getPlayer();
			alertNoSpe(player);
			if(gameId==-1||chatId==-1){
				throw new BusinessException("参数错误");
			}
			if(content==null||content.trim().equals("")){
				throw new BusinessException("发送内容不能为空");
			}
			int limitContent=GameParamsCache.getGameParams_Json().getDiscussContentLimit();
			if(content.length()>limitContent){
				throw new BusinessException("内容过长");
			}
			String id=UUID.randomUUID().toString().replace("-", "");
			//player.setId(10086L);
			sendDiscuss(gameId,chatId,content,player,id);
			
			//添加发表评论数量
			playerDynamicDao.addSendDiscussNum(player.getId(), 1);
		}
	
		public void sendDiscuss(int gameId, int chatId,String content,Player player,String id) {
			String dynamicId=StringUtil.appendString(gameId,chatId);
			long time=System.currentTimeMillis();
	        DiscussEntity entity=new DiscussEntity(id, dynamicId, "", time, content, player.getId(),  player.decodeName(), player.getAvatarUrl(), 0, 0,0L,"",time,id);//groupId设置为自己
		    discussDao.save(entity);
		    
		    //增加系统评论数
		    discussOptionService.load(gameId, chatId);
		    discussOptionDao.incDiscussNum(dynamicId);
		}
		
	public void sendDiscuss(int gameId, int chatId, int talkId,int index, String content,Player player,String id) {
		String dynamicId=this.discussDynamicId(gameId, chatId,talkId, index);
		long time=System.currentTimeMillis();
        DiscussEntity entity=new DiscussEntity(id, dynamicId, "", time, content, player.getId(),  player.decodeName(), player.getAvatarUrl(), 0, 0,0L,"",time,id);//groupId设置为自己
	    discussDao.save(entity);
	    
	    //增加系统评论数
	    discussOptionService.load(gameId, chatId, talkId, index);
	    discussOptionDao.incDiscussNum(dynamicId);
	}
	/**
	 * 回复评论
	 * @param gameId
	 * @param chatId
	 * @param index
	 * @param content
	 */
	public void replyDiscuss(String discussId,String content) {
		Player player = PlayerContext.getPlayer();
		alertNoSpe(player);
		//player.setId(10086L);
		if(discussId==null||discussId.trim().equals("")){
			throw new BusinessException("参数错误");
		}
		if(content==null||content.trim().equals("")){
			throw new BusinessException("发送内容不能为空");
		}
		DiscussEntity reply= discussDao.findByID(discussId);
		if(reply==null){
			throw new BusinessException("您回复的评论不存在");
		}
		if(reply.getFromPlayerId()==player.getId()){
			throw new BusinessException("不能回复自己的评论");
		}
		BlackListEntity toblackListEntity = blackListService.load(reply.getFromPlayerId());
		if (toblackListEntity.getBlackList().contains(player.getId())) {
			throw new BusinessException("对方设置了权限，不能进行评论哦~");
		}
		BlackListEntity fromblackListEntity = blackListService.load(player.getId());
		if (fromblackListEntity.getBlackList().contains(reply.getFromPlayerId())) {
			throw new BusinessException("你已将对方拉黑，不能进行评论");
		}
		int limitContent=GameParamsCache.getGameParams_Json().getDiscussContentLimit();
		if(content.length()>limitContent){
			throw new BusinessException("内容过长");
		}
		String id=UUID.randomUUID().toString().replace("-", "");
		long currentTime=System.currentTimeMillis();
		
        DiscussEntity entity=
        		new DiscussEntity(id, reply.getDynamicId(),discussId,currentTime,
        				content, player.getId(),  player.decodeName(),player.getAvatarUrl(), 0, 0,reply.getFromPlayerId(),reply.getFromNickName(),currentTime,reply.getGroupId());
	    discussDao.save(entity);
	    //更新一下被回复的消息的  最后回复更新时间
	    systemService.sendDiscussReply(entity);
	    discussDao.updateReplyInfo(discussId,currentTime);
	}
	/**
	 * 根据组号查询 
	 * @param groupId
	 * @param page
	 * @return
	 */
    public DiscussFindByGroupIdRes findReplyByGroup(String groupId,int page){
    	if(groupId==null||groupId.trim().equals("")){
			throw new BusinessException("参数错误");
		}
		DiscussEntity discuss= discussDao.findByID(groupId);
		if(discuss==null){
			throw new BusinessException("参数错误");
		}
		if(discuss.getToPlayerId()!=0){//不是评论
			throw new BusinessException("不是评论信息，参数错误！");
		}
		//评论分页条数
		int count = 10;
		//int count=GameParamsCache.getGameParams_Json().getDiscussPageCount();
		List<DiscussEntity> replys=discussDao.findByGroupId(groupId,page,count);
		//设置点赞状态
		ArrayList<String> unreadIds=new ArrayList<>();
		List<DiscussBean> list=new ArrayList<>(toTopBean(replys));
//	    for(DiscussEntity obj:replys){
//			list.add(new DiscussBean(obj));
//			if(obj.getReadState()==ReadState.unread.ordinal()){
//				unreadIds.add(obj.getId());
//			}
//		}
//	    if(unreadIds.size()>0){
//	    	discussDao.setReadState(unreadIds);//设置已读状态
//	    }
		Player player=PlayerContext.getPlayer();
		checkAndSetPaiseState(list, player.getId());
		
		return new DiscussFindByGroupIdRes(page,count,list);
	}
    //回复列表
	public DiscussReplyListRes replyListByPage(String discussId, int page) {
		Player player=PlayerContext.getPlayer();
		if(discussId==null||discussId.trim().equals("")){
			throw new BusinessException("参数错误");
		}
		DiscussEntity discuss= discussDao.findByID(discussId);
		if(discuss==null){
			throw new BusinessException("参数错误");
		}
		int count=GameParamsCache.getGameParams_Json().getDiscussPageCount();
		//根据评论ID查询评论
		List<DiscussEntity> replys=discussDao.findByPageAndId(discussId,page,count);
		//设置点赞状态
		ArrayList<String> unreadIds=new ArrayList<>();
		List<DiscussBean> list=new ArrayList<>();
	    for(DiscussEntity obj:replys){
			list.add(new DiscussBean(obj));
			if(obj.getReadState()==ReadState.unread.ordinal()&&obj.getToPlayerId()==player.getId()){
				unreadIds.add(obj.getId());
			}
		}
	    if(unreadIds.size()>0){
	    	discussDao.setReadState(unreadIds);//设置已读状态
	    }
		checkAndSetPaiseState(list, player.getId());
		
		return new DiscussReplyListRes(list);
	}
    //查询回复我的信息
	public DiscussMyListByPageRes myReplyListByPage(int page) {
		//获取玩家ID
		Player player=PlayerContext.getPlayer();
		//查询分页参数
		int count=GameParamsCache.getGameParams_Json().getDiscussPageCount();
		//查询对我的评论
		List<DiscussEntity> list=discussDao.myReplyListByPage(player.getId(),page,count);
		//创建返回实体类
		List<DiscussBean> results=new ArrayList<>();
		Map<String,DiscussReplyBean> temp=new HashMap<>();
		//设置点赞状态
//		List<String> unreadIds=new ArrayList<>();
//		List<String> toDiscussIds=new ArrayList<>();
		for(DiscussEntity obj:list){
//			if(obj.getReadState()==ReadState.unread.ordinal()&&obj.getToPlayerId()==player.getId()){
//				unreadIds.add(obj.getId());
//			}
			//DiscussReplyBean bean=temp.get(obj.getToDiscussId());
			//if(bean==null){
			DiscussReplyBean bean=new DiscussReplyBean(obj);
			//}
			temp.put(obj.getToDiscussId(),bean);
			results.add(bean);
			DiscussEntity discussEntity = discussDao.findByID(obj.getToDiscussId());
			temp.get(discussEntity.getId()).setToMyDiscuss(new DiscussBean(discussEntity));
			//toDiscussIds.add(obj.getToDiscussId());
		}
//		if(unreadIds.size()>0){
//		    discussDao.setReadState(unreadIds);//设置已读状态
//		}
//		System.err.println(toDiscussIds);
//		System.err.println(temp);
//		List<DiscussEntity> toDiscusses=discussDao.findByIds(toDiscussIds);
//		for(DiscussEntity obj:toDiscusses){
//			temp.get(obj.getId()).setToMyDiscuss(new DiscussBean(obj));
//		}
		//设置点赞状态
		checkAndSetPaiseState(results, player.getId());
		System.err.println(results);
		return new DiscussMyListByPageRes(results, page, count);
	}
	
	/**
	 * 查询未对我的评论的回复消息
	 * @param page
	 * @return
	 */
	public List<ResReplyDiscussBean> findReplyMyDiscuss(int page){
		//获取玩家信息
		Player player=PlayerContext.getPlayer();
		//分页参数，每页多少条
		int count = 30;
		//实例化返回对象
		List<ResReplyDiscussBean> result = new ArrayList<ResReplyDiscussBean>();
		//实例化，玩家所有评论Id集合
		Set<String> discussBeanIds = new HashSet<>();
		//实例化Map集合，存入我的评论Id，及评论内容
		Map<String,String> discussBeanContentMap = new HashMap<String, String>();
		//实例化List集合，保存回复玩家的评论Id，用于修改阅读状态
		List<String> readDiscussIds = new ArrayList<>();
		//查询玩家下的所有评论
		List<DiscussEntity> discussEntity = discussDao.findByPlayerids(player.getId());
		//遍历玩家评论，玩家所有的评论ID，存入集合
		for (DiscussEntity obj : discussEntity) {
			discussBeanIds.add(obj.getId());
			discussBeanContentMap.put(obj.getId(), obj.getContent());
		}
		//查询对该玩家评论的所有回复
		List<DiscussEntity> replyMyDiscuss = discussDao.findReplyMyDiscuss(discussBeanIds,page,count);
		//遍历评论，将评论信息封装如实体类
		for (DiscussEntity obj : replyMyDiscuss) {
			ResReplyDiscussBean res = new ResReplyDiscussBean();
			String[] arr=obj.getDynamicId().split("_");
			//arr[0] --------剧本Id
			//arr[1] --------章节Id
			String gameId = arr[0];
			String chapterId = arr[1];
			//查询回复玩家的头像框及徽章
			PlayerDynamicEntity fromPlayerDynamicEntity = playerDynamicDao.findByID(obj.getFromPlayerId());
			//设置回复玩家的头像框
			res.setAvatarFrame(fromPlayerDynamicEntity.getAvatarFrame());
			//设置回复玩家的徽章
			res.setAchievementTags(fromPlayerDynamicEntity.getAchievementTags());
			//设置我的评论id
			res.setMyDiscussId(obj.getToDiscussId());
			//设置已读状态
			res.setReadState(obj.getReadState());
			//设置我的评论内容
			res.setMyContent(discussBeanContentMap.get(obj.getToDiscussId()));
			//设置游戏Id，章节Id
			res.setGameId(Integer.parseInt(gameId));
			res.setChapterId(Integer.parseInt(chapterId));
			//设置回复玩家Id
			res.setFromPlayerId(obj.getFromPlayerId()+"");
			//设置回复玩家昵称
			res.setFromNickName(obj.getFromNickName());
			//设置回复玩家头像
			res.setFromHeadImgUrl(obj.getFromAvatarUrl());
			//设置回复玩家评论id
			res.setFromDiscussId(obj.getId());
			//设置回复评论内容
			res.setFromContent(obj.getContent());
			//设置回复评论时间
			res.setReplyTime(obj.getCreateTime());
			if(obj.getReadState()==0) {
				readDiscussIds.add(obj.getId());
			}
			result.add(res);
		}
		//设置已读状态
		discussDao.setReadState(readDiscussIds);
		return result;
	}
	
	/**
	 * 查询未读评论信息数量
	 * @param page
	 * @return
	 */
	
	public long findReplyMyDiscussCount() {
		//获取玩家信息
		Player player=PlayerContext.getPlayer();
		//查询未读评论信息数量
		return discussDao.findReplyMyDiscussCount(player.getId());
	}
	
	//查找自己的评论信息
	public DiscussMyListByPageRes findMyDiscussByPage(int page){
		int count=GameParamsCache.getGameParams_Json().getDiscussPageCount();
		Player player=PlayerContext.getPlayer();
		List<DiscussEntity> list =discussDao.findMydiscuss(player.getId(),page,count);
		List<DiscussBean> items= toMainBean(list);
		return new DiscussMyListByPageRes(items,page,count);
	}
	/**
	 * 检查和设置 是否点赞状态
	 * @param items
	 * @param playerId
	 */
	private void checkAndSetPaiseState(List<DiscussBean> items,long playerId){
		List<String> discussIds=new ArrayList<>();
		for (DiscussBean obj:items){
			discussIds.add(obj.getId());
		}
		List<String>  likeDiscussIds= discussLikeService.findLikeDiscussIds(playerId, discussIds);
		for(DiscussBean bean:items){
			if(likeDiscussIds.contains(bean.getId())){
				bean.setIsPraise(1);
			}
		}
	}
 
	public int getPlayerUnreadReplyState(long playerId) {
		if(discussDao.findOneUnreadReply(playerId)==null){
			return 0;
		}
		return 1;
	}
    
	public void recommendBest(String discussId){
		Player player=PlayerContext.getPlayer();
		GameParams_Json gameParams_Json = GameParamsCache.getGameParams_Json();	
	    if(!playerDynamicService.containsTitleItemId(player,gameParams_Json.getProtectEyesTitleId())){
	    	 throw new BusinessException("您还不是护眼大队成员评");
	    }
	    DiscussEntity discuss=discussDao.findByID(discussId);
	    if(discuss==null){
	    	throw new BusinessException("参数错误");
	    }
	    if(discuss.getIsBest()==IsBest.isBest.ordinal()){//已经是神评
	    	return ;
	    }
	    recommendBestService.recommendBest(discuss);
	}
	/**
	 * 查询所有已经删除的评论
	 */
	public List<DiscussAllBean> getRecoverDiscussAll(int page, int count) {
		List<DiscussEntity> list =  discussDao.findDeleteDiscusses(page,count);
		return toBeans(list);
	}

	/**
	 * 恢复已经删除的评论信息
	 */
	public boolean recoverComment(String id) {
		GMDataChange.recordChange("通过剧本评论ID恢复已删除的评论\t剧本评论ID为",id);
		return discussDao.cancelDelete(id);
	}

	/**
	 * 查询评论信息 返回给前端页面
	 */
	public List<DiscussAllBean> getDiscussAll(int page,int count) {
		List<DiscussEntity> discusses = discussDao.findDiscussAll(page, count);
		return toBeans(discusses);
	}
	
	
	/**
	 * 根据评论ID删除该条评论
	 */
	public int removeDiscuss(String id) {
		DiscussEntity discussEntity = discussDao.findByID(id);
		if(discussEntity == null) {
			return 0;
		}
		discussDao.removeByIdDiscuss(id,System.currentTimeMillis()); //根据id删除评论
		GMDataChange.recordChange("通过剧本评论ID删除评论\t评论ID为",id);
		return 1;
	}

	 /**
	   * 根据评论ID设置该评论为神评
	  */
	public int setGmIsHot(String hotId) {
		boolean flag = discussDao.setIsBest(hotId,System.currentTimeMillis());
		if(flag) {
			//成功设置
			GMDataChange.recordChange("通过剧本评论ID设置神评\t评论ID为",hotId);
			return 1;
		} 
		return 0;
	}

	public List<DiscussAllBean> getDeityDiscussAll(int page,int count) {
		List<DiscussEntity> list = discussDao.findDeityDiscussAll(page, count);
		return toBeans(list);
	}

	/**
	 * 移除神评
	 */
	public int removeDeity(String id) {
		boolean flag = discussDao.removeIsBest(id);
		if(flag) {
			//成功设置
			GMDataChange.recordChange("通过剧本评论ID移除神评\t剧本评论ID为",id);
			return 1;
		} 
		return 0;
	}

	/**
	 * 查询所有评论总条数
	 */
	public long getCommentCount() {
		return discussDao.findCount();
	}

	/**
	   * 查询删除的评论总条数
	 */
	public long getRecoverCommentCount() {
		return discussDao.findRecoverCount();
	}

	/**
	 * 查询神评总条数
	 */
	public long getDeityCommentCount() {
		return discussDao.getDeityCommentCount();
	}
	private List<DiscussAllBean> toBeans(List<DiscussEntity> list) {
		List<DiscussAllBean> beans = new ArrayList<DiscussAllBean>();
		GameCatalogCache cache = JsonCacheManager.getCache(GameCatalogCache.class);
	    DiscussAllBean bean =null;
		for (DiscussEntity entity : list) {
			String[] arr=entity.getDynamicId().split("_");
			String gameId=arr[0];//游戏id
			String c_id=arr[1];//选项id
			GameCatalog_Json json=cache.getData(Integer.parseInt(gameId));
		    if(json==null){
		    	log.error("脏数据不存在的gameId:"+gameId);
		    	continue;
		    }
		    PlayerDynamicEntity playerDynamicEntity = playerDynamicService.load(entity.getFromPlayerId());			
			bean = new DiscussAllBean(entity.getId(),json.getName(),
				entity.getContent(),c_id,entity.getPraiseNum(),entity.getReplieNum(),String.valueOf(playerDynamicEntity.getRoleId()),
				entity.getFromNickName(),DateUtil.getdate_yyyy_MM_dd_HH_MM_SS(entity.getCreateTime()),entity.getIsBest());
				beans.add(bean);
		 }
		 return beans;
	}
	public int findUnreadReply() {
		Player player=PlayerContext.getPlayer();
		long toPlayerId=player.getId();
		int count=(int)discussDao.findUnreadReply(toPlayerId);
		return count;
	}
	
	//查询章节下评论
	public Object findDisscuss(int gameId, int chatId) {
		// TODO Auto-generated method stub
		return null;
	}
    
}

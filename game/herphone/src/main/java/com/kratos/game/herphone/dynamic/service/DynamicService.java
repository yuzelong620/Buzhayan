package com.kratos.game.herphone.dynamic.service;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.globalgame.auto.json.DynamicTemp_Json;
import com.kratos.engine.framework.net.socket.exception.BusinessException;
import com.kratos.game.herphone.blackList.entity.BlackListEntity;
import com.kratos.game.herphone.common.BaseService;
import com.kratos.game.herphone.common.CommonCost;
import com.kratos.game.herphone.common.CommonCost.IsDelete;
import com.kratos.game.herphone.common.CommonCost.IsPraise;
import com.kratos.game.herphone.common.CommonCost.ReadState;
import com.kratos.game.herphone.common.CommonCost.ResType;
import com.kratos.game.herphone.dynamic.bean.DiscussMyReplysRes;
import com.kratos.game.herphone.dynamic.bean.DynamicBean;
import com.kratos.game.herphone.dynamic.bean.DynamicListRes;
import com.kratos.game.herphone.dynamic.bean.DynamicReplyBean;
import com.kratos.game.herphone.dynamic.bean.ResBean;
import com.kratos.game.herphone.dynamic.entity.DynamicEntity;
import com.kratos.game.herphone.dynamic.entity.DynamicLikeEntity;
import com.kratos.game.herphone.dynamic.entity.PlayerTagsEntity;
import com.kratos.game.herphone.json.JsonCacheManager;
import com.kratos.game.herphone.json.datacache.DynamicTempCache;
import com.kratos.game.herphone.json.datacache.GameParamsCache;
import com.kratos.game.herphone.player.PlayerContext;
import com.kratos.game.herphone.player.domain.Player;
import com.kratos.game.herphone.player.web.GMDataChange;
import com.kratos.game.herphone.playerDynamic.entity.PlayerDynamicEntity;
import com.kratos.game.herphone.util.DateUtil;

import lombok.extern.log4j.Log4j;
@Deprecated
@Log4j
@Service
public class DynamicService extends BaseService {
	 //查询回复我的信息
		public DiscussMyReplysRes myReplyListByPage(int page) {
			Player player=PlayerContext.getPlayer();
			int count=GameParamsCache.getGameParams_Json().getDynamicPageCount();
			List<DynamicEntity> list=dynamicDao.myReplyListByPage(player.getId(),page,count);
			List<DynamicReplyBean> results=new ArrayList<>();
			Map<String,DynamicReplyBean> temp=new HashMap<>();
			//设置点赞状态
			 
			List<String> unreadIds=new ArrayList<>();
			List<String> toDiscussIds=new ArrayList<>();
			for(DynamicEntity obj:list){
				if(obj.getReadState()==ReadState.unread.ordinal()&&obj.getToPlayerId()==player.getId()){
					unreadIds.add(obj.getId());
				}
				DynamicReplyBean bean=temp.get(obj.getToDynamicId());
				if(bean==null){
					bean=new DynamicReplyBean(obj);
				}
				temp.put(obj.getToDynamicId(),bean);
				results.add(bean);
				toDiscussIds.add(obj.getToDynamicId());
			}
			if(unreadIds.size()>0){
			    dynamicDao.setReadState(unreadIds);//设置已读状态
			}
			List<DynamicEntity> toDiscusses=dynamicDao.findByIds(toDiscussIds);
			for(DynamicEntity obj:toDiscusses){
				temp.get(obj.getId()).setToDynamic(new DynamicReplyBean(obj));
			}
//			checkAndSetPaiseState(results, player.getId());
			return new DiscussMyReplysRes(results, page, count);
		}
//	 /**
//	  * 查找回复我的列表
//	  * @param page
//	  * @return
//	  */
//	public DynamicReplyMyListRes myReplyListByPage(int page) {
//		Player player=PlayerContext.getPlayer();
//		int count=GameParamsCache.getGameParams_Json().getDynamicPageCount();
//		List<DynamicEntity> list=dynamicDao.myReplyListByPage(player.getId(),page,count);
//		//设置点赞状态
//		List<DynamicBean> items=toDynamicBeans(list);
//		List<String> unreadIds=new ArrayList<>();
//		for(DynamicEntity obj:list){
//			if(obj.getReadState()==ReadState.unread.ordinal()){
//				unreadIds.add(obj.getId());
//			}
//		}
//		if(unreadIds.size()>0){
//		    dynamicDao.setReadState(unreadIds);//设置已读状态
//	    }
//		checkAndSetPaiseState(items, player.getId());
//		return new DynamicReplyMyListRes(items, page, count);
//	}
	
	static DynamicService instance;
	
	public DynamicService() {
		instance=this;
	}
	public static DynamicService getInstance(){
		return instance;
	}
	public void  init(){
		DynamicTempCache cache=JsonCacheManager.getCache(DynamicTempCache.class);
		for(DynamicTemp_Json obj:cache.getList()){
			String id=obj.getId()+"";
			long playerId=obj.getPlayerId();
			PlayerDynamicEntity playerDynamic=playerDynamicService.load(playerId);
			if(playerDynamic==null){
				throw new RuntimeException("配置机器人 动态 错误，无法找到playerId");
			}
			if(dynamicDao.findByID(id)!=null){
				
				List<ResBean> resList;
				try {
					resList = JSONObject.parseArray(obj.getResJson(), ResBean.class);
				} catch (Exception e) {
					log.error("------"+obj.getId());
					throw new RuntimeException();
				}   
				dynamicDao.updateDynamic(obj.getTags(), resList, obj.getPlayerId(), obj.getId()+"");
				continue;
			}
			List<ResBean> resList=JSONObject.parseArray(obj.getResJson(), ResBean.class);                                                                  
			long currentTime=System.currentTimeMillis();
			DynamicEntity entity=new DynamicEntity(obj.getId()+"", null, 0, "", playerId, playerDynamic.getNickName(), playerDynamic.getHeadImgUrl(), resList, currentTime, null, obj.getPraiseNum(), 0, currentTime, 0, 0, obj.getTags(),"");
		    log.info(" 配置动态信息,id："+obj.getId()+"插入数据库");
			dynamicDao.insert(entity);
		}
	}
	List<DynamicBean> setItemTitle( List<DynamicEntity> list){
		List<Long> fromPlayerIds=new ArrayList<>();
		List<DynamicBean> dynamicBeans=new ArrayList<>();
		for(DynamicEntity obj:list){
			fromPlayerIds.add(obj.getFromPlayerId());
			dynamicBeans.add(new DynamicBean(obj));
		}
		Map<Long, PlayerDynamicEntity> titleMap=playerDynamicService.getItmeTitle(fromPlayerIds);
		for(DynamicBean obj:dynamicBeans){
			PlayerDynamicEntity playerDynamicEntity = titleMap.get(Long.valueOf(obj.getFromPlayerId()));	
			if(playerDynamicEntity==null){
				obj.setItemTitle(new ArrayList<>());
				obj.setAchievementTags(0);
				obj.setAvatarFrame(0);
			}
			else{
				obj.setItemTitle(playerDynamicEntity.getItemTitle());
				obj.setAchievementTags(playerDynamicEntity.getAchievementTags());
				obj.setAvatarFrame(playerDynamicEntity.getAvatarFrame());
			}
		}
		return dynamicBeans;
	}
	/**
	 * 查询 所有 没有删除的 回复
	 * @param page
	 * @param count
	 * @return
	 */
	public List<DynamicBean> findReplyAll(int page,int count){
		return setItemTitle(dynamicDao.findReplysAll(page, count));
	}

	/**
	 * 查询所有已经删除的回复
	 */
	public  List<DynamicBean> findDeleteReply(int page, int count) {
		return setItemTitle(dynamicDao.findDeleteReplys(page, count));
	}
	/**
	 * 查询 所有 没有删除的  动态 
	 */
	public  List<DynamicBean> findDynamicAll(int page,int count){
		return setItemTitle(dynamicDao.findDynamicAll(page, count)); 
	}

	/**
	 * 查询所有已经删除的评论
	 */
	public List<DynamicBean> findDeleteDynamics(int page, int count) {
		return setItemTitle(dynamicDao.findDeleteDynamics(page, count));
	}

	/**
	 * 恢复已经删除的评论信息
	 */
	public boolean cancelDelete(String id) {
		GMDataChange.recordChange("通过广场评论ID恢复评论\t评论ID为",id);
		return dynamicDao.cancelDelete(id);
	}

	/**
	 * 根据评论ID删除该条评论
	 */
	public int delete(String id) {
		DynamicEntity DynamicEntity = dynamicDao.findByID(id);
		if (DynamicEntity == null) {
			return 0;
		}
		dynamicDao.setIsDelete(id); // 根据id删除评论
		GMDataChange.recordChange("通过广场评论ID删除评论\t评论ID为",id);
		return 1;
	}

	/**
	 * 查询所有动态总条数
	 */
	public long findCount() {
		return dynamicDao.findCount();
	}

	/**
	 * 查询删除的总条数
	 */
	public long deleteCount() {
		return dynamicDao.deleteCount();
	}

	// gm
	// 分界线------------------------------------------------------------------------------------------------
	
	/**
	 * 查找首页动态
	 * 
	 * @param page
	 * @return
	 */
	public DynamicListRes fiendHoneDynamic(int page) {
		int count = GameParamsCache.getGameParams_Json().getDynamicPageCount();
		Player player = PlayerContext.getPlayer();
	    PlayerTagsEntity playerTags=playerTagsService.load(player.getId());		
	    
		List<DynamicEntity> list =null;
		if(playerTags.getTags().size()>0){//如果玩家又tag  就按照tag查找
			list =dynamicDao.findDynamicsByTag(playerTags.getTags(), page, count);
		}
		else{//否则直接分页查找
			list =dynamicDao.findDynamics(page,count);
		}

		List<DynamicBean> dynamicBeans = toDynamicBeans(list);
		setIsPraiseState(dynamicBeans, player.getId());
		return new DynamicListRes(count, page, dynamicBeans);
	}
	
	private void setIsPraiseState(List<DynamicBean> dynamicBeans,long playerId){
		List<String> dynamicIds=new ArrayList<>();
		for(DynamicBean bean:dynamicBeans){
			dynamicIds.add(bean.getId());
		}
		List<DynamicLikeEntity>  likes=dynamicLikeService.findByDynamicIds(playerId, dynamicIds);
		for(DynamicBean bean:dynamicBeans){
			//查找是否点赞过
			for(DynamicLikeEntity like:likes){
				if(like.getPlayerId()==playerId){
					bean.setIsPraise(IsPraise.yes.ordinal());
					break;
				}
			}
		}
	}
	
	private List<DynamicBean> toDynamicBeans(List<DynamicEntity> list){
		List<DynamicBean> items = new ArrayList<>();
	    //最后回复的消息id
		List<String> lastReplyIds=new ArrayList<>();
		for (DynamicEntity obj : list) {
			items.add(new DynamicBean(obj));
			String lastReplyId = obj.getLastReplyId();
			if(lastReplyId==null||"".equals(lastReplyId)){
				continue;
			}
			lastReplyIds.add(lastReplyId);
		}
		//设置最后回复的消息
		if(lastReplyIds.size()>0){
			List<DynamicEntity> replys=dynamicDao.findByIds(lastReplyIds);
			for(DynamicEntity obj:replys){
				for(DynamicBean bean:items){
					if(obj.getId().equals(bean.getLastReplyId())){
						bean.setLastReply(new DynamicBean(obj));
					}
				}
			}
		}
		return items;		
	}
//	/**
//	 * @deprecated
//	 * 查找我的朋友圈动态
//	 * 
//	 * @param page
//	 * @return
//	 */
//	public DynamicListRes fiendFriendDynamic(int page) {
//		int count = GameParamsCache.getGameParams_Json().getDynamicPageCount();
//		Player player = PlayerContext.getPlayer();
//		// 获取我关注的朋友圈id
//		List<AttentionEntity> attentions = attentionDao.seachAttentions(player.getId(), page, 50);// 我关注的50人的
//		List<Long> friendIds = new ArrayList<>();
//		for (AttentionEntity att : attentions) {
//			friendIds.add(att.getToPlayerId());
//		}
//		friendIds.add(player.getId());
//
//		attentionService.seachMyAttentionPlayers(1);
//		List<DynamicEntity> list = dynamicDao.findDynamics(friendIds, page, count);
//		List<DynamicBean> items = new ArrayList<>();
//		for (DynamicEntity obj : list) {
//			items.add(new DynamicBean(obj));
//		}
//		return new DynamicListRes(count, page, items);
//	}

	/**
	 * 查找玩家的所有动态 信息
	 * 
	 * @param playerId
	 * @return
	 */
	public DynamicListRes findDynamics(long playerId, int page) {
		int count = GameParamsCache.getGameParams_Json().getDynamicPageCount();
		List<DynamicEntity> list = dynamicDao.findDynamics(playerId, page, count);
		List<DynamicBean> items = new ArrayList<>();
		for (DynamicEntity obj : list) {
			items.add(new DynamicBean(obj));
		}
		return new DynamicListRes(count, page, items);
	}

	/**
	 * 弹出禁言消息
	 * 
	 * @param player
	 */
	private void alertNoSpe(Player player) {
		long end = player.getNoSpeakTime();
		long currentTime = System.currentTimeMillis();
		if (end > currentTime) {
			throw new BusinessException("您已被禁言至： " + DateUtil.getdate_yyyy_MM_dd_HH_MM_SS(player.getNoSpeakTime()));
		}
	}

	private void checkRes(List<ResBean> resList, boolean isReply) {
		if (resList == null || resList.isEmpty()) {
			throw new BusinessException("参数为空");
		}
		int sendTextLimit = GameParamsCache.getGameParams_Json().getDynamicTextLimit();
		int replyTextLimit = GameParamsCache.getGameParams_Json().getDynamicReplyTextLimit();
		for (ResBean obj : resList) {
			if (obj == null) {
				throw new BusinessException("参数错误");
			}
			if (obj.getContent() == null || obj.getContent().isEmpty()) {
				throw new BusinessException("参数为空");
			}
			if (ResType.findByType(obj.getType()) == ResType.none) {
				throw new BusinessException("参数无法识别");
			}
			if (ResType.text.ordinal() == obj.getType()) {
				int limit = sendTextLimit;
				if (isReply) {
					limit = replyTextLimit;
				}
				if (obj.getContent().length() > limit) {
					throw new BusinessException("文字内容过长");
				}
			}
		}
	}

	/**
	 * 发送 动态
	 * @param resList
	 * 资源列表
	 */
	public void sendDynamic(List<ResBean> resList,List<Integer> tags) {
		Player player = PlayerContext.getPlayer();
		alertNoSpe(player);// 是否被禁言
		checkRes(resList, false);
		String id = UUID.randomUUID().toString().replace("-", "");
		String fromNickName = player.decodeName();
		long currentTime = System.currentTimeMillis();
		DynamicEntity obj = new DynamicEntity(id, "", 0, "", player.getId(), fromNickName, player.getAvatarUrl(),
				resList, currentTime, "", 0, 0, currentTime, ReadState.unread.ordinal(), IsDelete.none.ordinal(),
				tags,"");
		dynamicDao.save(obj);
		// 添加发表 动态数量
		playerDynamicDao.addSendDynamicNum(player.getId(), 1);
	}

	/**
	 * 回复评论(1级)
	 * 
	 * @param gameId
	 * @param chatId
	 * @param index
	 * @param content
	 */
	public void replyDynamic(String dynamicId, List<ResBean> resList) {
		if (dynamicId == null) {
			throw new BusinessException("参数错误");
		}
		Player player = PlayerContext.getPlayer();
		alertNoSpe(player);
		checkRes(resList, true);

		DynamicEntity reply = dynamicDao.findByID(dynamicId);
		if (reply == null) {
			throw new BusinessException("您回复的评论不存在");
		}
		if (reply.getFromPlayerId() == player.getId()) {
			throw new BusinessException("不能回复自己的动态");
		}		
		BlackListEntity toblackListEntity = blackListService.load(reply.getFromPlayerId());
		if (toblackListEntity.getBlackList().contains(player.getId())) {
			throw new BusinessException("对方设置了权限，不能进行评论哦~");
		}
		BlackListEntity fromblackListEntity = blackListService.load(player.getId());
		if (fromblackListEntity.getBlackList().contains(reply.getFromPlayerId())) {
			throw new BusinessException("你已将对方拉黑，不能进行评论");
		}
		String id = UUID.randomUUID().toString().replace("-", "");
		long currentTime = System.currentTimeMillis();
		String groupId=getGroupId(reply, id);
		String fromNickName = player.decodeName();
		String toReplyText="";
		if(!groupId.equals(reply.getId())){//回复的不是第一条评论
			for(ResBean res:reply.getResList()){
				if(res.getType()==ResType.text.ordinal()){
					toReplyText=res.getContent();
				} else {
					//taskService.playerScheduleUpdate(CommonCost.TackType.comment.ordinal(),1); //任务接口(任务类型,完成次数)
				}
			}
		}
		DynamicEntity obj = new DynamicEntity(id, reply.getId(), reply.getFromPlayerId(), reply.getFromNickName(),
				player.getId(), fromNickName, player.getAvatarUrl(), resList, currentTime,groupId, 0, 0,
				currentTime, ReadState.unread.ordinal(), IsDelete.none.ordinal(),reply.getTags(),toReplyText);
		dynamicDao.save(obj);
		// 更新一下被回复的消息的 最后回复更新时间
		dynamicDao.updateReplyInfo(reply.getId(), currentTime,obj.getId());
		// 添加 回复动态数量
		playerDynamicDao.addReplyDynamicNum(player.getId(), 1);
		systemService.sendDynamicReply(obj);

	}
	private String getGroupId(DynamicEntity reply, String id) {
		String groupId="";
		if(reply.getGroupId()==null||"".equals(reply.getGroupId())){//如果 回复的动态，组号就是自己的id
			groupId=id;
		}
		else{
			groupId=reply.getGroupId();
		}
		return groupId;
	}

	//// ====================================================================================

	/**
	 * 根据组号查询
	 * 
	 * @param groupId
	 * @param page
	 * @return
	 */
	public DynamicListRes findReplyByGroupId(String groupId, int page) {
		if (groupId == null || groupId.trim().equals("")) {
			throw new BusinessException("参数错误");
		}
		DynamicEntity groupDynamic = dynamicDao.findByID(groupId);
		if (groupDynamic == null) {
			throw new BusinessException("参数错误");
		}		 
		if (!groupDynamic.getId().equals(groupId)) {//不是第1级回复
			throw new BusinessException("不是评论信息，参数错误！");
		}
		int count = GameParamsCache.getGameParams_Json().getDynamicPageCount();
		List<DynamicEntity> replys = dynamicDao.findReplyByGroupId(groupId, page, count);
		// 设置点赞状态
		ArrayList<String> unreadIds = new ArrayList<>();
		List<DynamicBean> list = new ArrayList<>();

		Player player = PlayerContext.getPlayer();
		for (DynamicEntity obj : replys) {	
			list.add(new DynamicBean(obj));
			if (obj.getReadState() == ReadState.unread.ordinal()&&player.getId()==obj.getToPlayerId()) {
				unreadIds.add(obj.getId());
			}
		}
		if (unreadIds.size() > 0) {
			dynamicDao.setReadState(unreadIds);// 设置已读状态
		}
		checkAndSetPaiseState(list, player.getId());		
		return new DynamicListRes(count, page, list);
	}

	// 回复列表
	public DynamicListRes replyListByPage(String toDynamicId, int page) {
		if (toDynamicId == null || toDynamicId.trim().equals("")) {
			throw new BusinessException("参数错误");
		}
		DynamicEntity reply = dynamicDao.findByID(toDynamicId);
		if (reply == null) {
			throw new BusinessException("参数错误");
		}
		int count = GameParamsCache.getGameParams_Json().getDynamicPageCount();
		List<DynamicEntity> replys = dynamicDao.findReplayList(toDynamicId, page, count);
		// 设置点赞状态
		ArrayList<String> unreadIds = new ArrayList<>();
		List<DynamicBean> list = new ArrayList<>();
		Set<Long> playerIds = new HashSet<>();
		Map<Long, PlayerDynamicEntity> map = new HashMap<>();
		Player player = PlayerContext.getPlayer();
		for (DynamicEntity obj : replys) {
			list.add(new DynamicBean(obj));
			playerIds.add(obj.getFromPlayerId());
			if (obj.getReadState() == ReadState.unread.ordinal()&&player.getId()==obj.getToPlayerId()) {
				unreadIds.add(obj.getId());
			}
		}
		if (unreadIds.size() > 0) {
			dynamicDao.setReadState(unreadIds);// 设置已读状态+
		}		
		checkAndSetPaiseState(list, player.getId());
		List<PlayerDynamicEntity> playerDynamicEntities = playerDynamicDao.findByIds(playerIds);
		for (PlayerDynamicEntity playerDynamicEntity : playerDynamicEntities) {
			map.put(playerDynamicEntity.getPlayerId(), playerDynamicEntity);
		}
		for (DynamicBean dynamicBean : list) {
			if (map.get(Long.valueOf(dynamicBean.getFromPlayerId()))!= null) {
				dynamicBean.setAchievementTags(map.get(Long.valueOf(dynamicBean.getFromPlayerId())).getAchievementTags());
				dynamicBean.setAvatarFrame(map.get(Long.valueOf(dynamicBean.getFromPlayerId())).getAvatarFrame());
				dynamicBean.setItemTitle(map.get(Long.valueOf(dynamicBean.getFromPlayerId())).getItemTitle());
			}
		}
		return new DynamicListRes(count, page, list);
	}

	/**
	 * 查询 回复我的动态信息
	 * 
	 * @param page
	 * @return
	 */
	public DynamicListRes findToMeReply(int page) {
		Player player = PlayerContext.getPlayer();
		int count = GameParamsCache.getGameParams_Json().getDiscussPageCount();
		List<DynamicEntity> list = dynamicDao.myReplyListByPage(player.getId(), page, count);
		// 设置点赞状态
		List<DynamicBean> items = new ArrayList<DynamicBean>();
		List<String> unreadIds = new ArrayList<>();
		for (DynamicEntity obj : list) {
			items.add(new DynamicBean(obj));
			if (obj.getReadState() == ReadState.unread.ordinal()&&player.getId()==obj.getToPlayerId()) {
				unreadIds.add(obj.getId());
			}
		}
		if (unreadIds.size() > 0) {
			dynamicDao.setReadState(unreadIds);// 设置已读状态
		}
		checkAndSetPaiseState(items, player.getId());
		return new DynamicListRes(count, page, items);
	}

	/**
	 * 检查和设置 是否点赞状态
	 * 
	 * @param items
	 * @param playerId
	 */
	private void checkAndSetPaiseState(List<DynamicBean> items, long playerId) {
		List<String> discussIds = new ArrayList<>();
		for (DynamicBean obj : items) {
			discussIds.add(obj.getId());
		}
		List<String> likeDiscussIds = dynamicLikeService.findLikeDynamicIds(playerId, discussIds);
		for (DynamicBean bean : items) {
			if (likeDiscussIds.contains(bean.getId())) {
				bean.setIsPraise(IsPraise.yes.ordinal());
			}
		}
	}

	/**
	 * 查找一个未读回复消息
	 * 
	 * @param playerId
	 * @return
	 */
	public int getPlayerUnreadReplyState(long playerId) {
		if (dynamicDao.findOneUnreadReply(playerId) == null) {
			return 0;
		}
		return 1;
	}

	/**
	 * 获取我动态 根据
	 * 
	 * @param page
	 *            页数
	 * @return
	 */
	public DynamicListRes findMyDynamic(int page) {
		Player player = PlayerContext.getPlayer();
		return findDynamics(player.getId(), page);
	}
	
	/**
	 * 设置为神评
	 * @param dynamicId
	 * @return
	 */
	public boolean setIsBest(String dynamicId) {
		 GMDataChange.recordChange("通过广场评论ID设置神评\t广场评论ID为",dynamicId);
		 long currentTime=System.currentTimeMillis();
		 return dynamicDao.setIsBest(dynamicId, currentTime);
	}
	/**
	 * 取消神评
	 */
	public boolean removeIsBest(String dynamicId) { 
		GMDataChange.recordChange("通过广场评论ID取消神评\t广场评论ID为",dynamicId);
		return super.dynamicDao.removeIsBest(dynamicId);
	}
	/**
	 * 推荐社评
	 * @param dynamicId
	 */
	public void nominateBest(String dynamicId) {
		DynamicEntity entity=dynamicDao.findByID(dynamicId);
		if(entity==null){
			throw new BusinessException("参数错误");
		}
		nominateBestService.nominateBest(entity);
	}
	
	/**获取 动态信息*/
	public DynamicBean findDynamicInfo(String id) {
		DynamicEntity entity=dynamicDao.findByID(id);
		if(entity==null){
			throw new BusinessException("参数错误");
		}
		return new DynamicBean(entity);
	} 
	public int findUnreadReplyNum() {
		Player player=PlayerContext.getPlayer();
		long toPlayerId=player.getId();
		int count=(int)dynamicDao.findReplyDynamic(toPlayerId);
		return count;
	}
	
	/**
     * 用户通过标签查询广场内容 (注释内容在下一版需求时开放)
     */
	public DynamicListRes findByTagsDynamic(int page,int tags) {
		Player player = PlayerContext.getPlayer();
		int count = GameParamsCache.getGameParams_Json().getDynamicPageCount();
		List<DynamicEntity> dynamicEntityList = dynamicDao.findByTagsDynamic(page,count,tags);
//		Set<Long> playerIds = new HashSet<Long>();
//		Map<Long, PlayerDynamicEntity> map = new HashMap<>();
		List<DynamicBean> dynamicBeanList = new ArrayList<DynamicBean>(); 
		if(dynamicEntityList.size() > 0) {
			for (DynamicEntity dynamicEntity : dynamicEntityList) {
//				playerIds.add(dynamicEntity.getFromPlayerId());
				DynamicBean dynamicBean = new DynamicBean(dynamicEntity);
				dynamicBeanList.add(dynamicBean);
			}
			checkAndSetPaiseState(dynamicBeanList,player.getId());
		}
//		List<PlayerDynamicEntity> playerDynamicEntities = playerDynamicDao.findByIds(playerIds);
//		for (PlayerDynamicEntity playerDynamicEntity : playerDynamicEntities) {
//			map.put(playerDynamicEntity.getPlayerId(), playerDynamicEntity);
//		}
//		for (DynamicBean dynamicBean : dynamicBeanList) {
//			if (map.get(Long.valueOf(dynamicBean.getFromPlayerId()))!= null) {
//				dynamicBean.setAchievementTags(map.get(Long.valueOf(dynamicBean.getFromPlayerId())).getAchievementTags());
//				dynamicBean.setAvatarFrame(map.get(Long.valueOf(dynamicBean.getFromPlayerId())).getAvatarFrame());
//				dynamicBean.setItemTitle(map.get(Long.valueOf(dynamicBean.getFromPlayerId())).getItemTitle());
//			}		
//		}
		return new DynamicListRes(count,page,dynamicBeanList);
	}
}

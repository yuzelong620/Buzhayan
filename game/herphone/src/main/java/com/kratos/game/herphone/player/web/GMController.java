
package com.kratos.game.herphone.player.web;

import com.kratos.game.herphone.aop.PrePermissions;
import com.kratos.game.herphone.common.BaseController;
import com.kratos.game.herphone.config.GlobalData;
import com.kratos.game.herphone.player.bean.PlayerPage;
import com.kratos.game.herphone.player.message.ResPlayerProfile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.kratos.game.herphone.json.JsonCacheManager;
import java.util.Map;
import com.kratos.game.herphone.discuss.service.RecommendBestService;

@RestController
@RequestMapping("/gm")
@PrePermissions
public class GMController extends BaseController {

	@Autowired
	private GlobalData globalData;
	@Autowired
	private RecommendBestService recommendBestService;
	
	/**
	 * 设置为审核模式
	 */
	@GetMapping("/auditingMode/{flag}")
	public ResponseEntity<?> auditingMode(@PathVariable String flag) {
		globalData.update("auditingMode", Boolean.parseBoolean(flag));
		return new ResponseEntity<>(HttpStatus.OK);
	}

	/**
	 * 设置电量
	 */
	@PostMapping("/setPower")
	@PrePermissions
	public ResponseEntity<ResPlayerProfile> setPower(@RequestBody Map<String, Object> param) {
		return new ResponseEntity<>(playerService.gmSetPower((String) param.get("roleId"), (Integer) param.get("power")), HttpStatus.OK);
	}

	/**
	 * 设置电量
	 */
	@PostMapping("/addPower")
	@PrePermissions
	public ResponseEntity<ResPlayerProfile> addPower(@RequestBody Map<String, Object> param) {
		return new ResponseEntity<>(playerService.gmAddPower((String) param.get("roleId"), (Integer) param.get("power")), HttpStatus.OK);
	}

	/**
	 * 查询指定剧本的玩家数量
	 */
	@GetMapping("/getScenario")
	@Deprecated
	public ResponseEntity<?> getScenario() {
		return new ResponseEntity<>(playerService.getPlayerNum(), HttpStatus.OK);
	}

	/**
	 * 查询指定剧本的玩家平均分
	 */
	@GetMapping("/getAverageScore")
	@Deprecated
	public ResponseEntity<?> getAverageScore() {
		return new ResponseEntity<>(playerService.getAverageScore(), HttpStatus.OK);
	}

	/**
	 * 查询探索排行榜中绑定手机号码玩家
	 */
	@GetMapping("/getExploration")
	@Deprecated
	public ResponseEntity<?> getExploration(String start, String page) {
		PlayerPage playerPage = new PlayerPage(Integer.valueOf(start), Integer.valueOf(page));
		return new ResponseEntity<>(playerService.getExplorationPhone(playerPage), HttpStatus.OK);
	}

	/**
	 * 查询成就排行榜中绑定手机号码玩家
	 */
	@GetMapping("/getAchievement")
	@Deprecated
	public ResponseEntity<?> getAchievement(String start, String page) {
		PlayerPage playerPage = new PlayerPage(Integer.valueOf(start), Integer.valueOf(page));
		return new ResponseEntity<>(playerService.getAchievementPhone(playerPage), HttpStatus.OK);
	}

	/**
	 * 查询剧本信息
	 */
	@GetMapping("/getGameScore")
	public ResponseEntity<?> getGameScore() {
		return new ResponseEntity<>(gameScoreService.listGameScore(), HttpStatus.OK);
	}

	/**
	 * 根据昵称ID查找成就榜用户信息
	 */
	@GetMapping("/getByIdAchievement")
	@Deprecated
	public ResponseEntity<?> getByIdAchievement(String data) {
		return new ResponseEntity<>(playerService.getByIdAchievement(data), HttpStatus.OK);
	}

	/**
	 * 根据昵称查找成就榜用户信息
	 */
	@GetMapping("/getByNameAchievement")
	@Deprecated
	public ResponseEntity<?> getByNameAchievement(String start, String page, String data) {
		PlayerPage playerPage = new PlayerPage(Integer.valueOf(start), Integer.valueOf(page));
		return new ResponseEntity<>(playerService.getByNameAchievement(playerPage, data), HttpStatus.OK);
	}

	/**
	 * 根据手机号码查找成就用户信息
	 */
	@GetMapping("/getByPhoneAchievement")
	@Deprecated
	public ResponseEntity<?> getByPhoneAchievement(String phone) {
		return new ResponseEntity<>(playerService.getByPhoneAchievement(phone), HttpStatus.OK);
	}

	/**
	 * 根据昵称ID查找探索榜用户信息
	 */
	@GetMapping("/getByIdExploration")
	@Deprecated
	public ResponseEntity<?> getByIdExploration(String data) {
		return new ResponseEntity<>(playerService.getByIdExploration(data), HttpStatus.OK);
	}

	/**
	 * 根据昵称查找探索榜用户信息
	 */
	@GetMapping("/getByNameExploration")
	@Deprecated
	public ResponseEntity<?> getByNameExploration(String start, String page, String data) {
		PlayerPage playerPage = new PlayerPage(Integer.valueOf(start), Integer.valueOf(page));
		return new ResponseEntity<>(playerService.getByNameExploration(playerPage, data), HttpStatus.OK);
	}

	/**
	 * 根据手机号码查找探索榜用户信息
	 */
	@GetMapping("/getByPhoneExploration")
	@Deprecated
	public ResponseEntity<?> getByPhoneExploration(String phone) {
		return new ResponseEntity<>(playerService.getByPhoneExploration(phone), HttpStatus.OK);
	}

	@GetMapping("/reloadJson")
	public ResponseEntity<?> reloadJson() {
		JsonCacheManager.reloadAll();
		return new ResponseEntity<>("reloadJson  finished! 加载完毕", HttpStatus.OK);
	}

	/**
	 * 根据评论ID设置该评论为热评
	 */
	@GetMapping("/setGmIsHot/{id}")
	public ResponseEntity<?> setIsHot(@PathVariable String id) {
		return new ResponseEntity<>(discussService.setGmIsHot(id), HttpStatus.OK);
	}

	/**
	 * 根据评论信息ID删除对应评论
	 */
	@GetMapping("/removeDiscuss/{id}")
	public ResponseEntity<?> removeDiscuss(@PathVariable String id) {
		return new ResponseEntity<>(discussService.removeDiscuss(id), HttpStatus.OK);
	}

	/**
	 * 分页查询评论信息
	 */
	@GetMapping("/getDiscussAll")
	public ResponseEntity<?> getDiscussAll(String page, String count) {
		return new ResponseEntity<>(discussService.getDiscussAll(Integer.valueOf(page), Integer.valueOf(count)),
				HttpStatus.OK);
	}

	/**
	 * 通过ID设置护眼大队 0设置失败 1设置成功 2没有此玩家
	 */
	@GetMapping("/setByIdItemTitle/{roleId}")
	public ResponseEntity<?> setByIdItemTitle(@PathVariable String roleId) {
		return new ResponseEntity<>(playerDynamicService.setItemTitle(roleId), HttpStatus.OK);
	}

	/**
	 * 通过手机号设置护眼大队 0设置失败 1设置成功 2没有绑定手机号
	 */
	@GetMapping("/setByPhoneItemTitle/{phone}")
	public ResponseEntity<?> setByPhoneItemTitle(@PathVariable String phone) {
		return new ResponseEntity<>(playerDynamicService.setItemTitleByPhone(phone), HttpStatus.OK);
	}

	/**
	 * 查询护眼大队玩家
	 */
	@GetMapping("/getItemTitlePlayer")
	public ResponseEntity<?> getItemTitlePlayer(String page, String count) {
		return new ResponseEntity<>(playerDynamicService.getItemTitle(Integer.valueOf(page), Integer.valueOf(count)),HttpStatus.OK);
	}

	/**
	 * 举报玩家列表
	 */
	@GetMapping("/getListReportPlayer")
	public ResponseEntity<?> getListReportPlayer(String page, String count, String state) {
		return new ResponseEntity<>(reportPlayerService.listReportPlayer(Integer.valueOf(page), Integer.valueOf(count),Integer.valueOf(state)), HttpStatus.OK);
	}

	/**
	 * 举报评论列表
	 */
	@GetMapping("/getListReportInfo")
	public ResponseEntity<?> getListReportInfo(String page, String count, String state) {
		return new ResponseEntity<>(reportInfoService.listReportInfo(Integer.valueOf(page), Integer.valueOf(count), Integer.valueOf(state)),HttpStatus.OK);
	}

	/**
	 * 用户封号处理
	 */
	@GetMapping("/setBlockPlayer")
	public ResponseEntity<?> setBlockPlayer(String id, long playerId) {
		reportPlayerService.setBlockPlayer(id, playerId);
		illegalRecordService.addIllegalRecord(1, playerId,0);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	/**
	 * 评论用户封号处理
	 */
	@GetMapping("/setDiscussBlockPlayer")
	public ResponseEntity<?> setDiscussBlockPlayer(String id, long playerId) {
		reportInfoService.setBlockPlayer(id, playerId);
		illegalRecordService.addIllegalRecord(1, playerId,0);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	/**
	 * 用户禁言处理
	 */
	@GetMapping("/setNoSpeak")
	public ResponseEntity<?> setNoSpeak(String id, long playerId, long noSpeakTime) {
		reportPlayerService.setNoSpeak(id, playerId, noSpeakTime);
		illegalRecordService.addIllegalRecord(2, playerId,noSpeakTime);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	/**
	 * 评论禁言处理
	 */
	@GetMapping("/setDiscussNoSpeak")
	public ResponseEntity<?> setDiscussNoSpeak(String id, long playerId, long noSpeakTime) {
		reportInfoService.setNoSpeak(id, playerId, noSpeakTime);
		illegalRecordService.addIllegalRecord(2, playerId,noSpeakTime);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	/**
	 * 用户移除护眼大队
	 */
	@GetMapping("/removeReportPlayerToPlayer")
	public ResponseEntity<?> removeReportPlayerToPlayer(String id, long playerId) {
		reportPlayerService.removeReportPlayerToPlayer(id, playerId);
		return new ResponseEntity<>(true, HttpStatus.OK);
	}

	/**
	 * 评论移除护眼大队
	 */
	@GetMapping("/removeReportInfoToPlayer")
	public ResponseEntity<?> removeReportInfoToPlayer(String id, long playerId) {
		reportInfoService.removeReportInfoToPlayer(id, playerId);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	/**
	 * 删除用户举报信息
	 */
	@GetMapping("/removeReportPlayer/{id}")
	public ResponseEntity<?> removeReportPlayer(@PathVariable String id) {
		reportPlayerService.removeReportPlayer(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	/**
	 * 删除评论举报信息
	 */
	@GetMapping("/removeDiscussReportPlayer/{id}")
	public ResponseEntity<?> removeDiscussReportPlayer(@PathVariable String id) {
		reportInfoService.removeReportInfo(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	/**
	 * 系统获取全部玩家消息
	 */
	@GetMapping("/listPlayerMessge")
	public ResponseEntity<?> listPlayerMessge(int page, int count) {
		return new ResponseEntity<>(systemMessageLastService.listSystemMessageLast(page, count), HttpStatus.OK);
	}

	/**
	 * 系统通过id发送内容
	 */
	@GetMapping("/systemSendMessgae")
	public ResponseEntity<?> systemSendMessgae(String content, int contentType, long... playerIds) {
		systemMessgaeService.systemSendMessgae(content, contentType, playerIds);
		return new ResponseEntity<>(true, HttpStatus.OK);
	}

	/**
	 * 系统通过手机发送内容
	 */
	@GetMapping("/systemSendMessgaeByPhone")
	public ResponseEntity<?> systemSendMessgaeByPhone(String content, int contentType, String phone) {
		return new ResponseEntity<>(systemMessgaeService.systemSendMessgaeByPhone(content, contentType, phone),HttpStatus.OK);
	}

	/**
	 * 系统通过角色Id发送内容
	 */
	@GetMapping("/systemSendMessgaeByRoleId")
	public ResponseEntity<?> systemSendMessgaeByRoleId(String content, int contentType, String RoleId) {
		return new ResponseEntity<>(systemMessgaeService.systemSendMessgaeByRoleId(content, contentType, RoleId),HttpStatus.OK);
	}

	/**
	 * 获取指定玩家的历史聊天记录
	 */
	@GetMapping("/getPlayerMessgeByPlayerId/{playerId}")
	public ResponseEntity<?> getPlayerMessgeByPlayerId(@PathVariable long playerId) {
		return new ResponseEntity<>(systemMessgaeService.getPlayerMessgeByPlayerId(playerId), HttpStatus.OK);
	}

	/**
	 * 查询所有已删除的评论信息
	 */
	@GetMapping("/getRecoverDiscussAll")
	public ResponseEntity<?> getRecoverDiscussAll(int page, int count) {
		return new ResponseEntity<>(discussService.getRecoverDiscussAll(page, count), HttpStatus.OK);
	}

	/**
	 * 恢复已删除的评论信息
	 */
	@GetMapping("/recoverComment/{id}")
	public ResponseEntity<?> recoverComment(@PathVariable String id) {
		return new ResponseEntity<>(discussService.recoverComment(id), HttpStatus.OK);
	}

	/**
	 * 神评列表
	 */
	@GetMapping("/getDeityDiscussAll")
	public ResponseEntity<?> getDeityDiscussAll(int page, int count) {
		return new ResponseEntity<>(discussService.getDeityDiscussAll(page, count), HttpStatus.OK);
	}

	/**
	 * 通过移除神评
	 */
	@GetMapping("/removeDeity/{id}")
	public ResponseEntity<?> removeDeity(@PathVariable String id) {
		return new ResponseEntity<>(discussService.removeDeity(id), HttpStatus.OK);
	}

	/**
	 * 查询评论总条数
	 */
	@GetMapping("/getCommentCount")
	public ResponseEntity<?> getCommentCount() {
		return new ResponseEntity<>(discussService.getCommentCount(), HttpStatus.OK);
	}

	/**
	 * 查询删除的评论总条数
	 */
	@GetMapping("/getRecoverCommentCount")
	public ResponseEntity<?> getRecoverCommentCount() {
		return new ResponseEntity<>(discussService.getRecoverCommentCount(), HttpStatus.OK);
	}

	/**
	 * 查询神评总条数
	 */
	@GetMapping("/getDeityCommentCount")
	public ResponseEntity<?> getDeityCommentCount() {
		return new ResponseEntity<>(discussService.getDeityCommentCount(), HttpStatus.OK);
	}

	/**
	 * 查询护眼大队玩家总人数
	 */
	@GetMapping("/getItemTitleCount")
	public ResponseEntity<?> getItemTitleCount() {
		return new ResponseEntity<>(playerDynamicService.getItemTitleCount(), HttpStatus.OK);
	}

	/**
	 * 查询举报评论信息总条数
	 */
	@GetMapping("/getReportDiscussCount")
	public ResponseEntity<?> getReportDiscussCount() {
		return new ResponseEntity<>(reportInfoService.getReportDiscussCount(), HttpStatus.OK);
	}

	/**
	 * 查询举报玩家信息总条数
	 */
	@GetMapping("/getReportPlayerCount")
	public ResponseEntity<?> getReportPlayerCount() {
		return new ResponseEntity<>(reportPlayerService.getReportPlayerCount(), HttpStatus.OK);
	}

	/**
	 * 查询历史通知记录总条数
	 */
	@GetMapping("/getInforCount")
	public ResponseEntity<?> getInforCount() {
		return new ResponseEntity<>(systemMessageLastService.getInforCount(), HttpStatus.OK);
	}

	/**
	 * 查询探索排行榜总人数
	 */
	@GetMapping("/getPlayerCount")
	@Deprecated
	public ResponseEntity<?> getPlayerCount() {
		return new ResponseEntity<>(playerService.getPlayerCount(), HttpStatus.OK);
	}

	/**
	 * 查询指定字段玩家总人数
	 */
	@GetMapping("/getAssignPlayerCount")
	public ResponseEntity<?> getAssignPlayerCount(String fields, String data) {
		return new ResponseEntity<>(playerService.getAssignPlayerCount(fields, data), HttpStatus.OK);
	}

	/**
	 * 查询被举报玩家是否成为护眼大队
	 */
	@GetMapping("/getToPlayerIdentity/{toPlayerId}")
	public ResponseEntity<?> getToPlayerIdentity(@PathVariable long toPlayerId) {
		return new ResponseEntity<>(playerDynamicService.getToPlayerIdentity(toPlayerId), HttpStatus.OK);
	}

	/**
	 * 查询所有推荐评论表
	 */
	@GetMapping("/findByUndeal")
	public ResponseEntity<?> findByUndeal(int page, int count) {
		return new ResponseEntity<>(recommendBestService.findByUndeal(page, count), HttpStatus.OK);
	}

	/**
	 * 推荐评论设置成已处理
	 */
	@GetMapping("/recommendBestDeal")
	public ResponseEntity<?> recommendBestDeal(String discussId) {
		recommendBestService.recommendBestDeal(discussId);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	/**
	 * 推荐评论设置成搁置
	 */
	@GetMapping("/recommendBestHold")
	public ResponseEntity<?> recommendBestShelve(String discussId) {
		recommendBestService.recommendBestHold(discussId);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	/**
	 * 推荐评论设置成未处理状态
	 */
	@GetMapping("/ecommendBestUndeal")
	public ResponseEntity<?> ecommendBestUndeal(String discussId) {
		recommendBestService.recommendBestUndeal(discussId);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	/**
	 * 封号
	 */
	@GetMapping("/getisblock/{playerid}")
	public ResponseEntity<?> getIsBlock(@PathVariable long playerid) {
		illegalRecordService.addIllegalRecord(1, playerid,0);
		return new ResponseEntity<>(playerService.setIsBlock(playerid), HttpStatus.OK);
	}

	/**
	 * 禁言
	 */
	@GetMapping("/getNoSpeak")
	@Deprecated
	public ResponseEntity<?> getNoSpeak(long playerid, long time) {
		Long playerId = Long.valueOf(playerid);
		long times = time * 3600000;// 毫秒
		illegalRecordService.addIllegalRecord(2, playerId,times);
		return new ResponseEntity<>(playerService.setNoSpeak(playerId, times), HttpStatus.OK);
	}

	/**
	 * 取消封号
	 */
	@GetMapping("/callisblock/{playerid}")
	public ResponseEntity<?> getcallIsBlock(@PathVariable long playerid) {
		return new ResponseEntity<>(playerService.callIsBlock(playerid), HttpStatus.OK);
	}

	/**
	 * 取消禁言
	 */
	@PostMapping("/callnospeak")
	public ResponseEntity<?> getcallNoIsSpeak(long playerId) {
		return new ResponseEntity<>(playerService.callNoSpeak(playerId), HttpStatus.OK);

	}

	/**
	 * 根据ID查询信息并展示
	 */
	@GetMapping("/selectByRoleId/{roleId}")
	@Deprecated
	public ResponseEntity<?> selectByRoleId(@PathVariable String roleId) {
		return new ResponseEntity<>(playerService.selectByRoleId(roleId), HttpStatus.OK);
	}

	/**
	 * 根据手机号查询信息并展示
	 */
	@GetMapping("/selectByphone/{phone}")
	@Deprecated
	public ResponseEntity<?> selectByphone(@PathVariable String phone) {
		return new ResponseEntity<>(playerService.selectByphone(phone), HttpStatus.OK);
	}

	/**
	 * 根据昵称查询玩家信息
	 */

	@GetMapping("/selectBynickname")
	@Deprecated
	public ResponseEntity<?> selectBynickname(String nickname,String start, String page) {
		PlayerPage payerpage = new PlayerPage(Integer.valueOf(start), Integer.valueOf(page));
		return new ResponseEntity<>(playerService.selectBynickname(nickname,payerpage), HttpStatus.OK);
	}

	/**
	 * 查询推荐评论总条数
	 */
	@GetMapping("/recommendCount")
	public ResponseEntity<?> findRecommendBestCount() {
		return new ResponseEntity<>(recommendBestService.findCount(), HttpStatus.OK);
	}

	/**
	 * 查询搁置状态的评论
	 */
	@GetMapping("/findByhold")
	public ResponseEntity<?> findByhold(int page, int count) {
		return new ResponseEntity<>(recommendBestService.findByhold(page, count), HttpStatus.OK);

	}

	/**
	 * 查询搁置状态的评论总条数
	 */
	@GetMapping("/findcountByhold")
	public ResponseEntity<?> findcountByhold() {
		return new ResponseEntity<>(recommendBestService.findcountByhold(), HttpStatus.OK);

	}

	/**
	 * 查询所有评论内容
	 */
	@GetMapping("/findAll")
	@Deprecated
	public ResponseEntity<?> findall(int start, int page) {
		PlayerPage payerpage = new PlayerPage(start,page);
		return new ResponseEntity<>(playerService.findall(payerpage), HttpStatus.OK);
	}

	/**
	 * 查询违规玩家信息
	 */
	@GetMapping("/findFoul")
	@Deprecated
	public ResponseEntity<?> findFoul(String start, String page) {
		PlayerPage payerpage = new PlayerPage(Integer.valueOf(start), Integer.valueOf(page));
		return new ResponseEntity<>(playerService.findFoul(payerpage), HttpStatus.OK);
	}

	/**
	 * 查询护眼大队审核玩家列表
	 */
	@GetMapping("/getTitleAuditPlayer")
	public ResponseEntity<?> getTitleAuditPlayer(int page, int count) {
		return new ResponseEntity<>(statisticalPlayerService.listPlayer(page, count), HttpStatus.OK);
	}

	/**
	 * 通过ID移除护眼大队
	 */
	@GetMapping("/removeProTitle/{playerId}")
	public ResponseEntity<?> removeProTitle(@PathVariable long playerId) {
		playerDynamicService.removeProTitle(playerId);
		return new ResponseEntity<>(true, HttpStatus.OK);
	}

	/**
	 * 查询指定时间护眼大队玩家列表
	 */
	@GetMapping("/getByTimeAuditPlayer")
	public ResponseEntity<?> getByTimeAuditPlayer(int time, int page, int count) {
		return new ResponseEntity<>(statisticalEyeshieldPlayerService.listPlayerByDate(time, page, count), HttpStatus.OK);
	}

	/**
	 * 查询指定时间护眼大队玩家列表总数
	 */
	@GetMapping("/getByTimeAuditCount/{time}")
	public ResponseEntity<?> getByTimeAuditCount(@PathVariable int time) {
		return new ResponseEntity<>(statisticalPlayerService.assignTimeCount(time), HttpStatus.OK);
	}

	/**
	 * 指定时间查询护眼大队审核玩家列表
	 */
	@GetMapping("/getEyeTitleAuditPlayer")
	public ResponseEntity<?> getEyeTitleAuditPlayer(int time, int page, int count) {
		return new ResponseEntity<>(statisticalPlayerService.listPlayerByDate(time, page, count),
				HttpStatus.OK);
	}

	/**
	 * 查询指定时间护眼大队审核玩家列表总数
	 */
	@GetMapping("/assignEyeTimeCount/{time}")
	public ResponseEntity<?> assignEyeTimeCount(@PathVariable int time) {
		return new ResponseEntity<>(statisticalPlayerService.assignTimeCount(time), HttpStatus.OK);
	}

	/**
	 * 动态评论管理
	 */
	@GetMapping("/findDynamicAll")
	public ResponseEntity<?> findDynamicAll(int page, int count) {
		return new ResponseEntity<>(dynamicService.findDynamicAll(page, count), HttpStatus.OK);
	}

	/**
	 * 根据ID删除动态评论
	 */
	@GetMapping("/deleteByIdDynamic/{id}")
	public ResponseEntity<?> deleteByIdDynamic(@PathVariable String id) {
		return new ResponseEntity<>(dynamicService.delete(id), HttpStatus.OK);
	}

	/**
	 * 查询动态评论总条数
	 */
	@GetMapping("/findDynamicCount")
	public ResponseEntity<?> findDynamicCount() {
		return new ResponseEntity<>(dynamicService.findCount(), HttpStatus.OK);
	}

	/**
	 * 查询所有已删除的动态评论
	 */
	@GetMapping("/findDeleteDynamics")
	public ResponseEntity<?> findDeleteDynamics(int page, int count) {
		return new ResponseEntity<>(dynamicService.findDeleteDynamics(page, count), HttpStatus.OK);
	}

	/**
	 * 查询删除的动态评论总条数
	 */
	@GetMapping("/findDeleteDynamicCount")
	public ResponseEntity<?> findDeleteDynamicCount() {
		return new ResponseEntity<>(dynamicService.deleteCount(), HttpStatus.OK);
	}

	/**
	* 恢复已经删除的广场评论信息
	 */
	@GetMapping("/cancelDeleteDynamic/{id}")
	public ResponseEntity<?> cancelDeleteDynamic(@PathVariable String id) {
		return new ResponseEntity<>(dynamicService.cancelDelete(id), HttpStatus.OK);
	}

	/**
	 * 查询粉圈评论总条数
	 */
	@GetMapping("/fansCommentFindCount")
	public ResponseEntity<?> fansCommentFindCount() {
		return new ResponseEntity<>(fandomService.findCount(), HttpStatus.OK);
	}

	/**
	 * 查询粉圈所有评论
	 */
	@GetMapping("/fansCommentFindList")
	public ResponseEntity<?> fansCommentFindList(int page, int count) {
		return new ResponseEntity<>(fandomService.findList(page, count), HttpStatus.OK);
	}

	/**
	 * 通过ID删除指定粉圈评论
	 */
	@GetMapping("/deleteByIdfansComment/{id}")
	public ResponseEntity<?> deleteByIdfansComment(@PathVariable String id) {
		return new ResponseEntity<>(fandomService.delete(id), HttpStatus.OK);
	}

	/**
	 * 查询粉圈删除的所有评论
	 */
	@GetMapping("/findDeleteFindList")
	public ResponseEntity<?> findDeleteFindList(int page, int count) {
		return new ResponseEntity<>(fandomService.findDeleteList(page, count), HttpStatus.OK);
	}

	/**
	 * 查询粉圈删除的评论总条数
	 */
	@GetMapping("/fansDeleteFindCount")
	public ResponseEntity<?> fansDeleteFindCount() {
		return new ResponseEntity<>(fandomService.deleteCount(), HttpStatus.OK);
	}

	/**
	 * 通过ID恢复指定粉圈评论
	 */
	@GetMapping("/fansCancelDelete/{id}")
	public ResponseEntity<?> fansCancelDelete(@PathVariable String id) {
		return new ResponseEntity<>(fandomService.cancelDelete(id), HttpStatus.OK);
	}

	/**
	 * 通过ID禁言指定时长
	 */
	@GetMapping("/setByIdNoSpeak")
	public ResponseEntity<?> setByIdNoSpeak(long playerId, long time) {
		illegalRecordService.addIllegalRecord(2, playerId,time);
		return new ResponseEntity<>(playerService.setNoSpeak(playerId, time), HttpStatus.OK);
	}

	/**
	 * 系统发送消息给所有玩家
	 */
	@GetMapping("/sendMessagePlayerAll")
	public ResponseEntity<?> sendMessagePlayerAll(String content, int contentType) {
		systemMessgaeService.systemSendAnnouncement(content, contentType);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	/**
	 * 通过推荐评论ID移除神评并设置状态为未处理
	 */
	@GetMapping("/setRecommendUtd")
	public ResponseEntity<?> setRecommendUtd(String discussId) {
		recommendBestService.setRecommendUtd(discussId);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	/**
	 * 根据ID查询护眼大队审核列表玩家信息
	 */
	@GetMapping("/findByIdTitleAudit")
	public ResponseEntity<?> findByIdTitleAudit(String roldId,int page,int count) {
		return new ResponseEntity<>(statisticalPlayerService.findByIdTitleAudit(roldId,page,count), HttpStatus.OK);
	}
	
	/**
	 * 查询指定ID的护眼大队审核列表的数据总数
	 */
	@GetMapping("/findByIdTitleAuditCount")
	public ResponseEntity<?> findByIdTitleAuditCount(String roldId) {
		return new ResponseEntity<>(statisticalPlayerService.findByIdTitleAuditCount(roldId), HttpStatus.OK);
	}
	
	/**
	 * 根据ID和时间查询护眼大队玩家审核玩家信息
	 */
	@GetMapping("/findByIdAndTimeAudit")
	public ResponseEntity<?> findByIdAndTimeAudit(int page,int count,String roleId,int date) {
		return new ResponseEntity<>(statisticalPlayerService.findByIdAndTimeAudit(roleId,date), HttpStatus.OK);
	}
	
	/**
	 * 指定ID查询护眼大队玩家信息变化
	 */
	@GetMapping("/getByIdAuditPlayer")
	public ResponseEntity<?> getByIdAuditPlayer(String roleId, int page, int count) {
		return new ResponseEntity<>(statisticalEyeshieldPlayerService.getByIdAuditPlayer(roleId, page, count), HttpStatus.OK);
	}
	
	/**
	 * 指定ID查询护眼大队玩家信息变化总数
	 */
	@GetMapping("/getByIdAuditPlayerCount")
	public ResponseEntity<?> getByIdAuditPlayerCount(String roleId) {
		return new ResponseEntity<>(statisticalEyeshieldPlayerService.getByIdAuditPlayerCount(roleId), HttpStatus.OK);
	}
	
	/**
	 * 根据ID和时间查询护眼大队玩家信息
	 */
	@GetMapping("/findByIdAndTimeTitle")
	public ResponseEntity<?> findByIdAndTimeTitle(String roleId,int date) {
		return new ResponseEntity<>(statisticalEyeshieldPlayerService.findByIdAndTimeTitle(roleId,date), HttpStatus.OK);
	}
	
	/**
	 * 根据ID设置动态评论神评
	 */
	@GetMapping("/setByIdDynamicIsBest")
	public ResponseEntity<?> setByIdDynamicIsBest(String dynamicId){
		return new ResponseEntity<>(dynamicService.setIsBest(dynamicId),HttpStatus.OK);
	}
	
	/**
	 * 根据ID取消动态评论神评
	 */
	@GetMapping("/removeByIdDynamicIsBest")
	public ResponseEntity<?> removeByIdDynamicIsBest(String dynamicId){
		return new ResponseEntity<>(dynamicService.removeIsBest(dynamicId),HttpStatus.OK);
	}
	
	/**
	 * 动态评论推荐
	 */
	@GetMapping("/dynamicRecommend")
	public ResponseEntity<?> dynamicRecommend(int page,int count){
		return new ResponseEntity<>(nominateBestService.findByUndeal(page, count),HttpStatus.OK);
	}
	
	/**
	 * 动态评论推荐总数
	 */
	@GetMapping("/dynamicRecommendCount")
	public ResponseEntity<?> dynamicRecommendCount(){
		return new ResponseEntity<>(nominateBestService.findCount(),HttpStatus.OK);
	}
	
	/**
	 * 动态评论推荐设为神评
	 */
	@GetMapping("/setDynamicIsBest")
	public ResponseEntity<?> setDynamicIsBest(String dynamicId){
		nominateBestService.recommendBestDeal(dynamicId);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	/**
	 * 动态评论推荐取消神评
	 */
	@GetMapping("/removeDynamicIsBest")
	public ResponseEntity<?> removeDynamicIsBest(String dynamicId){
		nominateBestService.recommendBestUndeal(dynamicId);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	/**
	 * 查询护眼先锋列表
	 */
	@GetMapping("/pioneerList")
	public ResponseEntity<?> pioneerList(int page,int count){
		return new ResponseEntity<>(pioneerService.listPioneer(page, count),HttpStatus.OK);
	}
	
	/**
	 * 查询护眼先锋列表总人数
	 */
	@GetMapping("/pioneerCount")
	public ResponseEntity<?> pioneerCount(){
		return new ResponseEntity<>(pioneerService.pioneerCount(),HttpStatus.OK);
	}
	
	/**
	 * 根据ID设置护眼先锋
	 */
	@GetMapping("/setByIdPioneer")
	public ResponseEntity<?> setByIdPioneer(String roleId){
		pioneerService.addPioneerByRoleId(roleId);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	/**
	 * 通过手机号码设置护眼先锋
	 */
	@GetMapping("/setByPhonePioneer")
	public ResponseEntity<?> setByPhonePioneer(String phone){
		pioneerService.addPioneerByPhone(phone);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	/**
	 * 通过ID解除护眼先锋
	 */
	@GetMapping("/relievePioneerById")
	public ResponseEntity<?> relievePioneerById(long playerId){
		pioneerService.removePioneer(playerId);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	/**
	 * 查询护眼先锋审核列表
	 */
	@GetMapping("/pioneerAuditAll")
	public ResponseEntity<?> pioneerAuditAll(int page){
		return new ResponseEntity<>(reportInfoDistinctService.listResReportInfoDistinct(page),HttpStatus.OK);
	}
	
	/**
	 * 查询护眼先锋审核总数
	 */
	@GetMapping("/pioneerReportCount")
	public ResponseEntity<?> pioneerReportCount(){
		return new ResponseEntity<>(reportInfoDistinctService.pioneerReportCount(),HttpStatus.OK);
	}
	
	/**
	 * 通过评论ID和类型查询玩家ID和昵称等信息
	 */
	@GetMapping("/getByDiscussIdAndTypeData")
	public ResponseEntity<?> getByDiscussIdAndTypeData(String discussId,int type){
		return new ResponseEntity<>(reportInfoDistinctService.getReportPlayer(discussId,type),HttpStatus.OK);
	}
	
	/**
	 * 通过ID删除护眼先锋审核信息
	 */
	@GetMapping("/removeByIdDiscuss")
	public ResponseEntity<?> removeByIdDiscuss(String id){
		reportInfoDistinctService.removeDiscuss(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	/**
	 * 根据ID查询护眼先锋信息
	 */
	@GetMapping("/getPioneerByRoleId")
	public ResponseEntity<?> getPioneerByRoleId(String id){
		return new ResponseEntity<>(pioneerService.getPioneerByRoleId(id),HttpStatus.OK);
	}
	
	/**
	 * 根据时间来查询护眼先锋信息
	 */
	@GetMapping("/getPioneerBytimeAll")
	public ResponseEntity<?> getPioneerBytimeAll(int date,int page,int count){
		return new ResponseEntity<>(statisticalPioneerService.listStatisticalPioneerBytime(date,page,count),HttpStatus.OK);
	}
	
	/**
	 * 根据时间查询护眼先锋信息总数
	 */
	@GetMapping("/getByTimePioneerCount")
	public ResponseEntity<?> getByTimePioneerCount(int date){
		return new ResponseEntity<>(statisticalPioneerService.getByTimePioneerCount(date),HttpStatus.OK);
	}
	
	/**
	 * 根据时间和ID查询护眼先锋信息
	 */
	@GetMapping("/getStatisticalPioneer")
	public ResponseEntity<?> getStatisticalPioneer(String roleId,int date){
		return new ResponseEntity<>(statisticalPioneerService.getStatisticalPioneer(roleId,date),HttpStatus.OK);
	}

	
	/**
	 * 查询某个昵称的总条数
	 * */
	@GetMapping("/getNicknamecount")
	@Deprecated
	public ResponseEntity<?>getnicknamecount(String nickname){
		return new ResponseEntity<>(playerService.findnicknamecount(nickname),HttpStatus.OK);
	}
	
	/**
	 * 查询数据库总记录数
	 * */
	@GetMapping("getdbcount")
	public ResponseEntity<?>getdbcount(){
		return new ResponseEntity<>(playerService.getPlayerCount(),HttpStatus.OK);
	}
	
	/**
	 * 查询数据库违规总数
	 * */
	@GetMapping("/badcount")
	@Deprecated
	public ResponseEntity<?>getbadcount(){
		return new ResponseEntity<>(playerService.badcount(),HttpStatus.OK);
	}
	
	/**
	 * 根据ID获取指定玩家违规记录
	 */
	@GetMapping("/getIllegalRecord")
	public ResponseEntity<?> getIllegalRecord(long playerId,int page,int count){
		return new ResponseEntity<>(illegalRecordService.getIllegalRecord(playerId,page,count),HttpStatus.OK);
	}
	
	/**
	 * 查询指定玩家违规记录总数
	 */
	@GetMapping("/getIllegalRecordCount")
	public ResponseEntity<?> getIllegalRecordCount(long playerId){
		return new ResponseEntity<>(illegalRecordService.getIllegalRecordCount(playerId),HttpStatus.OK);
	}
	
	/**
	 * 查询已绑定手机号机器人玩家的剧本信息
	 */
	@GetMapping("/getHavePhone")
	public ResponseEntity<?> getHavePhone(){
		return new ResponseEntity<>(gameDisposeService.getHavePhone(),HttpStatus.OK);
	}
	
	/**
	 * 查询未绑定手机号机器人玩家的剧本信息
	 */
	@GetMapping("/getNotHavePhone")
	public ResponseEntity<?> getNotHavePhone(){
		return new ResponseEntity<>(gameDisposeService.getNotHavePhone(),HttpStatus.OK);
	}
	
	/**
	 * 为机器人绑定手机号
	 */
	@GetMapping("/gmAddPhone")
	public ResponseEntity<?> gmAddPhone(String phone,long playerId){
		userService.gmAddPhone(phone,playerId);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	/**
	 * 删除机器人绑定的手机号
	 */
	@GetMapping("/gmDeletePhone")
	public ResponseEntity<?> gmDeletePhone(long playerId){
		userService.gmDeletePhone(playerId);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	/**
	 * 修改机器人绑定的手机号
	 */
	@GetMapping("/gmUpdatePhone")
	public ResponseEntity<?> gmUpdatePhone(long playerId,String phone){
		userService.gmUpdatePhone(playerId,phone);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	/**
	 * 举报评论 删除被举报的评论
	 */
	@GetMapping("/dealReportInfo")
	public ResponseEntity<?> dealReportInfo(String id){
		reportInfoService.dealReportInfo(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	/**
	 * 查询所有玩家信息
	 */
	@GetMapping("/getPlayerAllData")
	public ResponseEntity<?> getPlayerAllData(int page,int count){
		return new ResponseEntity<>(playerDynamicService.getPlayerAllData(page,count),HttpStatus.OK);
	}
	
	/**
	 * 查询所有玩家总数
	 */
	@GetMapping("/findPlayerCount")
	public ResponseEntity<?> findPlayerCount(){
		return new ResponseEntity<>(playerDynamicService.findPlayerCount(),HttpStatus.OK);
	}
	
	/**
	 * 根据ID查询玩家信息
	 */
	@GetMapping("/findByIdPlayerData")
	public ResponseEntity<?> findByIdPlayerData(String roleId){
		return new ResponseEntity<>(playerDynamicService.findByIdPlayerData(roleId),HttpStatus.OK);
	}
	
	/**
	 * 根据手机号查询玩家信息
	 */
	@GetMapping("/findByPhonePlayerData")
	public ResponseEntity<?> findByPhonePlayerData(String phone){
		return new ResponseEntity<>(playerDynamicService.findByPhonePlayerData(phone),HttpStatus.OK);
	}
	
	/**
	 * 查询指定昵称的玩家信息
	 */
	@GetMapping("/findByNamePlayerData")
	public ResponseEntity<?> findByNamePlayerData(String nickName,int page,int count){
		return new ResponseEntity<>(playerDynamicService.findByNamePlayerData(nickName,page,count),HttpStatus.OK);
	}
	
	/**
	 * 查询指定昵称的玩家总数
	 */
	@GetMapping("/findByNamePlayerCount")
	public ResponseEntity<?> findByNamePlayerCount(String nickName){
		return new ResponseEntity<>(playerDynamicService.findByNamePlayerCount(nickName),HttpStatus.OK);
	}
	
	/**
	 * 查询当前所有违规人员
	 */
	@GetMapping("/findAtFoulPlayAll")
	public ResponseEntity<?> findAtFoulPlayAll(int page,int count){
		return new ResponseEntity<>(playerDynamicService.findAtFoulPlayAll(page,count),HttpStatus.OK);
	}
	
	/**
	 * 查询当前违规人员总数
	 */
	@GetMapping("/findAtFoulPlayCount")
	public ResponseEntity<?> findAtFoulPlayCount(){
		return new ResponseEntity<>(playerDynamicService.findAtFoulPlayCount(),HttpStatus.OK);
	}
}

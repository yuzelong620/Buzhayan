package com.kratos.game.herphone.common;

import org.springframework.beans.factory.annotation.Autowired;

import com.kratos.game.herphone.achievement.service.AchievementService;
import com.kratos.game.herphone.attention.service.AttentionService;
import com.kratos.game.herphone.badge.service.BadgeService;
import com.kratos.game.herphone.bag.service.BagService;
import com.kratos.game.herphone.blackList.service.BlackListService;
import com.kratos.game.herphone.box.service.BoxService;
import com.kratos.game.herphone.clue.service.PlayerClueDataService;
import com.kratos.game.herphone.clue.service.PlayerClueService;
import com.kratos.game.herphone.discuss.service.DiscussLikeService;
import com.kratos.game.herphone.discuss.service.DiscussOptionService;
import com.kratos.game.herphone.discuss.service.DiscussService;
import com.kratos.game.herphone.discuss.service.RecommendBestService;
import com.kratos.game.herphone.dynamic.service.DynamicLikeService;
import com.kratos.game.herphone.dynamic.service.DynamicService;
import com.kratos.game.herphone.dynamic.service.NominateBestService;
import com.kratos.game.herphone.dynamic.service.PlayerTagsService;
import com.kratos.game.herphone.fandom.service.FandomLikeService;
import com.kratos.game.herphone.fandom.service.FandomService;
import com.kratos.game.herphone.game.chose.service.ChosenOptionService;
import com.kratos.game.herphone.game.chose.service.PlayerExplorationService;
import com.kratos.game.herphone.gameDispose.service.GameDisposeService;
import com.kratos.game.herphone.gameHistory.service.GameHistoryService;
import com.kratos.game.herphone.gameOver.service.GameOverService;
import com.kratos.game.herphone.gamemanager.service.AdminUserService;
import com.kratos.game.herphone.gamemanager.service.GameScoreService;
import com.kratos.game.herphone.gift.service.GiftService;
import com.kratos.game.herphone.helperMessage.service.HelperMessageService;
import com.kratos.game.herphone.homePage.service.HomePageService;
import com.kratos.game.herphone.illegal.service.IllegalRecordService;
import com.kratos.game.herphone.intimacy.service.IntimacyService;
import com.kratos.game.herphone.message.service.MessageFirstService;
import com.kratos.game.herphone.message.service.MessageService;
import com.kratos.game.herphone.order.service.OrderService;
import com.kratos.game.herphone.pioneer.service.PioneerService;
import com.kratos.game.herphone.player.service.PlayerService;
import com.kratos.game.herphone.player.service.PlayerServiceImpl;
import com.kratos.game.herphone.intimacy.service.NoteService;
import com.kratos.game.herphone.playerDynamic.service.PlayerDynamicService;
import com.kratos.game.herphone.playerOnline.service.PlayerLoginTimeService;
import com.kratos.game.herphone.playerOnline.service.PlayerOnlineService;
import com.kratos.game.herphone.rank.service.AchievementRankService;
import com.kratos.game.herphone.rank.service.CluesRankService;
import com.kratos.game.herphone.recentGame.service.RecentGameService;
import com.kratos.game.herphone.report.service.ReportInfoDistinctService;
import com.kratos.game.herphone.report.service.ReportInfoService;
import com.kratos.game.herphone.report.service.ReportPlayerService;
import com.kratos.game.herphone.score.service.ScoreRewardService;
import com.kratos.game.herphone.score.service.ScoreService;
import com.kratos.game.herphone.signIn.service.SignInService;
import com.kratos.game.herphone.sms.service.SmsService;
import com.kratos.game.herphone.statisticalPlayer.service.StatisticalEyeshieldPlayerService;
import com.kratos.game.herphone.statisticalPlayer.service.StatisticalPioneerService;
import com.kratos.game.herphone.statisticalPlayer.service.StatisticalPlayerService;
import com.kratos.game.herphone.system.service.SystemService;
import com.kratos.game.herphone.systemMessgae.service.PublicSystemMessageService;
import com.kratos.game.herphone.systemMessgae.service.SystemMessageLastService;
import com.kratos.game.herphone.systemMessgae.service.SystemMessgaeService;
import com.kratos.game.herphone.systemNotice.service.SystemNoticeService;
import com.kratos.game.herphone.task.service.EveryDayVideoService;
import com.kratos.game.herphone.task.service.StageTaskService;
import com.kratos.game.herphone.task.service.TaskService;
import com.kratos.game.herphone.tencent.service.UnionIdService;
import com.kratos.game.herphone.user.service.UserService;
import com.kratos.game.herphone.watchVideo.service.WatchVideoService;


public abstract class BaseController {
	@Autowired
	protected MessageFirstService messageFirstService;
	@Autowired
	@Deprecated
	protected FandomLikeService fandomLikeService;
	@Autowired
	protected NominateBestService nominateBestService;
	@Autowired
	@Deprecated
	protected FandomService fandomService;
	@Autowired
	protected DiscussService discussService;
	@Autowired
	protected OrderService orderService;
	@Autowired
	protected AttentionService attentionService;
	@Autowired
	protected MessageService messageService;
	@Autowired
    protected BagService bagService;
	@Autowired
	protected PlayerServiceImpl playerServiceImpl; 
	@Autowired
	protected UserService userService;
	@Autowired
	protected SmsService smsService;
	@Autowired
	protected PlayerService playerService;
	@Autowired
	protected WatchVideoService warchVideoService;
	@Autowired
	protected PlayerDynamicService playerDynamicService;
	@Autowired
	protected PlayerExplorationService playerExplorationService;
	@Autowired
	protected ChosenOptionService chosenOptionService;
	@Autowired
	protected RecentGameService recentGameService;
	@Autowired
	protected PlayerOnlineService playerOnlineService;	
	@Autowired
	protected BadgeService badgeService;
	@Autowired
	protected DiscussLikeService discussLikeService;
	@Autowired
	protected HelperMessageService helperMessageService;
	@Autowired
	protected UnionIdService unionIdService;
	@Autowired
	protected AchievementService achievementService;
	@Autowired
	protected DiscussOptionService discussOptionService;
	@Autowired
	protected BlackListService blackListService;
	@Autowired
	protected ReportInfoService reportInfoService;
	@Autowired
	protected SystemNoticeService systemNoticeService;
	@Autowired
	protected ReportPlayerService reportPlayerService;
	@Autowired
	protected SystemMessgaeService systemMessgaeService;
	@Autowired
	protected RecommendBestService recommendBestService;
	@Autowired
	protected AdminUserService adminUserService;
	@Autowired
	protected ScoreService scoreService;
	@Autowired
	protected ScoreRewardService ScoreRewardEntityService;
	@Autowired
	protected SystemMessageLastService systemMessageLastService;
	@Autowired
	protected GameScoreService gameScoreService ;
	@Autowired
	protected StatisticalPlayerService statisticalPlayerService;
	@Autowired
	protected PlayerTagsService playerTagsService;
	@Autowired
	protected PlayerLoginTimeService playerLoginTimeService;
	@Autowired
	protected DynamicLikeService dynamicLikeService;
	@Autowired
	protected NoteService noteService;
	@Autowired
	protected PublicSystemMessageService publicSystemMessageService;
	@Deprecated
	@Autowired
	protected DynamicService dynamicService;
	@Autowired
	protected StatisticalEyeshieldPlayerService statisticalEyeshieldPlayerService;
	@Autowired
	protected SignInService signInService;
	@Autowired
	protected TaskService taskService;
	@Autowired
	protected  ReportInfoDistinctService reportInfoDistinctService;
	@Autowired
	protected PioneerService pioneerService;
	@Autowired
	protected EveryDayVideoService everyDayVideoService;
	@Autowired
	protected IntimacyService intimacyService;
	@Autowired
	protected PlayerClueService playerClueService;
	@Autowired
	protected CluesRankService cluesRankService;
	@Autowired
	protected AchievementRankService achievementRankService;
	@Autowired
	protected StatisticalPioneerService statisticalPioneerService;
	@Autowired
	protected IllegalRecordService illegalRecordService;
	@Autowired
	protected GameDisposeService gameDisposeService;
	@Autowired
	protected SystemService systemService;
	@Autowired
	protected StageTaskService stageTaskService;
	@Autowired
	protected GiftService giftService;
	@Autowired
	protected BoxService boxService;
	@Autowired
	protected PlayerClueDataService playerClueDataService;
	@Autowired
	protected GameHistoryService gameHistoryService;
	@Autowired
	protected HomePageService homePageService;
	@Autowired
	protected GameOverService gameOverService;
}

package com.kratos.game.herphone.common;

import org.springframework.beans.factory.annotation.Autowired;

import com.kratos.game.herphone.achievement.dao.AchievementDao;
import com.kratos.game.herphone.attention.dao.AttentionAuthorDao;
import com.kratos.game.herphone.attention.dao.AttentionDao;
import com.kratos.game.herphone.badge.dao.BadgeDao;
import com.kratos.game.herphone.bag.dao.BagDao;
import com.kratos.game.herphone.blackList.dao.BlackListDao;
import com.kratos.game.herphone.clue.dao.PlayerClueDao;
import com.kratos.game.herphone.clue.dao.PlayerClueDataDao;
import com.kratos.game.herphone.discuss.dao.DiscussDao;
import com.kratos.game.herphone.discuss.dao.DiscussLikeDao;
import com.kratos.game.herphone.discuss.dao.DiscussOptionDao;
import com.kratos.game.herphone.discuss.dao.DiscussRemoveDao;
import com.kratos.game.herphone.discuss.dao.RecommendBestDao;
import com.kratos.game.herphone.dynamic.dao.DynamicDao;
import com.kratos.game.herphone.dynamic.dao.DynamicLikeDao;
import com.kratos.game.herphone.dynamic.dao.NominateBestDao;
import com.kratos.game.herphone.dynamic.dao.PlayerTagsDao;
import com.kratos.game.herphone.fandom.dao.FandomDao;
import com.kratos.game.herphone.fandom.dao.FandomLikeDao;
import com.kratos.game.herphone.fandom.service.FandomLikeService;
import com.kratos.game.herphone.gameHistory.dao.GameHistoryDao;
import com.kratos.game.herphone.gameHistory.dao.PlayHistoryDao;
import com.kratos.game.herphone.gameOver.dao.GameOverDao;
import com.kratos.game.herphone.gift.dao.GiftDao;
import com.kratos.game.herphone.helperMessage.dao.HelperMessageOptionAnswerDao;
import com.kratos.game.herphone.helperMessage.dao.PlayerHelperAnswerDao;
import com.kratos.game.herphone.illegal.dao.IllegalRecordDao;
import com.kratos.game.herphone.intimacy.dao.IntimacyDao;
import com.kratos.game.herphone.intimacy.dao.NoteDao;
import com.kratos.game.herphone.message.dao.MessageDao;
import com.kratos.game.herphone.message.dao.MessageFirstDao;
import com.kratos.game.herphone.message.service.MessageService;
import com.kratos.game.herphone.order.dao.OrderDao;
import com.kratos.game.herphone.order.entity.OrderEntity;
import com.kratos.game.herphone.pioneer.dao.PioneerDao;
import com.kratos.game.herphone.player.dao.ExchangeRewardDao;
import com.kratos.game.herphone.player.dao.RewardDao;
import com.kratos.game.herphone.playerDynamic.dao.PlayerDynamicDao;
import com.kratos.game.herphone.playerOnline.dao.PlayerLoginTimeDao;
import com.kratos.game.herphone.playerOnline.dao.PlayerOnlineDao;
import com.kratos.game.herphone.rank.dao.AchievementRankDao;
import com.kratos.game.herphone.rank.dao.CluesRankDao;
import com.kratos.game.herphone.recentGame.dao.RecentGameDao;
import com.kratos.game.herphone.report.dao.ReportInfoDao;
import com.kratos.game.herphone.report.dao.ReportInfoDistinctDao;
import com.kratos.game.herphone.report.dao.ReportPlayerDao;
import com.kratos.game.herphone.score.dao.ScoreRewardDao;
import com.kratos.game.herphone.signIn.dao.SignInDao;
import com.kratos.game.herphone.sms.dao.SmsDao;
import com.kratos.game.herphone.statisticalPlayer.dao.StatisticalEyeshieldPlayerDao;
import com.kratos.game.herphone.statisticalPlayer.dao.StatisticalPioneerDao;
import com.kratos.game.herphone.statisticalPlayer.dao.StatisticalPlayerDao;
import com.kratos.game.herphone.system.service.SystemService;
import com.kratos.game.herphone.systemMessgae.dao.PublicSystemMessageDao;
import com.kratos.game.herphone.systemMessgae.dao.SystemMessageLastDao;
import com.kratos.game.herphone.systemMessgae.dao.SystemMessgaeDao;
import com.kratos.game.herphone.systemNotice.dao.SystemNoticeDao;
import com.kratos.game.herphone.task.dao.EveryDayVideoDao;
import com.kratos.game.herphone.task.dao.StageTaskDao;
import com.kratos.game.herphone.task.dao.TaskDao;
import com.kratos.game.herphone.tencent.dao.UnionIdDao;
import com.kratos.game.herphone.user.dao.UserDao;
import com.kratos.game.herphone.watchVideo.dao.WatchVideoDao;


public abstract class BaseService extends BaseController{
	@Autowired
	protected SystemService systemService;
	@Autowired
	protected MessageFirstDao messageFirstDao;
	@Autowired
	protected FandomLikeService fandomLikeService;
	@Autowired
	protected FandomLikeDao fandomLikeDao;
	@Autowired
	protected FandomDao fandomDao;
	@Autowired
	protected CommonService commonService;
	@Autowired
	protected DiscussDao discussDao;
	@Autowired
	protected MessageDao  messageDao;
	@Autowired
	protected BagDao bagDao;
	@Autowired
	protected RewardDao RewardDao;
	@Autowired
	protected BadgeDao badgeDao;
	@Autowired
	protected ExchangeRewardDao ExchangeRewardDao;
	@Autowired
	protected ScoreRewardDao scoreRewardDao;
	@Autowired
	protected SmsDao smsDao;
	@Autowired
	protected UserDao userDao;
	@Autowired
	protected WatchVideoDao watchVideoDao;
	@Autowired
	protected AchievementDao achievementDao;
	@Autowired
	protected AttentionDao attentionDao;
	@Autowired
	protected PlayerDynamicDao playerDynamicDao;
	@Autowired
	protected RecentGameDao recentGameDao;	
	@Autowired
	protected DiscussLikeDao discussLikeDao;
	@Autowired
	protected PlayerOnlineDao playerOnlineDao;
	@Autowired
	protected MessageService messasgeService;
	@Autowired
	protected PlayerHelperAnswerDao playerHelperAnswerDao;
	@Autowired
	protected HelperMessageOptionAnswerDao helperMessageOptionAnswerDao;
	@Autowired
	protected UnionIdDao unionIdDao;
	@Autowired
	protected DiscussOptionDao discussOptionDao;
	@Autowired
	protected DiscussRemoveDao discussRemoveDao;
	@Autowired
	protected BlackListDao blackListDao;
	@Autowired
	protected ReportInfoDao reportInfoDao;
	@Autowired
	protected ReportPlayerDao reportPlayerDao;
	@Deprecated
	@Autowired
	protected DynamicDao dynamicDao;
	@Autowired
	protected CluesRankDao cluesRankDao;
	@Autowired
	protected DynamicLikeDao dynamicLikeDao;
	@Autowired
	protected RecommendBestDao recommendBestDao; 
	@Autowired
	protected SystemMessgaeDao systemMessgaeDao;
	@Autowired
	protected PlayerTagsDao playerTagsDao;
	@Autowired
	protected SystemMessageLastDao systemMessageLastDao;
	@Autowired
	protected StatisticalPlayerDao statisticalPlayerDao;
	@Autowired
	protected SystemNoticeDao systemNoticeDao;
	@Autowired
	protected StatisticalEyeshieldPlayerDao statisticalEyeshieldPlayerDao;
	@Autowired
	protected PlayerLoginTimeDao playerLoginTimeDao;
	@Autowired
	protected PublicSystemMessageDao publicSystemMessageDao;	
	@Autowired
	protected SignInDao signInDao;
	@Autowired
	protected EveryDayVideoDao everyDayVideoDao;
	@Autowired
	protected NoteDao noteDao;
	@Autowired
	protected AchievementRankDao achievementRankDao;
	@Autowired
	protected ReportInfoDistinctDao reportInfoDistinctDao;
	@Autowired
	protected PioneerDao pioneerDao;
	@Autowired
	protected TaskDao taskDao;
	@Autowired
	protected IntimacyDao intimacyDao;
	@Autowired
	protected StatisticalPioneerDao statisticalPioneerDao;
	@Autowired
	protected NominateBestDao nominateBestDao;
	@Autowired
	protected PlayerClueDao playerClueDao;
	@Autowired
	protected OrderDao orderDao;
	@Autowired
	protected IllegalRecordDao illegalRecordDao;
	@Autowired
	protected StageTaskDao stageTaskDao;
	@Autowired
	protected GiftDao giftDao;
	@Autowired
	protected AttentionAuthorDao attentionAuthorDao;
	@Autowired
	protected PlayerClueDataDao playerClueDataDao;
	@Autowired
	protected GameHistoryDao gameHistoryDao;
	@Autowired
	protected PlayHistoryDao playHistoryDao;
	@Autowired
	protected GameOverDao gameOverDao;
}

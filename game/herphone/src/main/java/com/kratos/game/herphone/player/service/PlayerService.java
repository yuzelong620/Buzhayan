package com.kratos.game.herphone.player.service;

import java.util.List;
import java.util.Map;

import com.globalgame.auto.json.Achievement_Json;
import com.kratos.engine.framework.crud.ICrudService;
import com.kratos.game.herphone.player.bean.PlayerPage;
import com.kratos.game.herphone.player.bean.PlayerScoreBean;
import com.kratos.game.herphone.player.bean.RanKingData;
import com.kratos.game.herphone.player.bean.ResPlayerPowerData;
import com.kratos.game.herphone.player.domain.Player;
import com.kratos.game.herphone.player.message.ReqPlayerEdit;
import com.kratos.game.herphone.player.message.ReqPlayerTencentLogin;
import com.kratos.game.herphone.player.message.ReqPlayerWechatLogin;
import com.kratos.game.herphone.player.message.ResPlayerLogin;
import com.kratos.game.herphone.player.message.ResPlayerProfile;
import com.kratos.game.herphone.player.message.ResRankPlayer;
import com.kratos.game.herphone.statisticalPlayer.bean.ResStplayerInfo;
import com.kratos.game.herphone.player.bean.PlayerProperty;
import com.kratos.game.herphone.player.bean.PlayerAllBean;
import com.kratos.game.herphone.player.bean.PlayerAstrictBean;
import com.kratos.game.herphone.player.bean.PlayerIdBean;
public interface PlayerService extends ICrudService<Long, Player> {
    /**
     * 根据微信验证code登录
     * @param code 信验证code登录
     * @return 是否为创建用户
     */
    ResPlayerLogin wechatLogin(String code);

    /**
     * QQ登录
     * @param request 请求
     * @return 是否为创建用户
     */
    ResPlayerLogin tencentLogin(ReqPlayerTencentLogin request);

    /**
     * 游客登录
     * @return 是否为创建用户
     */
    ResPlayerLogin guestLogin();

    /**
     * 根据token查询
     * @param token token
     * @return 用户
     */
    Player findByToken(String token);

    /**
     * 重命名
     * @param reqPlayerEdit 用户信息
     */
    Player edit(ReqPlayerEdit reqPlayerEdit);

    /**
     * 获取用户信息
     */
    ResPlayerProfile getProfile();

    /**
     * 授权
     * @param request 请求
     * @return 玩家
     */
    Player auth(ReqPlayerEdit request);

    /**
     * 每日重置
     * @param player 用户
     */
    void checkDailyReset(Player player);

    /**
     * 访问首页
     */
    void index();
    
    /**根据角色Id查找player*/
    Long getPlayerByRoleId(String roleId);
    
    /**
     * 解锁成就
     * @param id 成就id
     * @return  是否已经解锁
     */
    Boolean releaseAchievement(int id);

    /**
     * 查询成就
     * @param gameId 游戏id
     * @return 成就
     */
    List<Achievement_Json> getReleasedAchievements(int gameId);

    /**
     * 获取排行榜
     * @return 排行榜
     */
    List<ResRankPlayer> getRank();

    /**
     * 扣除电量
     * @return 用户信息
     */
    ResPlayerProfile costPower();

    /**
     * 设置电量
     * @param power 电量
     */
    ResPlayerProfile setPower(Integer power);
    /**
     * 扣除电量
     * @param power 电量
     * @return 用户信息
     */
    ResPlayerProfile costPower(Integer power);

    /**
     * 分享成功
     * @return 用户
     */
    ResPlayerProfile onShared();

    /**
     * 领取微信分享奖励
     * @return 用户信息
     */
    ResPlayerProfile takeShare();

    /**
     * 领取消耗20体力
     * @return 用户信息
     */
    ResPlayerProfile takeCostPower20();

    /**
     * 领取消耗100体力
     * @return 用户信息
     */
    ResPlayerProfile takeCostPower100();

    /**
     * gm设置电量
     * @param name 玩家名
     * @param power 电量
     * @return 用户
     */
    ResPlayerProfile gmSetPower(String name, Integer power);

    /**
     * 查看探索度排行榜
     * @return 探索度排行
     */
    List<ResRankPlayer> getExploration();

    /**
     * 绑定微信
     * @param request 请求参数
     * @return 用户信息
     */
    ResPlayerProfile bindWechat(ReqPlayerWechatLogin request);

    /** 
     * 绑定QQ(老接口)
     * @param request 请求参数
     * @return 用户信息
     */
    ResPlayerProfile bindTencent(ReqPlayerTencentLogin request);
    /**
     * 绑定QQ 新的的方法, request.channelId 参数用于 不同appid.  
     * @param request 请求参数
     * @return 用户信息
     */
    ResPlayerProfile bindTencent2(ReqPlayerTencentLogin request);

    /**
     * 消耗体力存档
     * @return 用户信息
     */
    ResPlayerProfile saveRecordCostPower();

    /**
     * GM 添加电量
     * @param roleId 角色id
     * @param power 电量
     * @return 用户
     */
    ResPlayerProfile gmAddPower(String roleId, Integer power);

    /**
     * 查看广告
     * @return 用户
     */
    ResPlayerProfile watchAd();

    /**
     * 领取查看广告
     * @return 用户
     */
    ResPlayerProfile takeWatchAd();

    /**
     * 新的qq登录
     * @param request
     * @return
     */
    ResPlayerLogin tencentLogin2(ReqPlayerTencentLogin request);

    /**
     * 查询所有剧本编号
     */
	List<Integer> getScenarioAll();

	/**
	 * 查询所有剧本的对应玩家数量
	 */
	Map<String,Integer> getPlayerNum();
	
	/**
	 * 查询已评分的玩家
	 */
	Map<Integer,Integer> getGradePlayer();
	
	/**
	 * 查询所有剧本的对应玩家平均分
	 */
	List<PlayerScoreBean> getAverageScore();

	/**
	 * 查询成就排行榜
	 */
	List<RanKingData> getAchievement(PlayerPage playerPage);
	
	/**
	 * 查询探索度排行榜
	 */
	List<RanKingData> getExploration(PlayerPage playerPage);

	/**
	 * 查询探索排行榜中绑定手机号码玩家
	 */
	List<RanKingData>getExplorationPhone (PlayerPage playerPage);

	/**
	 * 查询成就排行榜中绑定手机号码玩家
	 */
	List<RanKingData> getAchievementPhone (PlayerPage playerPage);

	/**
	 * 创建机器人
	 */
	void creatRobot();

	/**
     * 根据成就指定昵称查找用户信息
     */
	List<RanKingData> getByNameAchievementData(PlayerPage playerPage,String name);
	
	/**
     * 根据成就指定角色ID查找用户信息
     */
	List<RanKingData> getByIdAchievementData(String roleId);
	
	/**
     * 根据成就指定手机号码查找用户信息
     */
	RanKingData getByPhoneAchievement(String phone);
	
	/**
	 * 根据成就榜指定昵称处理探索榜是否绑定手机号
	 */
	List<RanKingData> getByNameAchievement(PlayerPage playerPage,String name);
	
	/**
	 * 根据成就指定ID处理探索榜是否绑定手机号
	 */
	List<RanKingData> getByIdAchievement(String roleId);

	/**
             * 根据探索指定昵称查找用户信息
     */
	List<RanKingData> getByNameExplorationData(PlayerPage playerPage,String name);
	
	/**
             * 根据探索指定角色ID查找用户信息
     */
	List<RanKingData> getByIdExplorationData(String roleId);
	
	/**
             * 根据探索指定手机号码查找用户信息
     */
	RanKingData getByPhoneExploration(String phone);
	
	/**
	 * 根据探索指定昵称处理探索榜是否绑定手机号
	 */
	List<RanKingData> getByNameExploration(PlayerPage playerPage,String name);
	
	/**
	 * 根据探索指定ID处理探索榜是否绑定手机号
	 */
	List<RanKingData> getByIdExploration(String roleId);

	/**
	 *缓存 查询玩家总人数
	 */
	long getPlayerCount();
	
	/**
	 * 在DB查询玩家总人数
	 */
	long getPlayerCountDB();

	/**
	 * 在DB查询指定字段玩家总人数
	 */
	int getAssignPlayerCount(String fields,String data);
	
	/**
	 * 根据参数生成sql语句
	 */
	String createSqlWord(String fields,String data);
    /**
	 * 根据roleid查找playerid
	 * */
	PlayerIdBean findplayeridbyroleid(String roleid);
	
	/**
	 * 根据ID查询信息并展示
	 * */
	PlayerProperty selectByRoleId(String roleId);
	
	/**
	 *根据playerid查询玩家信息
	 * */
	PlayerProperty findmessageBylayerId(long PlayerId);
	
	/**
	 *根据手机号查询玩家信息并展示
	 * */
	PlayerProperty selectByphone(String phone);
	
	/**
	 *根据昵称查询玩家信息
	 * */
	List<PlayerProperty> selectmessages(String nickname,PlayerPage playerPage);
	
	/**
	 *根据昵称查询玩家信息并展示
	 * */
	List<PlayerProperty> selectBynickname(String nickname,PlayerPage playerPage);

	
	 /**
     * 封号
     * @param playerId
     * @return
     */
    public boolean setIsBlock(long playerId);
    
	 /**
     * 取消封号
     * @param playerId
     * @return
     */
    public boolean callIsBlock(long playerId);
   
     /**
     * @param playerId
     * @param time  禁言时间
     * @return
     */
    public boolean setNoSpeak(long playerId,long time);
    
    /**根据成就值排名获取玩家信息*/
    public List<ResStplayerInfo> ListPlayerByAch(int page ,int count);
    
     /**
     * 取消禁言
     * @param playerId
     * @param time  禁言时间
     * @return
     */
    public boolean callNoSpeak(long playerId);
    
    /**
     * 查询所有玩家信息
     * */
    @Deprecated
    List<PlayerAllBean> findall(PlayerPage playerPage);	
    
    /**
     * 查询违规玩家信息
     * */
    @Deprecated
    List<PlayerProperty> findFoul(PlayerPage playerPage);

	/**
	 * 查询玩家玩过剧本个数
	 * */
    @Deprecated
	public List<PlayerProperty> findisPlayerById(List<Long> playerid);
	
	/**
	 * 查询某个昵称的总条数
	 * */
	@Deprecated
	public long findnicknamecount(String nickname);
	/**查询500条不是游客的玩家 （用于初始化转移数据）*/
	public List<Player> listInitPlayer(int page);
	/**
	 * 查询数据库违规总数
	 * */
	@Deprecated
	public long badcount();
	
	/**获取玩家禁言和封号信息*/
	Map<String,PlayerAstrictBean> getPlayerAstrictData(List<Long> playerIds);
	/**查询指定玩家禁言封号信息*/
	PlayerAstrictBean getPlayerAstrictData(long playerId);
	/**查询所有当前违规玩家*/
	Map<String,PlayerAstrictBean> findAtFoulPlayAll(int page,int count);
	/**查询当前违规玩家人员总数*/
	long findAtFoulPlayCount();
	
	/**
	 * 查询玩家玩过的剧本个数 并记录玩过剧本个数
	 */
	int getPlayerNumAlsoRecord();

	/**
	 * 查询指定玩家货币数量
	 */
	int getCurrencyNum();

	/**
	 * 查询玩家电量上限
	 */
	int getPowerLimit();

	/**
	 * 电量更新
	 */
	void recoverPower(Player player);

	/**
	 * 查询玩家当前电量
	 */
	ResPlayerPowerData getPower();
}

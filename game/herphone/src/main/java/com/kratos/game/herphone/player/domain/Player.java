package com.kratos.game.herphone.player.domain;

import com.kratos.engine.framework.common.CommonConstant;
import com.kratos.engine.framework.common.utils.StringHelper;
import com.kratos.engine.framework.db.LongKeyEntity;
import com.kratos.engine.framework.net.socket.exception.BusinessException;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.log4j.Log4j;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Base64;


/**
 * 玩家实体
 *
 * @author herton
 */
@Data
@Log4j
@EqualsAndHashCode(callSuper = true)
@Entity
//@Table(indexes = {@Index(name="is_guest",columnList="isGuest"),@Index(name="role_id",columnList="roleId"),@Index(name="nick_name",columnList="nickName")})
public class Player extends LongKeyEntity {
    private static final long serialVersionUID = 8913056963732639062L;

    @Column(length = 100)
    private String loginName;
    @Column(length = 500)
    private String password;
    @Column(length = 500)
    private String name; // 姓名
    @Column(length = 500)
    private String nickName; // 微信昵称
    @Column(length = 36)
    private String roleId; // 角色ID最长9位，由数字和大写英文字母构成。
    @Column(length = 36)
    private String token; // 权限
    @Column(length = 2)
    private String gender; // 性别
    @Column(length = 30)
    private String wechatOpenid; // 微信openid
    @Column(length = 32)
    private String tencentOpenid; // QQ openid
    @Column(length = 500)
    private String avatarUrl; // 头像
    @Column
    private boolean gm; // 是否为gm
    @Column(nullable = false, length = 20)
    private long lastDailyReset; // 上一次每日重置的时间戳
    @Column(length = 20)
    private Long lastLoginTime; // 上次登录时间
    @Column
    private int achievement; // 成就值
    @Column(length = 4000)
    private String releasedAchievements; // 成就
    @Column(length = 20)
    private Long lastAddAchievementTime; // 上次解锁成就时间
    @Column(nullable=false,columnDefinition="INT default 0")
    private Integer power; // 电量
    @Column(length = 20)
    private Long lastRecoverPowerTime; // 上次恢复电量时间
    @Column
    private Integer costedPower; // 已消耗电量
    @Column
    private Boolean takenCostPower20; // 领取消耗20体力任务
    @Column
    private Boolean takenCostPower100; // 领取消耗100体力任务
    @Column
    private Boolean takenShare; // 领取QQ分享任务
    @Column
    private Integer sharedCount; // QQ分享次数
    @Column
    private Integer exploration; // 总探索度
    @Column(length = 20)
    private Long lastAddExplorationTime; // 上次解锁探索度时间
    @Column
    private Integer watchAdTask; // 查看广告任务
    @Column
    private Integer takenWatchAdTask; // 领取查看广告任务
    @Column(nullable=false,columnDefinition="INT default 0")
    private  int extraPowerLimit;//额外增加的体力上限
    @Column(nullable=false,columnDefinition="INT default 0")
    private  double extraRecoverRote;//额外恢复 体力加成。 小数
    @Column(nullable=false,columnDefinition="INT default 1")
    private int isGuest  = 1;//是否为游客身份 0为不是游客身份 1为游客身份 
    @Column(nullable=false,columnDefinition="bigint default 0")
    long noSpeakTime;//禁言截止时间
    @Column(nullable=false,columnDefinition="int default 0")
    int isBlock;//封号 =1
    @Column(nullable=false,columnDefinition="bigint default 1569317965250")
    private long register;
    
    public void addAchievement(int achievement) {
        this.achievement += achievement;
    }

    public void addPower(int power) {
        this.addPower(power, false);
    }

    public void addPower(int power, boolean over) {
    	if( this.power==null) {
    		this.power=100;
    	}
        if(over) {
            this.power += power;
        } else {
        	int max_power=100+extraPowerLimit;//我的上限
            if(this.power < max_power) {
                if(this.power + power <= max_power) {
                    this.power += power;
                } else {
                    this.power = max_power;
                }
            }
        }
    }

    public void costPower(int power) {
    	if(power<0){//负数 非法参数
    		throw new BusinessException("参数错误");
    	}
        if(this.power - power < 0) {
            throw new BusinessException("电量不足");
        }
        this.power -= power;
        if(this.costedPower == null) {
            this.costedPower = 0;
        }
        this.costedPower += power;
    }

    public void addWatchAd(int time) {
        if(this.watchAdTask == null) {
            this.watchAdTask = 0;
        }
        this.watchAdTask += time;
    }

    public void addTakenWatchAd(int time) {
        if(this.takenWatchAdTask == null) {
            this.takenWatchAdTask = 0;
        }
        this.takenWatchAdTask += time;
    }

    @Override
    public String toString() {
        return "Player [id=" + getId() + ", name=" + name + "]";
    }

    public String decodeName() {
        try {
            if (StringHelper.isNotBlank(this.nickName))
                return new String(Base64.getDecoder().decode(this.nickName.getBytes()), CommonConstant.UTF8);
        } catch (Exception e) {
            log.error("", e);
        }
        return "";
    }
    
    public String decodeName(String nickName) {
        try {
            if (StringHelper.isNotBlank(nickName))
                return new String(Base64.getDecoder().decode(nickName.getBytes()), CommonConstant.UTF8);
        } catch (Exception e) {
            log.error("", e);
        }
        return "";
    }
    
    public String setCodeName(String nickName) {
    	try {
    		if(StringHelper.isNotBlank(nickName)) {
    			return new String(Base64.getEncoder().encode(nickName.getBytes("utf-8")), CommonConstant.UTF8);
    		}
    	 } catch (Exception e) {
             log.error("", e);
         }
    	return "";
    }
    
}

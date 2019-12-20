package org.hutu.blacklist.manager;

import java.util.concurrent.ConcurrentHashMap;

import org.hutu.blacklist.bean.BlackListIpBean;
import org.hutu.timer.manager.TimerManager;
import org.slf4j.LoggerFactory;

public class BlackListIpManager {
	org.slf4j.Logger logger = LoggerFactory.getLogger(BlackListIpManager.class);
	public static final BlackListIpManager instance = new BlackListIpManager();

	final ConcurrentHashMap<String, BlackListIpBean> map = new ConcurrentHashMap<>();

	public void addExceptionCount(String ip) {
		BlackListIpBean obj = map.get(ip);
		long currentTime = System.currentTimeMillis();
		if (obj == null) {
			obj = new BlackListIpBean(ip, 1, currentTime);
			map.put(obj.getIp(), obj);
			return;
		}
		long step =currentTime- limit_step_time;
		if (obj.getLastTime() > step) {// 上次報錯，在十秒内。纍計報錯次數
			obj.setCount(obj.getCount() + 1);
		} else {
			obj.setCount(1);// 重新記錄報錯次數
		}
		obj.setLastTime(currentTime);
		logger.info(obj.getIp() + "报错累计：" + obj.getCount() + "次！");
	}

	public boolean isBlackIp(String ip) {
		if (ip == null) {
			return false;
		}
		BlackListIpBean obj = map.get(ip);
		if (obj == null) {
			return false;
		}
		if (obj.getCount() >= limit_count) {// 超過10次
			return true;
		}
		return false;
	}

	private static final int limit_count = 10; // 連續報錯的次數
	private static final long limit_step_time = 1000 * 4; // 超过这个时间 累计次数

	private static final long check_stepTime = 1000 * 60 * 10;// 1分钟定时检查，是否移除黑名单
	private static final long check_limit_time = 1000 * 60 * 60;// 黑名单限制时间

	public void init() {
		Runnable task = getCheckTask();
		TimerManager.instance.schedule(task, 0, check_stepTime);
	}

	private Runnable getCheckTask() {
		// 清理 没有达到条件的黑名单移除
		return new Runnable() {
			long checkTime = System.currentTimeMillis() - check_limit_time;
			@Override
			public void run() {
				logger.info("检查黑名单ip-->");
				for (BlackListIpBean obj : map.values()) {
					if (obj.getLastTime() < checkTime) {// 過期的時間
						map.remove(obj.getIp(), obj);
						logger.info("解除黑名單：" + obj.getIp());
					}
				}
			}
		};
	}

}

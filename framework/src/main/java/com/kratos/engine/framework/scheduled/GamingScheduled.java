package com.kratos.engine.framework.scheduled;

import com.kratos.engine.framework.global.BaseGlobalData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class GamingScheduled {
	@Autowired
	private BaseGlobalData globalData;

    @Scheduled(cron = "0 0 5 * * *")
    public void scheduled() {
		log.info("每日５点定时任务开始");

		long now = System.currentTimeMillis();
		globalData.update("dailyResetTimestamp", now);
    }

}

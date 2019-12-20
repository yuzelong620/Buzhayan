package com.kratos.game.herphone;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.kratos.engine.framework.base.SpringContext;
import com.kratos.engine.framework.db.DbService;
import com.kratos.engine.framework.net.socket.transport.SocketServer;
import com.kratos.game.herphone.common.MongodbManager;
import com.kratos.game.herphone.event.WorkManager;
import com.kratos.game.herphone.json.JsonCacheManager;
import com.kratos.game.herphone.systemMessgae.service.SystemMessgaeService;

import lombok.extern.log4j.Log4j;

/**
 *  MongoAutoConfiguration.class,MongoDataAutoConfiguration.class
 * @author herton
 */
@Log4j
@EnableScheduling
@SpringBootApplication(exclude ={MongoDataAutoConfiguration.class,MongoAutoConfiguration.class})//排除屏蔽 自動配置mongodb
@EntityScan(basePackages = {"com.kratos.engine.framework", "com.kratos.game.herphone"})
@ComponentScan(basePackages = {"com.kratos.engine.framework", "com.kratos.game.herphone"})
public class HerPhoneApplication implements CommandLineRunner {
    public static void main(String[] args) {
        //加载mongodb 相关配置
	    MongodbManager.instance.init();
	    log.info("mongodb 加载完成");
	    JsonCacheManager.getInstance().init();
	    log.info("json文件  加载完成");
	    JsonCacheManager.getInstance().check();
	    //启动数据存储服务
	    DbService.init(30);	   
	    SpringApplication.run(HerPhoneApplication.class, args);
// 		DiscussService.getInstance().init();
	    WorkManager.getInstance().init(10);
	    SystemMessgaeService.getInstance().init();
//	    BadgeService.getInstance().init(); //需求有变，此方法删掉
//	    PlayerDynamicService.getInstance().init(); //转化成就和探索数据 ,需求有变，此方法删掉
    }
    private void stop() {
        try {
            SpringContext.getBean(SocketServer.class).shutDown();
            DbService.close();
        } catch (Exception e) {
            log.error("", e);
        }
    }

    @Override
    public void run(String... strings) throws Exception {
        HerPhoneApplication app = new HerPhoneApplication();
        Runtime.getRuntime().addShutdownHook(new Thread(app::stop));
    }

    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true); // 允许cookies跨域
        config.addAllowedOrigin("*");// #允许向该服务器提交请求的URI，*表示全部允许，在SpringMVC中，如果设成*，会自动转成当前请求头中的Origin
        config.addAllowedHeader("*");// #允许访问的头信息,*表示全部
        config.setMaxAge(18000L);// 预检请求的缓存时间（秒），即在这个时间段里，对于相同的跨域请求不会再预检了
        config.addAllowedMethod("OPTIONS");// 允许提交请求的方法，*表示全部允许
        config.addAllowedMethod("HEAD");
        config.addAllowedMethod("GET");// 允许Get的请求方法
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("DELETE");
        config.addAllowedMethod("PATCH");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}

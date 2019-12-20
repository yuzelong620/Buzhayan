package com.kratos.game.herphone.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import java.util.Properties;

import org.apache.log4j.Logger;

import org.hutu.mongo.config.MongoTemplateFactory;
import org.hutu.mongo.config.MongodbConfigLoad;
import org.hutu.mongo.dao.BaseDao;
import org.springframework.data.mongodb.core.MongoTemplate;
 

public class MongodbManager {
	
	public static final MongodbManager instance=new MongodbManager();
	Logger logger = Logger.getLogger(MongodbManager.class);
	public   void init(){ 
	try {
		File file = new File("config");
		InputStream mongodbConfig = null; 
		if (file.exists()) {// 外部文件
			mongodbConfig = new FileInputStream(file.getPath() + "/mongo.properties"); 
			logger.info("加载外部config路径配置文件:" + file.getAbsolutePath());
		} 
		else {// 类路径文件
			mongodbConfig = ClassLoader.getSystemResourceAsStream(file.getPath() + "/mongo.properties"); // mongodb
            logger.info("加载class路径配置文件");
		}

		// 加载mongodb 配置文件
		Properties prop = new Properties();
		prop.load(mongodbConfig);
		mongodbConfig.close();
		MongoTemplate template= MongoTemplateFactory.intiDataSource(MongodbConfigLoad.load(prop));
        BaseDao.setMongoTemplate(template);
        
        template.getDb();
		logger.info("加载mongodb 加载");

	} catch (Exception e) {
		logger.error("发生异常导致启动失败：",e);
		System.exit(0);
	}
	}
}

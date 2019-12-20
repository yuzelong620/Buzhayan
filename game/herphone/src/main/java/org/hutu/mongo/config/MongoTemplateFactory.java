package org.hutu.mongo.config;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.alibaba.fastjson.JSONObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.MongoClientOptions.Builder;

public class MongoTemplateFactory {
	static Logger logger=LoggerFactory.getLogger(MongoTemplateFactory.class);
	
	public static MongoTemplate intiDataSource(String host,int port,String databaseName){
		try{
			MongoTemplate mongoTemplate;
			Builder builder = MongoClientOptions.builder();
			builder.connectionsPerHost(50);
			builder.connectTimeout(10000);
			builder.maxWaitTime(2000);
			builder.socketKeepAlive(true);
			builder.socketTimeout(1500);
			builder.threadsAllowedToBlockForConnectionMultiplier(50);
		
			MongoClientOptions mongoClientOptions = builder.build();
			ServerAddress serverAddress = new ServerAddress(host,port);
			MongoClient mongoClient = new MongoClient(serverAddress, mongoClientOptions);			 
			mongoTemplate = new MongoTemplate(mongoClient,databaseName);
			mongoTemplate.getDb();
			return mongoTemplate;
		}
		catch(Exception e){
			throw new RuntimeException(e);
		}
	
	}  
	
	public static MongoTemplate intiDataSource(MongoConfigBean config){
		    if(config.getUsername()==null||config.getUsername().trim().equals("")){
		    	logger.info("没有设置mongodb用户名验证"+JSONObject.toJSONString(config.getHost()));
		    	return intiDataSource(config.getHost().get(0).getIp(),config.getHost().get(0).getPort(), config.getDatabase());//本地 一个连接地址
		    }
		    logger.info("配置文件设置了用户名"+JSONObject.toJSONString(config.getHost()));
	    	return loadMongodb(config);//使用全参配置
		}
	
	private static MongoTemplate loadMongodb(MongoConfigBean config) {
		try{			
			Builder builder = MongoClientOptions.builder();
			builder.connectionsPerHost(config.getConnectionsPerHost());
			builder.connectTimeout(config.getConnectTimeout());
			builder.maxWaitTime(config.getMaxWaitTime());
			builder.socketKeepAlive(config.isSocketKeepAlive());
			builder.socketTimeout(config.getSocketTimeout());
			builder.threadsAllowedToBlockForConnectionMultiplier(config.getThreadsAllowedToBlockForConnectionMultiplier());
		
			MongoClientOptions mongoClientOptions = builder.build();
			//设置连接地址
            List<ServerAddress> serverAddresses=new ArrayList<>();
            for(ConfigHost host:config.getHost()){
			   serverAddresses.add(new ServerAddress(host.getIp(),host.getPort()));
            }			
			//构建鉴权信息
			List<MongoCredential> credentials = new ArrayList<MongoCredential>();
			credentials.add(MongoCredential.createScramSha1Credential(config.getUsername(), config.getDatabase(), config.getPassword().toCharArray()));//2.14.2版本里出现了
		    MongoClient mongoClient = new MongoClient(serverAddresses,credentials, mongoClientOptions); 
		    
			MongoTemplate mongoTemplate  = new MongoTemplate(mongoClient,config.getDatabase());//设置用户名密码 
			mongoTemplate.getDb();
			return mongoTemplate;
		}
		catch(Exception e){
			throw new RuntimeException(e);
		}
	}
}

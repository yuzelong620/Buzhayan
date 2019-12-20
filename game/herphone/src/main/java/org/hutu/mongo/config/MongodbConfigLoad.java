package org.hutu.mongo.config;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

public class MongodbConfigLoad {
   static Logger logger=LoggerFactory.getLogger(MongodbConfigLoad.class);
	public static MongoConfigBean load(Properties prop) {
		logger.info("加载mongodb 配置文件："+JSONObject.toJSONString(prop));
		MongoConfigBean bean=new MongoConfigBean();
		bean.setAutoConnectRetry(getBool(prop,"autoConnectRetry"));
		bean.setConnectionsPerHost(getInt(prop,"connectionsPerHost"));
		bean.setConnectTimeout(getInt(prop,"connectTimeout"));
		bean.setDatabase(prop.getProperty("database"));
		bean.setFsync(getBool(prop,"fsync"));
		//host中包含 ip:post,ip:post
		
		String[] hosts=prop.getProperty("host").split(",");
		
		for(String host:hosts){
			 String [] arr=host.split(":");
			 String ip=arr[0];
			 int port=Integer.parseInt(arr[1]);
			 bean.getHost().add(new ConfigHost(ip, port));
		}
		logger.info("数据库连接地址："+JSONObject.toJSONString(bean.getHost()));
        bean.setMaxWaitTime(getInt(prop,"maxWaitTime"));
        bean.setPassword(prop.getProperty("password"));
    
        bean.setSlaveOk(getBool(prop,"slaveOk"));
        bean.setSocketKeepAlive(getBool(prop,"socketKeepAlive"));
        bean.setSocketTimeout(getInt(prop,"socketTimeout"));
        bean.setThreadsAllowedToBlockForConnectionMultiplier(getInt(prop,"threadsAllowedToBlockForConnectionMultiplier"));
        bean.setUsername(prop.getProperty("username"));
        bean.setWriteNumber(getInt(prop,"writeNumber"));
        bean.setWriteTimeout(getInt(prop,"writeTimeout"));
        return bean; 
	}
	
	static int  getInt(Properties prop,String key){
		try{
		    return Integer.parseInt(prop.getProperty(key));
		}
		catch(Exception E){
			throw new RuntimeException("参数："+key+"不是数字",E);
		}
	}
	static boolean  getBool(Properties prop,String key){
		try{
		    return Boolean.parseBoolean(prop.getProperty(key));
		}
		catch(Exception E){
			throw new RuntimeException("参数："+key+"不是boolean",E);
		}
	}

}

package org.hutu.mongo.config;

import java.util.ArrayList;
import java.util.List;

public class MongoConfigBean {

	List<ConfigHost>host=new ArrayList<>();
	String database;
	String username;
	String password;
	// ##mongo options
	boolean autoConnectRetry = true;
	// 每个主机答应的连接数（每个主机的连接池大小），当连接池被用光时，会被阻塞住
	int connectionsPerHost = 100;
	int threadsAllowedToBlockForConnectionMultiplier = 50;
	// 在建立（打开）套接字连接时的超时时间 （ms）
	int connectTimeout = 10000;
	// # 被阻塞线程从连接池获取连接的最长等待时间（ms）
	int maxWaitTime = 2000;
	boolean socketKeepAlive = true;
	// # 套接字超时时间（ms）
	int socketTimeout = 1500;
	// # 指明是否答应驱动从次要节点或者奴隶节点读取数据
	boolean slaveOk = false;
	int writeNumber = 1;
	int writeTimeout = 1000;
	boolean fsync = false; 
	public void addHost(String host,int port) {
		this.host.add(new ConfigHost(host,port));
	}
	 
	 
	public List<ConfigHost> getHost() {
		return host;
	}
	public void setHost(List<ConfigHost> host) {
		this.host = host;
	}
	public String getDatabase() {
		return database;
	}
	public void setDatabase(String database) {
		this.database = database;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public boolean isAutoConnectRetry() {
		return autoConnectRetry;
	}
	public void setAutoConnectRetry(boolean autoConnectRetry) {
		this.autoConnectRetry = autoConnectRetry;
	}
	public int getConnectionsPerHost() {
		return connectionsPerHost;
	}
	public void setConnectionsPerHost(int connectionsPerHost) {
		this.connectionsPerHost = connectionsPerHost;
	}
	public int getThreadsAllowedToBlockForConnectionMultiplier() {
		return threadsAllowedToBlockForConnectionMultiplier;
	}
	public void setThreadsAllowedToBlockForConnectionMultiplier(int threadsAllowedToBlockForConnectionMultiplier) {
		this.threadsAllowedToBlockForConnectionMultiplier = threadsAllowedToBlockForConnectionMultiplier;
	}
	public int getConnectTimeout() {
		return connectTimeout;
	}
	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}
	public int getMaxWaitTime() {
		return maxWaitTime;
	}
	public void setMaxWaitTime(int maxWaitTime) {
		this.maxWaitTime = maxWaitTime;
	}
	public boolean isSocketKeepAlive() {
		return socketKeepAlive;
	}
	public void setSocketKeepAlive(boolean socketKeepAlive) {
		this.socketKeepAlive = socketKeepAlive;
	}
	public int getSocketTimeout() {
		return socketTimeout;
	}
	public void setSocketTimeout(int socketTimeout) {
		this.socketTimeout = socketTimeout;
	}
	public boolean isSlaveOk() {
		return slaveOk;
	}
	public void setSlaveOk(boolean slaveOk) {
		this.slaveOk = slaveOk;
	}
	public int getWriteNumber() {
		return writeNumber;
	}
	public void setWriteNumber(int writeNumber) {
		this.writeNumber = writeNumber;
	}
	public int getWriteTimeout() {
		return writeTimeout;
	}
	public void setWriteTimeout(int writeTimeout) {
		this.writeTimeout = writeTimeout;
	}
	public boolean isFsync() {
		return fsync;
	}
	public void setFsync(boolean fsync) {
		this.fsync = fsync;
	}
	
	

}

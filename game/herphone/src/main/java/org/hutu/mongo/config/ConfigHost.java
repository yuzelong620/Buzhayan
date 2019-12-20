package org.hutu.mongo.config;

public class ConfigHost {

	private int port;
	private String ip;

	public ConfigHost(String ip, int port) {
		this.port = port;
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public String getIp() {
		return ip;
	}

	
}

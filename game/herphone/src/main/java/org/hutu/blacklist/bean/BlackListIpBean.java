package org.hutu.blacklist.bean;

public class BlackListIpBean {
	
	private String ip;// ip
	private int count;// 累計次數
	private long lastTime;// 上次記錄時間

	public BlackListIpBean(String ip, int count, long lastTime) {
		this.ip = ip;
		this.count = count;
		this.lastTime = lastTime;
	}

	public String getIp() {
		return ip;
	}

	public int getCount() {
		return count;
	}

	public long getLastTime() {
		return lastTime;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public void setLastTime(long lastTime) {
		this.lastTime = lastTime;
	}
	
	
    
}

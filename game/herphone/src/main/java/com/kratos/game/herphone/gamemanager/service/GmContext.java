package com.kratos.game.herphone.gamemanager.service;

import org.springframework.util.Assert;

/**
 * 从线程里获取管理员身份
 * @author Administrator
 *
 */
public class GmContext {
	 private static ThreadLocal<String> contextHolder = new ThreadLocal<>();

	    public static String getGm() {
	        return contextHolder.get();
	    }

	    public static void setGm(Object userName) {
	        Assert.notNull(userName, "不能为空");
	        contextHolder.set(userName.toString());
	    }
	    public static void setGm(String userName) {
	        Assert.notNull(userName, "不能为空");
	        contextHolder.set(userName);
	    }

	    public static void clear() {
	        contextHolder.remove();
	    }
}

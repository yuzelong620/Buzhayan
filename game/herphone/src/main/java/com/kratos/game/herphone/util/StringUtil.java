package com.kratos.game.herphone.util;

import org.junit.Test;

public class StringUtil {
	/**
	 * 
	 * @param strs
	 * @return
	 */
	public static String appendString(Object... strs) {
		StringBuffer sb = new StringBuffer();
		int index = 0;
		for (Object obj : strs) {
			if (index > 0) {
				sb.append("_");
			}
			sb.append(obj.toString());
			index++;
		}
		return sb.toString();
	}
	/**
	 * 
	 * @param strs
	 * @return
	 */
	public static String appendString(String pxf,Object... strs) {
		StringBuffer sb = new StringBuffer();
		int index = 0;
		for (Object obj : strs) {
			if (index > 0) {
				sb.append(pxf);
			}
			sb.append(obj.toString());
			index++;
		}
		return sb.toString();
	}
	public static boolean isNullAndEmpty(String str) {
		if (str == null||str.isEmpty()) {
			return true;
		}
		return false;
	}
}

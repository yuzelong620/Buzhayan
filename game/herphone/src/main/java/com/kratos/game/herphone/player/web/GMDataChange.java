package com.kratos.game.herphone.player.web;


import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.kratos.game.herphone.common.CommonCost;

import lombok.extern.log4j.Log4j;

@Log4j
public class GMDataChange {

	public static void recordChange(String messgae,Object data) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		log.info("接口名:" + request.getRequestURI() + "\t操作人:" + request.getSession().getAttribute("SessionInfo")
				+ "\t" + messgae + ":" + data);
	}
	
}

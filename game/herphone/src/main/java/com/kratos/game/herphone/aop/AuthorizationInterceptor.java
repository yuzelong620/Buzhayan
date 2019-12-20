package com.kratos.game.herphone.aop;

import com.alibaba.fastjson.JSON;
import com.kratos.engine.framework.common.CommonConstant;
import com.kratos.game.herphone.gamemanager.service.GmContext;

import com.kratos.game.herphone.json.datacache.GameParamsCache;
import com.kratos.game.herphone.player.PlayerContext;
import com.kratos.game.herphone.player.domain.Player;
import com.kratos.game.herphone.player.service.PlayerService;
import com.kratos.game.herphone.util.IpUtil;

import lombok.extern.log4j.Log4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j
@Component
public class AuthorizationInterceptor extends HandlerInterceptorAdapter {
	@Autowired
	private PlayerService playerService;
	@Value("${spring.profiles.active}")
	private String profile;
	public int status = -1;

	public boolean checkSwtich() {
		if (status == -1) {// 没有验证测试环境
			if ("test".equals(profile) || "dev".equals(profile)) {
				status = 1;// 需要输出请求信息
			} else {
				status = 0;// 不需要显示请求信息
			}
		}
		return (status == 1);
	}

	 
	/**检查白名单ip*/
	public boolean checkWhiteListIp(HttpServletRequest request, HttpServletResponse response) {
		String ip=IpUtil.getRemoteHost(request);
		List<String> list=GameParamsCache.getGameParams_Json().getWhitelistId();
		if(!list.contains(ip)) {
			sendError(response,"非法操作！");
			return false;
		}
		return true;
	}
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		checkSwtich();// 打印测试
		if(status==1){
			log.info("ip"+request.getRemoteAddr()+"-------->"+request.getServletPath());
		}		
		//请求方法检查
		if (request.getMethod().equalsIgnoreCase(RequestMethod.OPTIONS.name())) {
			return super.preHandle(request, response, handler);
		}
		//gm 功能登录回话检查
		if (request.getRequestURI().startsWith("/gm")) {
			return sessionLogin(request,response);
		}
		//系统消息，白名单检查
		if (request.getRequestURI().startsWith("/system/")) {
			return checkWhiteListIp(request,response);
		}
		
		final HandlerMethod handlerMethod = (HandlerMethod) handler;
		final Method method = handlerMethod.getMethod();
		final Class<?> clazz = method.getDeclaringClass();

		if (clazz.isAnnotationPresent(PrePermissions.class)) {
			PrePermissions clazzPermissions = clazz.getAnnotation(PrePermissions.class);
			if (clazzPermissions != null && clazzPermissions.required()) {
				if (method.isAnnotationPresent(PrePermissions.class)) {
					PrePermissions prePermissions = method.getAnnotation(PrePermissions.class);
					if (prePermissions != null && prePermissions.required()) {
						return hasLogin(request, response);
					}
				}
			}
		}

		return super.preHandle(request, response, handler);
	}

	private boolean hasLogin(HttpServletRequest request, HttpServletResponse response) {
		String token = request.getHeader("Authorization");

		Player player = playerService.findByToken(token);
		if (player == null) {
			sendError(response,"您未登录，请先登录后再进行操作！");
			return false;
		}
		PlayerContext.setPlayer(player);
		return true;
	}
	public boolean sessionLogin(HttpServletRequest request, HttpServletResponse response) {		
			if (request.getSession().getAttribute("SessionInfo") == null) {
				sendError(response,"您未登录，请先登录后再进行操作！");
				return false;
			}else {			
				GmContext.setGm(request.getSession().getAttribute("SessionInfo"));
				return true;
			}			
	}


	private void sendError(HttpServletResponse response,String errorInfo) {
		Map<String, Object> responseWithMap = new HashMap<>();
		responseWithMap.put("status", HttpStatus.UNAUTHORIZED.value());
		responseWithMap.put("message", errorInfo);
		responseWithMap.put("timestamp", new Date().getTime());
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setContentType(CommonConstant.CONTENT_TYPE);
		response.setCharacterEncoding(CommonConstant.UTF8);
		try (PrintWriter writer = response.getWriter()) {
			writer.write(JSON.toJSONString(responseWithMap));
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("Failed to response");
		}
	}
}

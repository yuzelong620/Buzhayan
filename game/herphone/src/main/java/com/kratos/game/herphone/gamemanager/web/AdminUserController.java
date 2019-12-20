package com.kratos.game.herphone.gamemanager.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kratos.game.herphone.common.BaseController;

@RestController
@RequestMapping("/gamemanager")
public class AdminUserController extends BaseController{
	
	@RequestMapping("/login")
	public boolean adminLogin(HttpServletRequest request,HttpServletResponse response,String username,String password) {
		if (adminUserService.adminLogin(username, password)) {
			HttpSession session = request.getSession(true);	
			session.setAttribute("SessionInfo", username);
			session.setMaxInactiveInterval(1 * 60 * 60);
			return true;
		}
		return false;
	}		
}

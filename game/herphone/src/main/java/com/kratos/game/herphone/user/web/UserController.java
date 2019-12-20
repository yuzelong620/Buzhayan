package com.kratos.game.herphone.user.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kratos.game.herphone.aop.PrePermissions;
import com.kratos.game.herphone.common.BaseController;

@RestController
@RequestMapping("/user")
@PrePermissions
public class UserController extends BaseController{
	/**
	 *获取用户绑定手机状态
	 *@return 0未绑定 1已绑定
	 */
    @GetMapping("/getUser")
    @PrePermissions
	public ResponseEntity<String> GetUser() {
    	return new ResponseEntity<String>(userService.GetUser(),HttpStatus.OK);
	}						
}

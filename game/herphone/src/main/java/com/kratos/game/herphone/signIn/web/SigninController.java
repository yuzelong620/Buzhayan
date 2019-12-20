package com.kratos.game.herphone.signIn.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.kratos.game.herphone.aop.PrePermissions;
import com.kratos.game.herphone.common.BaseController;
@RestController
@RequestMapping("/signin")
@PrePermissions
public class SigninController extends BaseController{
	/**签到 */
	@GetMapping("/doSignin")
	@PrePermissions
	public  ResponseEntity<?>  doSignin(){
		 return new ResponseEntity<>(signInService.doSignin(),HttpStatus.OK);
	}
	/**签到(获取签到信息)*/
	@PrePermissions
	@GetMapping("/getSignin")
	public  ResponseEntity<?>  getSignin(){ 
		 return new ResponseEntity<>(signInService.getSignin(),HttpStatus.OK);
	}

}
 
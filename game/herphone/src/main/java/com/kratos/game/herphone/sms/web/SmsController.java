package com.kratos.game.herphone.sms.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.kratos.engine.framework.net.socket.exception.BusinessException;
import com.kratos.game.herphone.aop.PrePermissions;
import com.kratos.game.herphone.common.BaseController;
import com.kratos.game.herphone.player.message.ResPlayerProfile;
import com.kratos.game.herphone.sms.message.ReqSmsCode;


@RestController
@RequestMapping("/sms")
@PrePermissions
public class SmsController extends BaseController{
	
	 /**
     * 发送手机验证码
     */
    @GetMapping("/sendPhoneCode/{mobile}")
    @PrePermissions(required = false)
    public ResponseEntity<?> sendPhoneCode(@PathVariable String mobile) {
    	smsService.sendVerifyCode(mobile,"");
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    /**
     * 手机绑定验证
     */
    @PostMapping(value="/verificationCode")
    @PrePermissions
    public ResponseEntity<ResPlayerProfile> verificationCode(@RequestBody ReqSmsCode param) {
       
        if (smsService.validateVerifyCode(param.getMobile(), "", param.getCode()) == false) {		
			//验证失败 
			throw new BusinessException("验证码错误或验证码已失效");
		}
    	//验证成功 用户绑定手机 增加体力
        return new ResponseEntity<ResPlayerProfile>(new ResPlayerProfile(userService.phoneBinding(param.getMobile())),HttpStatus.OK);
    }
    
}

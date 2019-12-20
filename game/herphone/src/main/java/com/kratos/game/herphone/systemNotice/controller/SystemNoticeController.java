package com.kratos.game.herphone.systemNotice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kratos.game.herphone.aop.PrePermissions;
import com.kratos.game.herphone.common.BaseController;
import com.kratos.game.herphone.systemNotice.bean.ChangeReadStateReq;

@RestController
@RequestMapping("/systemNotice")
@PrePermissions
public class SystemNoticeController extends BaseController{
	
	 //指定玩家系统通知
	@GetMapping("/ListSystemNotice/{page}")
	@PrePermissions
	public ResponseEntity<?> ListSystemNotice(@PathVariable String page) {
		return new ResponseEntity<>(systemNoticeService.getSystemNotice(Integer.valueOf(page)),HttpStatus.OK);
	}	
	 //获取未读数量
	@GetMapping("/getSystemNoticeUnReadNum")
	@PrePermissions
	public ResponseEntity<?> getSystemNoticeUnReadNum() {
		return new ResponseEntity<>(systemNoticeService.getSystemNoticeUnReadNum(),HttpStatus.OK);
	}
	
	//设置玩家系统消息已读
	@PostMapping("/setReadState")
	@PrePermissions
	public ResponseEntity<?> setReadState(@RequestBody ChangeReadStateReq req) {
		systemNoticeService.setReadState(req.getSystemNoticeIds());
		return new ResponseEntity<>(HttpStatus.OK);
	}
}

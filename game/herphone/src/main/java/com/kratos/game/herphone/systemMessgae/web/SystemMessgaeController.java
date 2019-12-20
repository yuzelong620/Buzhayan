package com.kratos.game.herphone.systemMessgae.web;

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
import com.kratos.game.herphone.systemMessgae.bean.SystemMessgaeInfoBean;

@RestController
@RequestMapping("/systemMessgae")
@PrePermissions
public class SystemMessgaeController extends BaseController{
	
	 //查找最新的
		@GetMapping("/getSystemMessgae")
		@PrePermissions
		public ResponseEntity<?> getSystemMessgae() {
			return new ResponseEntity<>(systemMessgaeService.getSystemMessge(),HttpStatus.OK);
		}	
	 //指定玩家聊天信息
		@GetMapping("/ListSystemMessgae/{page}")
		@PrePermissions
		public ResponseEntity<?> ListSystemMessgae(@PathVariable String page) {
			return new ResponseEntity<>(systemMessgaeService.listSystemMessge(Integer.valueOf(page)),HttpStatus.OK);
		}	
	//玩家向系统发送消息
		@PostMapping("/sendSystemMessgae")
		@PrePermissions
		public ResponseEntity<?> sendSystemMessgae(@RequestBody SystemMessgaeInfoBean systemMessgaeInfoBean) {
			systemMessgaeService.playerSendMessgae(systemMessgaeInfoBean.getContent(),systemMessgaeInfoBean.getMessageType());
			return new ResponseEntity<>(HttpStatus.OK);
		}
}

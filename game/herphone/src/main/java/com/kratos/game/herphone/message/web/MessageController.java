package com.kratos.game.herphone.message.web;

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
import com.kratos.game.herphone.message.bean.ChangeReadStateReq;
import com.kratos.game.herphone.message.bean.FindMessageReq;
import com.kratos.game.herphone.message.bean.SendMessage;

@RequestMapping("/message")
@PrePermissions
@RestController // ：@RestController注解相当于@ResponseBody ＋ @Controller合在一起的作用。
public class MessageController extends BaseController{
	//分页查询 ，私聊回话列表
	@PrePermissions
	@GetMapping("/findmessageInfos/{page}")
	public  ResponseEntity<?>  findMessageFirstInfo(@PathVariable String page){
		return new ResponseEntity<>(messageService.findMessageFirstInfo(Integer.parseInt(page)),HttpStatus.OK);
	}
	 //查找最新的
	@GetMapping("/newMessageInfo")
	@PrePermissions
	public ResponseEntity<?> findAttentions() {
		return new ResponseEntity<>(messageService.newMessageInfo(),HttpStatus.OK);
	}	
	//查询 与 指定用户的消息
	@PostMapping("/findMessage")
	@PrePermissions
	public ResponseEntity<?> findMessage(@RequestBody FindMessageReq req) {
		return new ResponseEntity<>(messageService.findMessage(req.getFromPlayerId(),req.getPage()),HttpStatus.OK);
	}
	//发送消息
	@PostMapping("/sendMessage")
	@PrePermissions
	public ResponseEntity<?> sendMessage(@RequestBody SendMessage message){
		messageService.sendMessage(message.getToPlayerId(),message.getContent(),message.getMessageType(),message.getAudioTime());
		return new ResponseEntity<>(HttpStatus.OK);
	}
	//设置已经读取状态
	@PostMapping("/setReadState")
	@PrePermissions
	public ResponseEntity<?> setReadState(@RequestBody ChangeReadStateReq req){
		messageService.setReadState(req.getMessageIds());
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	//删除好友消息
	@GetMapping("/removeMessage/{playerId}")
	@PrePermissions
	public ResponseEntity<?> removeMessage(@PathVariable String playerId){
		messageService.setReadState(Long.valueOf(playerId));
		return new ResponseEntity<>(HttpStatus.OK);
	}	
	@GetMapping("/deleteMessageRecord/{toPlayerId}")
	@PrePermissions
	public ResponseEntity<?> sendDelete(@PathVariable  Long toPlayerId ){
		
		messageService.setLimitTime(toPlayerId);
		return new ResponseEntity<>(messageService.findMessageFirstInfo(1),HttpStatus.OK);
	}
}
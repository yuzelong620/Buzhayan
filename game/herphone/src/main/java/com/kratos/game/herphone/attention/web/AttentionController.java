package com.kratos.game.herphone.attention.web;
 

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kratos.game.herphone.aop.PrePermissions;
import com.kratos.game.herphone.attention.dao.AttentionAuthorDao;
import com.kratos.game.herphone.common.BaseController;


@RequestMapping("/attention")
@PrePermissions
@RestController // ：@RestController注解相当于@ResponseBody ＋ @Controller合在一起的作用。
public class AttentionController extends BaseController {
	// 查找我的粉丝
	@GetMapping("/findFans/{page}")
	@PrePermissions
	public ResponseEntity<?> findAttentions(@PathVariable String page) {
		return new ResponseEntity<>(attentionService.seachFansPlayers(Integer.parseInt(page)), HttpStatus.OK);
	}
	@GetMapping("/seachAttentions/{page}")
	@PrePermissions
	// **查询我关注的用户 *
	public ResponseEntity<?> seachAttentions(@PathVariable String page) {
		return new ResponseEntity<>(attentionService.seachMyAttentionPlayers(Integer.parseInt(page)), HttpStatus.OK);
	}
	@GetMapping("/attentionPlayer/{toPlayerId}")
	@PrePermissions
	public ResponseEntity<?> attentionPlayer(@PathVariable String toPlayerId){
		attentionService.attentionPlayer(Long.parseLong(toPlayerId));
		return new ResponseEntity<>( HttpStatus.OK);
	}
	@GetMapping("/cancelAttentionPlayer/{toPlayerId}")
	@PrePermissions
	public ResponseEntity<?> cancelAttentionPlayer(@PathVariable String toPlayerId){
		attentionService.attentionCancel(Long.parseLong(toPlayerId));
		return new ResponseEntity<>( HttpStatus.OK);
	}
	/**
	 * 关注作者
	 * @param authorId
	 * @return
	 */
	@GetMapping("/attentionAuthor/{authorId}")
	@PrePermissions
	public ResponseEntity<?> attentionAuthor(@PathVariable String authorId){
		attentionService.attentionAuthor(Long.parseLong(authorId));
		return new ResponseEntity<>(HttpStatus.OK);
	}
	/**
	 * 取消对作者的关注
	 */
	@GetMapping("/cancelAttentionAuthor/{authorId}")
	@PrePermissions
	public ResponseEntity<?> cancelAttentionAuthor(@PathVariable String authorId){
		attentionService.attentionAuthorCancel(Long.parseLong(authorId));
		return new ResponseEntity<>( HttpStatus.OK);
	}
	
	/**
	 * 查询粉丝关注的所有作者
	 */
	@GetMapping("/allAttentionAuthor")
	@PrePermissions
	public ResponseEntity<?> allAttentionAuthor(){
		
		return new ResponseEntity<>(attentionService.findAttentionAuthor(), HttpStatus.OK);
	}
}

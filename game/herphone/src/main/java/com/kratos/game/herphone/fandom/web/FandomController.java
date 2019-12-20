package com.kratos.game.herphone.fandom.web;

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
import com.kratos.game.herphone.fandom.bean.FandomSeachReq;
import com.kratos.game.herphone.fandom.bean.SendFandomReq;

@RequestMapping("/fandom")
@PrePermissions
@Deprecated
@RestController // ：@RestController注解相当于@ResponseBody ＋ @Controller合在一起的作用。
public class FandomController extends BaseController {
	@PostMapping("/send")
	@PrePermissions
	public ResponseEntity<?> send(@RequestBody SendFandomReq req) {
		fandomService.send( req.getGameId(), req.getContent());
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PostMapping("/seach")
	@PrePermissions
	public ResponseEntity<?> find(@RequestBody FandomSeachReq req) {
		return new ResponseEntity<>(fandomService.seach( req.getGameId(), req.getPage()),HttpStatus.OK);
	}
	// 点赞
	@GetMapping("/like/{randomId}")
	@PrePermissions
	public ResponseEntity<?> addLike(@PathVariable String randomId) {
		fandomLikeService.addLike(randomId);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	
}

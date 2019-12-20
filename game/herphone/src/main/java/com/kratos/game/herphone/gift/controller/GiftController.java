package com.kratos.game.herphone.gift.controller;

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
import com.kratos.game.herphone.gift.bean.GiftBean;

@RestController
@RequestMapping("/Gift")
@PrePermissions
public class GiftController extends BaseController{

	
	/**
	 * 玩家送给作者礼物
	 */
	@PostMapping("/sendGift")
	@PrePermissions
	public ResponseEntity<?> sendGift(@RequestBody GiftBean giftBean) {
		giftService.sendGift(giftBean);
		return  new ResponseEntity<>(HttpStatus.OK);
	}
	
	/**
	 * 好感度排行榜
	 */
	@GetMapping("/giftRinkingList/{authorId}")
	@PrePermissions
	public ResponseEntity<?> giftRinkingList(@PathVariable long authorId) {
		return  new ResponseEntity<>(giftService.giftRinKingList(authorId),HttpStatus.OK);
	}
	
}

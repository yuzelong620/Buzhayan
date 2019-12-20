package com.kratos.game.herphone.gameHistory.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kratos.game.herphone.aop.PrePermissions;
import com.kratos.game.herphone.common.BaseController;

@RestController
@RequestMapping("/history")
@PrePermissions
public class GameHistoryController extends BaseController {
	/**
	 * 添加玩家副本记录
	 * @param page
	 * @return
	 */
	@GetMapping("/addHistory/{gameId}")
	@PrePermissions
	public ResponseEntity<?> addHistory(@PathVariable String gameId) {
		gameHistoryService.addHistory(gameId);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	/**
	 * 根据最新时间查询玩家副本历史记录
	 */
	@PrePermissions
	@GetMapping("/findHistory/{playerId}")
	public ResponseEntity<?> findHistoryByPlayerId(@PathVariable String playerId) {
		return new ResponseEntity<>(gameHistoryService.findHistoryByPlayerId(playerId),HttpStatus.OK);
	}
	
	/**
	 * 增加游戏剧本播放次数
	 */
	@PrePermissions
	@GetMapping("/addShowNum/{gameId}")
	public ResponseEntity<?> addGameShowNum(@PathVariable int gameId) {
		gameHistoryService.addGameShowNum(gameId);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	/**
	 * 查看副本播放次数
	 */
	@PrePermissions
	@GetMapping("/findShowNum/{gameId}")
	public ResponseEntity<?> findGameShowNum(@PathVariable int gameId) {
		return new ResponseEntity<>(gameHistoryService.findGameShowNum(gameId),HttpStatus.OK);
	}
}

package com.kratos.game.herphone.blackList.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kratos.game.herphone.aop.PrePermissions;
import com.kratos.game.herphone.common.BaseController;

@RestController
@RequestMapping("/blackList")
@PrePermissions
public class BlackListController extends BaseController{
	
	/**
	 * 拉黑
	 * @param playerId
	 * @return
	 */
	@PrePermissions(required = true)
    @GetMapping("/addBlackPlayer/{playerId}")
    public ResponseEntity<?> addBlackPlayer(@PathVariable String playerId) {
		long playerid = Long.valueOf(playerId);
    	return new ResponseEntity<>(blackListService.defriend(playerid),HttpStatus.OK); 
    }
	/**
	 * 取消拉黑
	 * @param playerId
	 * @return
	 */
	@PrePermissions(required = true)
    @GetMapping("/removeBlackPlayer/{playerId}")
    public ResponseEntity<?> removeBlackPlayer(@PathVariable String playerId) {
		long playerid = Long.valueOf(playerId);
		blackListService.removeBlackList(playerid);
    	return new ResponseEntity<>(HttpStatus.OK); 
    }
	/**
	 * 黑名单列表
	 * @param playerId
	 * @return
	 */
	@PrePermissions(required = true)
    @GetMapping("/listBlack")
    public ResponseEntity<?> listBlack() {
    	return new ResponseEntity<>(blackListService.getBlackList(),HttpStatus.OK); 
    }
}

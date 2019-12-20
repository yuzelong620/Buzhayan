package com.kratos.game.herphone.recentGame.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kratos.game.herphone.aop.PrePermissions;
import com.kratos.game.herphone.common.BaseController;
import com.kratos.game.herphone.recentGame.bean.ResRecentGameBean;
import com.kratos.game.herphone.recentGame.entity.RecentGameEntity;

@RestController
@RequestMapping("/recentgame")
@PrePermissions
public class RecentGameController extends BaseController{
	/**
	 * 获取别人的最近游戏列表
	 * */
    @GetMapping("/getRecentGame/{playerid}") 
    @PrePermissions
	public ResponseEntity<?> getRecentGame(@PathVariable long playerid) {
    	return new ResponseEntity<>(new ResRecentGameBean(recentGameService.load(playerid)),HttpStatus.OK);
	}				
   /**
    * 获取自己最近游戏列表
    * */
    @GetMapping("/getRecentGameByMyself") 
    @PrePermissions
	public ResponseEntity<?> getRecentGameByMySelf() {
    	return new ResponseEntity<>(new ResRecentGameBean(recentGameService.load()),HttpStatus.OK);
	}		
}

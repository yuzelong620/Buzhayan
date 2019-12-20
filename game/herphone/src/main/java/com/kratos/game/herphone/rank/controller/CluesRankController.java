package com.kratos.game.herphone.rank.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kratos.game.herphone.aop.PrePermissions;
import com.kratos.game.herphone.common.BaseController;

@RestController
@RequestMapping("/cluesRank")
@PrePermissions
public class CluesRankController extends BaseController{
	/**
	 * 获取排行榜
	 */
	@GetMapping("/listCluesRank")
	@PrePermissions
	  public ResponseEntity<?> listCluesRank() {
        return new ResponseEntity<>(cluesRankService.getMyselfRank(),HttpStatus.OK);
    }
}

package com.kratos.game.herphone.system.web;



import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kratos.game.herphone.aop.PrePermissions;
import com.kratos.game.herphone.common.BaseController;
import com.kratos.game.herphone.player.domain.Player;
import com.kratos.game.herphone.playerOnline.entity.PlayerOnlineEntity;
@RestController
@RequestMapping("/system")
@PrePermissions
public class SystemController extends BaseController {
@GetMapping("/sendtoken/{token}")

 	public	ResponseEntity<?> SystemFind(@PathVariable String token){
	Player player=playerService.findByToken(token);
	if(player==null) {
		return new  ResponseEntity<>(-1,HttpStatus.OK);
	}
	 return new ResponseEntity<>(player.getId(),HttpStatus.OK);
	}
@GetMapping("/settlementOnlineTime/{playerId}")

	public ResponseEntity<?> SystemSettle(@PathVariable long playerId){
	    PlayerOnlineEntity playerOnlineEntity=systemService.findTimeSettlement(playerId);
		return new ResponseEntity<>(playerOnlineEntity.getOnlineTime(), HttpStatus.OK);
	}
	
}

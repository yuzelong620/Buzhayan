package com.kratos.game.herphone.bag.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.kratos.game.herphone.aop.PrePermissions;
import com.kratos.game.herphone.common.BaseController;
import com.kratos.game.herphone.player.PlayerContext;
import com.kratos.game.herphone.player.domain.Player;
import com.kratos.game.herphone.player.message.ResPlayerProfile;

@RestController
@RequestMapping("/bag")
@PrePermissions
public class BagController extends BaseController{	
	@PrePermissions(required = true)
    @GetMapping("/getInfo")
    public ResponseEntity<?> getInfo() {
    	return new ResponseEntity<>(bagService.getBagInfo(), HttpStatus.OK); 
    }
	@PrePermissions(required = true)
    @PostMapping("/useItem/{itemId}")
    public ResponseEntity<?> useItem(@PathVariable String itemId) {
		int id=Integer.parseInt(itemId);
		bagService.useAddPowerValueItem(id,1);
		Player player=PlayerContext.getPlayer();
    	return new ResponseEntity<>(new ResPlayerProfile(player),HttpStatus.OK); 
    }
}

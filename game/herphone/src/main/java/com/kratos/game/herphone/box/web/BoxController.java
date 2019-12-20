package com.kratos.game.herphone.box.web;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.globalgame.auto.json.Box_Json;
import com.kratos.game.herphone.aop.PrePermissions;
import com.kratos.game.herphone.common.BaseController;
import com.kratos.game.herphone.json.JsonCacheManager;
import com.kratos.game.herphone.json.datacache.BoxCache;
import com.kratos.game.herphone.player.PlayerContext;
import com.kratos.game.herphone.player.domain.Player;
import com.kratos.game.herphone.player.message.ResPlayerProfile;
@RestController
@RequestMapping("/box")
@PrePermissions
public class BoxController extends BaseController {
	
	/**
	 * 开宝箱
	 * @return
	 */
	@PrePermissions
    @GetMapping("/openBox/{boxKind}")
    public ResponseEntity<?> openBox(@PathVariable int boxKind) {
    	return new ResponseEntity<>(boxService.openBox(boxKind) ,HttpStatus.OK); 
    }
}

package com.kratos.game.herphone.gameOver.web;

import org.springframework.data.convert.ReadingConverter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kratos.game.herphone.aop.PrePermissions;
import com.kratos.game.herphone.common.BaseController;

@RestController
@RequestMapping("/gameOver")
@PrePermissions
public class GameOverController extends BaseController {
	@GetMapping("/getAward")
	@PrePermissions
	public ResponseEntity<?> test() {
		return new ResponseEntity<>(gameOverService.gameOver(),HttpStatus.OK);
	}
}

package com.kratos.game.herphone.intimacy.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.kratos.game.herphone.aop.PrePermissions;
import com.kratos.game.herphone.common.BaseController;
import com.kratos.game.herphone.intimacy.bean.GameAddIntmacyReq;
import com.kratos.game.herphone.intimacy.bean.IntiamacyRes;

@RestController
@RequestMapping("/intimacy")
@PrePermissions
public class IntimacyController extends BaseController{
	@PostMapping("/getByPlayerId")//查询亲密度信息
	@PrePermissions
	 public ResponseEntity<?> getByPlayerId(@RequestBody IntiamacyRes res){	
		
		return new ResponseEntity<>(intimacyService.findAll(res.getPage()),HttpStatus.OK);
	}
	@PostMapping("/addIntimacy")//添加亲密度
	@PrePermissions
	public ResponseEntity<?> addIntimacy(@RequestBody GameAddIntmacyReq Req){
		intimacyService.addInimacy(Req.getId());
		return new ResponseEntity<>(HttpStatus.OK);
	}
} 
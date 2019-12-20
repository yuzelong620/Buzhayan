package com.kratos.game.herphone.homePage.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kratos.game.herphone.aop.PrePermissions;
import com.kratos.game.herphone.common.BaseController;
@PrePermissions
@Controller
@RequestMapping("/page")
public class HomePageController extends BaseController {
	/**
	 * 获取主页信息：
	 * 			1.玩家金币
	 * 			2.剧本播放数量
	 * 			3.剧本评论数量
	 */
	
	
	@GetMapping("/getPageInfo")
	@PrePermissions
	public ResponseEntity<?> findInfo() {
		return new ResponseEntity<>(homePageService.getPageInfo(),HttpStatus.OK);
	}
}

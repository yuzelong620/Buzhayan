package com.kratos.game.herphone.achievement.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kratos.game.herphone.aop.PrePermissions;
import com.kratos.game.herphone.common.BaseController;

@RestController
@RequestMapping("/achievement")
@PrePermissions
public class AchievementController extends BaseController{
	 /**
     * 解锁成就（新）
     */
    @GetMapping("/releaseAch/{id}")
    @PrePermissions
    public ResponseEntity<?> releaseAch(@PathVariable String id) {  
    	return new ResponseEntity<>( achievementService.addAchievement(Integer.parseInt(id)),HttpStatus.OK);
    }
    /**
     *获取我的成就碎片
     */
    @GetMapping("/reslistAchAndClue/{id}")
    @PrePermissions
    public ResponseEntity<?> reslistAchAndClue(@PathVariable String id) {  
    	return new ResponseEntity<>( achievementService.reslistAchAndClue(Integer.parseInt(id)),HttpStatus.OK);
    }
}

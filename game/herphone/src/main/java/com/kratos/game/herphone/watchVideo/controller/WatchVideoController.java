package com.kratos.game.herphone.watchVideo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kratos.game.herphone.aop.PrePermissions;
import com.kratos.game.herphone.common.BaseController; 
import com.kratos.game.herphone.watchVideo.bean.TakeWatchVideoRes;

@RestController
@RequestMapping("/watchVideo")
@PrePermissions
public class WatchVideoController extends BaseController{
	 /**
     * 领取查看广告
     */
    @PostMapping("/take")
    @PrePermissions
    public ResponseEntity<TakeWatchVideoRes> takeWatchVideo() {
        return new ResponseEntity<>(warchVideoService.takeWatchVideo(), HttpStatus.OK);
    }
	 /**
	  * /watchVideo/nextTime
     * 领取查看广告
     */
    @GetMapping("/nextTime")
    @PrePermissions
    public ResponseEntity<?> nextTime() {
        return new ResponseEntity<>(warchVideoService.nextTime(), HttpStatus.OK);
    }
}
package com.kratos.game.herphone.score.web;

import com.kratos.game.herphone.aop.PrePermissions;
import com.kratos.game.herphone.common.BaseController;
import com.kratos.game.herphone.score.message.ResScoreInfo;
import com.kratos.game.herphone.score.service.ScoreRewardService;
import com.kratos.game.herphone.score.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/score")
@PrePermissions
public class ScoreController extends BaseController{

    /**
     * 根据游戏id查询评分
     */
    @GetMapping("/{gameId}")
    @PrePermissions
    public ResponseEntity<Integer> getScore(@PathVariable String gameId) {
        return new ResponseEntity<>(scoreService.getScore(Integer.parseInt(gameId)), HttpStatus.OK);
    }
    
    /**
     * 2019/07/25
     * @author fjx
     * 新版接口
     * 根据游戏id查询是否领取过评分奖励
     */
    @GetMapping("/getScoreInfo/{gameId}")
    @PrePermissions
    public ResponseEntity<ResScoreInfo> getScoreInfo(@PathVariable String gameId) {
    	int state = ScoreRewardEntityService.selectScoreReward(Integer.parseInt(gameId));
    	Integer score = scoreService.getScore(Integer.parseInt(gameId));
    	ResScoreInfo scoreInfo = new ResScoreInfo(score,state);
        return new ResponseEntity<ResScoreInfo>(scoreInfo, HttpStatus.OK);
    }
    /**
     * 给游戏评分
     */
    @PostMapping("/{gameId}")
    @PrePermissions
    public ResponseEntity<Integer> rate(@PathVariable String gameId, @RequestBody Map<String, Integer> param) {
    	scoreService.rate(Integer.parseInt(gameId), param.get("score"));
        return new ResponseEntity<Integer>(ScoreRewardEntityService.jugeGetScoreReward(Integer.parseInt(gameId)),HttpStatus.OK);
    }

    /**
     * 查询所有游戏的评分
     */
    @GetMapping("/all")
    @PrePermissions
    public ResponseEntity<Map<Integer, String>> allScore() {
        return new ResponseEntity<>(scoreService.getAllScore(), HttpStatus.OK);
    }
}

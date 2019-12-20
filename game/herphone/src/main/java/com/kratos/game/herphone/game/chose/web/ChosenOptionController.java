package com.kratos.game.herphone.game.chose.web;

import com.kratos.game.herphone.aop.PrePermissions;
import com.kratos.game.herphone.game.chose.message.ReqPlayerChose;
import com.kratos.game.herphone.game.chose.message.ResEachExplorationRank;
import com.kratos.game.herphone.game.chose.message.ResGameProgress;
import com.kratos.game.herphone.game.chose.service.ChosenOptionService;
import com.kratos.game.herphone.player.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/game/choseOption")
@PrePermissions
public class ChosenOptionController {
    @Autowired
    private ChosenOptionService chosenOptionService;
    @Autowired
    private PlayerService playerService;

    /**
     * 做出选择
     */
    @PostMapping
    @PrePermissions
    public ResponseEntity<?> chose(@RequestBody ReqPlayerChose request) {
        chosenOptionService.chose(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 查询所有游戏的完成度
     */
    @GetMapping("/all")
    @PrePermissions
    public ResponseEntity<ResGameProgress> allProgress() {
        return new ResponseEntity<>(chosenOptionService.getAllProgress(), HttpStatus.OK);
    }

    /**
     * 查询单个游戏的完成度
     */
    @GetMapping("/{gameId}")
    @PrePermissions
    public ResponseEntity<Long> getProgress(@PathVariable String gameId) {
        return new ResponseEntity<>(chosenOptionService.getProgress(Integer.parseInt(gameId)), HttpStatus.OK);
    }

    /**
     * 查询所有游戏的完成度排行榜
     */
    @GetMapping("/rank")
    @PrePermissions
    public ResponseEntity<ResEachExplorationRank> allRank() {
        return new ResponseEntity<>(chosenOptionService.getAllRank(), HttpStatus.OK);
    }
}

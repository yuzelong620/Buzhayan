package com.kratos.game.herphone.clue.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kratos.game.herphone.aop.PrePermissions;
import com.kratos.game.herphone.clue.bean.ReqClueData;
import com.kratos.game.herphone.common.BaseController;

@RestController
@RequestMapping("/clue")
@PrePermissions
public class ClueController extends BaseController { 
	/**
	 * 增加线索
	 * @param clueId
	 * @return
	 */
	@PrePermissions
	@GetMapping("/add/{clueId}")
	public ResponseEntity<?> add(@PathVariable String clueId){
		return new ResponseEntity<>(playerClueService.add(Integer.parseInt(clueId)),HttpStatus.OK);
	}
	//获取我的所有获得的线索值
	@PrePermissions
	@GetMapping("/getClues/{gameId}")
	public ResponseEntity<?> getMyClues(@PathVariable String gameId){
		return new ResponseEntity<>(playerClueService.getMyClues(Integer.parseInt(gameId)),HttpStatus.OK);
	}
	
	/**
	 * 记录线索
	 */
	@PrePermissions
	@PostMapping("/recordClue")
	public ResponseEntity<?> recordClue(@RequestBody ReqClueData reqClueData){
		playerClueDataService.recordClue(reqClueData);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	/**
	 * 根据剧本ID和章节ID查询玩家所有线索
	 */
	@PrePermissions
	@PostMapping("/findClue")
	public ResponseEntity<?> findClue(@RequestBody ReqClueData reqClueData){
		return new ResponseEntity<>(playerClueDataService.findClue(reqClueData),HttpStatus.OK);
	}
	
}
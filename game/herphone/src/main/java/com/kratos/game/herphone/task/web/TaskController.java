package com.kratos.game.herphone.task.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kratos.game.herphone.aop.PrePermissions;
import com.kratos.game.herphone.common.BaseController;
import com.kratos.game.herphone.task.bean.PlayerGetAwardBean;

@RestController
@RequestMapping("/Task")
@PrePermissions
public class TaskController extends BaseController {

	/**
	 * 用户加载所有任务进度
	 */
	@GetMapping("/taskScheduleAll")
	@PrePermissions
	public ResponseEntity<?> taskScheduleAll() {
		return new ResponseEntity<>(taskService.taskScheduleAll(), HttpStatus.OK);
	}

	/**
	 * 用户获取每日视频
	 */
	@GetMapping("/getEveryDayVideo")
	@PrePermissions
	public ResponseEntity<?> getEveryDayVideo() {
		return new ResponseEntity<>(everyDayVideoService.getEveryDayVideo(), HttpStatus.OK);
	}

	/**
	 * 用户领取奖励
	 */
	@PostMapping("/playerGetAward")
	@PrePermissions
	public ResponseEntity<?> playerGetAward(@RequestBody PlayerGetAwardBean playerGetAwardBean) {
		return new ResponseEntity<>(taskService.playerGetAward(playerGetAwardBean), HttpStatus.OK);
	}
	
	/**
	 * 阶段任务列表
	 */
	@GetMapping("/stageTaskList")
	@PrePermissions
	public ResponseEntity<?> StageTaskList(){
		return new ResponseEntity<>(stageTaskService.StageTaskList(),HttpStatus.OK);
	}
	
	/**
	 * 阶段任务领取奖励
	 */
	@GetMapping("/stageTaskGetAward/{taskId}")
	@PrePermissions
	public ResponseEntity<?> stageTaskGetAward(@PathVariable int taskId){
		return new ResponseEntity<>(stageTaskService.taskGetAward(taskId),HttpStatus.OK);
	}
	
	
}

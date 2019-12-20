package com.kratos.game.herphone.json.datacache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.globalgame.auto.json.TaskAward_Json;
import com.kratos.game.herphone.json.JsonCache;

public class TaskAwardCache extends JsonCache<TaskAward_Json> {
	
	Map<Integer, List<TaskAward_Json>> typeMap = new HashMap<>();

	@Override
	protected void setDataCache(List<TaskAward_Json> datas) {
		Map<Integer, List<TaskAward_Json>> typeTemp = new HashMap<>();
		List<TaskAward_Json> taskAward_JsonList = new ArrayList<TaskAward_Json>();
		for (TaskAward_Json taskAward_Json : datas) {
			if(typeTemp.containsKey(taskAward_Json.getType())) {
				taskAward_JsonList = typeTemp.get(taskAward_Json.getType());
				taskAward_JsonList.add(taskAward_Json);
				typeTemp.put(taskAward_Json.getType(), taskAward_JsonList);
			} else {
				taskAward_JsonList = new ArrayList<TaskAward_Json>();
				taskAward_JsonList.add(taskAward_Json);
				typeTemp.put(taskAward_Json.getType(), taskAward_JsonList);
			}
		}
		typeMap = typeTemp;
	}
	
	/**
	 * 根据任务类型获取内容
	 */
	public List<TaskAward_Json> getTaskAward_Json(int type) {
		return typeMap.get(type);
	}
	
	
}

package com.kratos.game.herphone.json.datacache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.globalgame.auto.json.EveryDayVideo_Json;
import com.kratos.game.herphone.json.JsonCache;

public class EveryDayVideoCache extends JsonCache<EveryDayVideo_Json>{
	
	/**实现自己的缓存，请实现此方法*/
	Map<Integer, EveryDayVideo_Json> map = new HashMap<Integer, EveryDayVideo_Json>();
	
	@Override
	public void setDataCache(List<EveryDayVideo_Json> datas) {
		Map<Integer, EveryDayVideo_Json> temp = new HashMap<Integer, EveryDayVideo_Json>();
		for (EveryDayVideo_Json everyDayVideo_Json : datas) {
			temp.put(everyDayVideo_Json.getDay(), everyDayVideo_Json);
		}
		map = temp;
	}

	/**
	 * 根据天 获取视频内容
	 */
	public EveryDayVideo_Json getEveryDayVideo_Jsons(Integer day) {
		return map.get(day);
	}
}

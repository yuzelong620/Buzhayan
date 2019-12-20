package com.kratos.game.herphone.json.datacache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.globalgame.auto.json.VideoReward_Json;
import com.kratos.game.herphone.json.JsonCache;

public class VideoRewardCache extends JsonCache<VideoReward_Json>{
	
	/**实现自己的缓存，请实现此方法*/
	Map<Integer, VideoReward_Json> map = new HashMap<Integer, VideoReward_Json>();
	
	@Override
	public void setDataCache(List<VideoReward_Json> datas) {
		Map<Integer, VideoReward_Json> temp = new HashMap<Integer, VideoReward_Json>();
		for (VideoReward_Json videoReward_Json : datas) {
			temp.put(videoReward_Json.getDay(), videoReward_Json);
		}
		map = temp;
	}

	/**
	 * 根据天 获取助手内容
	 * @param day 第几天
	 * @return
	 */
	public VideoReward_Json getVideoReward_Json(Integer day) {
		return map.get(day);
	}
}

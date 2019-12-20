package com.kratos.game.herphone.json.datacache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.globalgame.auto.json.AchBadge_Json;
import com.kratos.game.herphone.json.JsonCache;

public class AchBadgeCache extends JsonCache<AchBadge_Json>{
	HashMap<Integer, List<Integer>> map = new HashMap<>();
	@Override
	protected void setDataCache(List<AchBadge_Json> datas) {
		HashMap<Integer, List<Integer>> map = new HashMap<>();
		for (AchBadge_Json achBadge_Json : datas) {		
			List<Integer> list =map.get(achBadge_Json.getFromTagId());
			if (list == null) {
				list = new ArrayList<Integer>();
				map.put(achBadge_Json.getFromTagId(), list);	
			}
				list.add(achBadge_Json.getId());				
		}		
		this.map = map;
	}
	/**根據tagID查出该小徽章ID集合*/
	public List<Integer> getAchBadgeId(int tagId) {
		return map.get(tagId);
	}
	public HashMap<Integer, List<Integer>> getMap() {
		return map;
	}
}

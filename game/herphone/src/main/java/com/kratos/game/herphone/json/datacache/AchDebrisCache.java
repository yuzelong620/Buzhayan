package com.kratos.game.herphone.json.datacache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.globalgame.auto.json.AchDebris_Json;
import com.kratos.game.herphone.json.JsonCache;

public class AchDebrisCache extends JsonCache<AchDebris_Json>{	
	HashMap<Integer, List<Integer>> map = new HashMap<>();
	
	@Override
	protected void setDataCache(List<AchDebris_Json> datas) {
		HashMap<Integer, List<Integer>> map = new HashMap<>();
		for (AchDebris_Json achDebris_Json : datas) {		
			List<Integer> list =map.get(achDebris_Json.getFromBadgeId());
			if (list == null) {
				list = new ArrayList<Integer>();
				map.put(achDebris_Json.getFromBadgeId(), list);	
			}
				list.add(achDebris_Json.getId());				
		}
		this.map = map;
	}
	/**根據小徽章ID查出该小徽章的碎片ID集合*/
	public List<Integer> getAchDebrisId(int badgeId) {
		return map.get(badgeId);
	}
	public HashMap<Integer, List<Integer>> getMap() {
		return map;
	}
	
}

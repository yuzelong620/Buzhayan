package com.kratos.game.herphone.json.datacache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.globalgame.auto.json.AchDebris_Json;
import com.globalgame.auto.json.Box_Json;
import com.kratos.game.herphone.json.JsonCache;

public class BoxCache extends JsonCache<Box_Json> {

	Map<Integer, List<Box_Json>> map = new HashMap<Integer, List<Box_Json>>();

	@Override
	protected void setDataCache(List<Box_Json> datas) {
		Map<Integer, List<Box_Json>> temp = new HashMap<Integer, List<Box_Json>>();

		for (Box_Json box_Json : datas) {
			List<Box_Json> list = temp.get(box_Json.getBoxKind());
			if (list == null) {
				list = new ArrayList<Box_Json>();
			}
			list.add(box_Json);
			temp.put(box_Json.getBoxKind(), list);
		}
		this.map = temp;
	}

	public List<Box_Json> getListByBoxKind(int boxKind) {
		return map.get(boxKind);
	}
	public Map<Integer, List<Box_Json>> getMap() {
		return map;
	}
}

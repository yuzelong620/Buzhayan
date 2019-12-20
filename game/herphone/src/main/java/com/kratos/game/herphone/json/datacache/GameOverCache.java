package com.kratos.game.herphone.json.datacache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.globalgame.auto.json.Box_Json;
import com.globalgame.auto.json.CreatRole_Json;
import com.globalgame.auto.json.GameOver_Json;
import com.kratos.game.herphone.json.JsonCache;

public class GameOverCache extends JsonCache<GameOver_Json> {
	Map<Integer, List<GameOver_Json>> map = new HashMap<Integer, List<GameOver_Json>>();
	
	@Override
	protected void setDataCache(List<GameOver_Json> datas) {
		Map<Integer, List<GameOver_Json>> temp = new HashMap<Integer, List<GameOver_Json>>();

		for (GameOver_Json GameOver_Json : datas) {
			List<GameOver_Json> list = temp.get(GameOver_Json.getId());
			if (list == null) {
				list = new ArrayList<GameOver_Json>();
			}
			list.add(GameOver_Json);
			temp.put(GameOver_Json.getId(), list);
		}
		this.map = temp;
	}
	
	public List<GameOver_Json> getListById(int id) {
		return map.get(id);
	}
	public Map<Integer, List<GameOver_Json>> getMap() {
		return map;
	}
}

package com.kratos.game.herphone.json.datacache;

import java.util.List;

import com.globalgame.auto.json.GameParams_Json;
import com.kratos.game.herphone.json.JsonCache;

public class GameParamsCache extends JsonCache<GameParams_Json> {
	static GameParams_Json gameParams_Json;

	public static GameParams_Json getGameParams_Json() {
		return gameParams_Json;
	}
	@Override
	protected void setDataCache(List<GameParams_Json> datas) {
		GameParamsCache.gameParams_Json = datas.get(0);
	}
}

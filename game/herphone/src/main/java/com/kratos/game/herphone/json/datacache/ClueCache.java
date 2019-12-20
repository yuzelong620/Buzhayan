package com.kratos.game.herphone.json.datacache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.globalgame.auto.json.Clue_Json;
import com.kratos.game.herphone.json.JsonCache;
import com.kratos.game.herphone.util.StringUtil;

public class ClueCache extends JsonCache<Clue_Json>{	
	@Override
	protected void setDataCache(List<Clue_Json> datas) {
		for(Clue_Json json:datas){
			String key=getKey(json.getGameid(), json.getChatid(), json.getCid());
			map.put(key, json);
		}
		
	}
	Map<String ,Clue_Json> map=new HashMap<String, Clue_Json>();
	/**
	 * 获取线索json
	 */
	public  Clue_Json getClue(int gameId,int chatId,int cId){
		String key=getKey(gameId, chatId, cId);
		return map.get(key);
	}
	private String getKey(int gameId, int chatId, int cId) {
		return StringUtil.appendString(gameId,chatId,cId);
	}
	
	
}

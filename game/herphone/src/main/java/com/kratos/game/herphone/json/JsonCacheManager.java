package com.kratos.game.herphone.json;

import java.io.File; 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.log4j.Logger;

import com.globalgame.auto.json.AchBadge_Json;
import com.globalgame.auto.json.Item_Json;
import com.globalgame.auto.json.LevelReward_Json;
import com.globalgame.auto.json.Tag_Json;
import com.kratos.engine.framework.net.socket.exception.BusinessException;
import com.kratos.game.herphone.json.datacache.AchBadgeCache;
import com.kratos.game.herphone.json.datacache.AchDebrisCache;
import com.kratos.game.herphone.json.datacache.AchievementAwardCache;
import com.kratos.game.herphone.json.datacache.AchievementCache;
import com.kratos.game.herphone.json.datacache.AdminCache;
import com.kratos.game.herphone.json.datacache.BoxCache;
import com.kratos.game.herphone.json.datacache.ClueCache;
import com.kratos.game.herphone.json.datacache.CostCurrencyCache;
import com.kratos.game.herphone.json.datacache.CreatRoleCache;
import com.kratos.game.herphone.json.datacache.DiscussTemplateCache;
import com.kratos.game.herphone.json.datacache.DynamicTempCache;
import com.kratos.game.herphone.json.datacache.EveryDayVideoCache;
import com.kratos.game.herphone.json.datacache.GameCatalogCache;
import com.kratos.game.herphone.json.datacache.GameOverCache;
import com.kratos.game.herphone.json.datacache.GameParamsCache;
import com.kratos.game.herphone.json.datacache.GiftCache;
import com.kratos.game.herphone.json.datacache.IntimacyCache;
import com.kratos.game.herphone.json.datacache.ItemCache;
import com.kratos.game.herphone.json.datacache.LevelCache;
import com.kratos.game.herphone.json.datacache.LevelRewardCache;
import com.kratos.game.herphone.json.datacache.SignInRewardCache;
import com.kratos.game.herphone.json.datacache.StageTaskCache;
import com.kratos.game.herphone.json.datacache.TagCache;
import com.kratos.game.herphone.json.datacache.TaskAwardCache;
import com.kratos.game.herphone.json.datacache.TitleCache;
import com.kratos.game.herphone.json.datacache.VideoRewardCache;
import com.mind.core.util.JsonUtil;

public class JsonCacheManager  {
    
	private JsonCacheManager() {
	}
    Logger logger=Logger.getLogger(JsonCacheManager.class);
    
	private static JsonCacheManager instance = new JsonCacheManager();

	public static JsonCacheManager getInstance() {
		return instance;
	}

	public Map<Class<?>, JsonCache<?>> getClassMappingCache() {
		return ClassMappingCache;
	}

	Map<Class<?>, JsonCache<?>> ClassMappingCache = new HashMap<>();

	public synchronized <T> void putJSONDataCache(JsonCache<T> cache) {
		JsonCache<?> cacheObj = ClassMappingCache.get(cache.getClass());
		if (cacheObj == null) {
			ClassMappingCache.put(cache.getClass(), cache);
			return;
		}
		//如果已经有cache,替换cache里的数据
		JsonCache<T> temp = (JsonCache<T>) cacheObj;
		temp.setList(cache.getList());
	}

	public static <T> T getCache(Class<? extends T> clz) {
		return (T) instance.ClassMappingCache.get(clz);
	}

	/**
	 * 加载全部json数据
	 */
	public static synchronized void reloadAll() {
		JsonCacheManager object = new JsonCacheManager();
		object.init();
		instance = object;
	}

	private void loadData(Class<? extends JsonCache>... clzs) {
		Map<String,List<String>> pathMap=getPathMapping();
		for (Class<? extends JsonCache> clz : clzs) {
			try {
				JsonCache obj = clz.newInstance();
				String key=obj.getJsonFileName();
				logger.info("get key:"+key);
				List<String> paths=pathMap.get(key);
				if(paths==null){
					throw new RuntimeException("没有找到对应的json 文件。cache:"+key);
				}
				List list =null;
				for(String path:paths){			   				  
					List items = JsonUtil.parseJsonArray(path, obj.EntityClass);
					if(list==null){
						list=items;
					}
					else{
						list.addAll(items);
					}
					logger.info("读取json文件：" + path);
				}
				obj.setList(list);
				putJSONDataCache(obj);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * 初始化
	 */
	public void init() {
		loadData(ItemCache.class,
				ClueCache.class,
				AchievementCache.class,
				GameCatalogCache.class,
				GameParamsCache.class,
				VideoRewardCache.class,
				TitleCache.class,
				IntimacyCache.class,
				CostCurrencyCache.class,
				CreatRoleCache.class,
				DiscussTemplateCache.class,
				AdminCache.class,
				EveryDayVideoCache.class,
				TaskAwardCache.class,
				DynamicTempCache.class,
				SignInRewardCache.class,
				LevelCache.class,
				LevelRewardCache.class,
				TagCache.class,
				AchBadgeCache.class,		
				AchDebrisCache.class,
				StageTaskCache.class,
				GiftCache.class,
				StageTaskCache.class,
				BoxCache.class,
				AchievementAwardCache.class,
				BoxCache.class,
				GameOverCache.class);
	}

	// /**
	// * 获得json 配置目录
	// * @return
	// */
	// private static String getJsonPath(){
	// if(jsonPath==null){
	// jsonPath="json/";
	// }
	// return jsonPath;
	// }
	private  Map<String,List<String>> getPathMapping() {
		File file = new File("json/");
		if (file.exists()) {
			logger.info("外部文件");
		} 
		else {
			logger.info("类路径文件");
			file = new File(JsonCacheManager.class.getResource("/").getPath() + "//json//"  );
		}
		Map<String,List<String>> map=new HashMap<>();
		for (File obj : file.listFiles()) { 
			String path=obj.getName();
			String pathKey=path;
			if(pathKey.contains("_")){
				pathKey=pathKey.substring(pathKey.lastIndexOf("_")+1,pathKey.length());
			}
			//首字母小写处理 。 Action.json  处理成 action.json 作为key
			pathKey=pathKey.substring(0,1).toLowerCase()+pathKey.substring(1,pathKey.length());
			
			List<String> list=map.get(pathKey);
			if(list==null){
				list=new ArrayList<>();
				map.put(pathKey, list);
			}
			list.add(path);
		}
		
		for(Entry<String, List<String>> entry:map.entrySet()){
			logger.info(entry.getKey()+"  <>  "+entry.getValue().toString()); 
		}
		return map;
	}
	/**json数据检查校验*/
	public void check() {
		AchDebrisCache achDebrisCache = JsonCacheManager.getCache(AchDebrisCache.class);
		AchBadgeCache achBadgeCache = JsonCacheManager.getCache(AchBadgeCache.class);
		TagCache tagCache = JsonCacheManager.getCache(TagCache.class);
		ItemCache itemCache = JsonCacheManager.getCache(ItemCache.class);
		/**检查achDebris与achBadge之间的关联是否错误*/
		List<AchBadge_Json> achBadge_Jsons = achBadgeCache.getList();
		for (AchBadge_Json achBadge_Json : achBadge_Jsons) {
			if (!achDebrisCache.getMap().containsKey(achBadge_Json.getId())) {		
				throw new RuntimeException("achDebris与achBadge关联错误: "+achBadge_Json.getId()+"在achDebris里没有关联");
			}
		}
		/**检查tag与achBadge之间的关联是否错误*/
		List<Tag_Json> tag_Jsons = tagCache.getList();
		for (Tag_Json tag_Json : tag_Jsons) {
			if (!achBadgeCache.getMap().containsKey(tag_Json.getId())) {
				throw new RuntimeException("tag与achBadge关联错误"+tag_Json.getId()+"在achBadge里没有关联");
			}
			for (Integer itemId : tag_Json.getList()) {
				if (itemCache.getData(itemId) == null) {
					throw new RuntimeException("tag奖励物品不存在");
				}
			}
		}
	}
	public static void main(String[] args) {
//		String pa="Action.json";
//		pa=pa.substring(0,1).toLowerCase()+pa.substring(1,pa.length());
//		System.out.println(pa);
		 
	}
 
}

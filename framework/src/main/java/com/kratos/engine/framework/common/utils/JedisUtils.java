package com.kratos.engine.framework.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kratos.engine.framework.base.SpringContext;
import com.kratos.engine.framework.cache.JedisConfiguration;
import redis.clients.jedis.GeoRadiusResponse;
import redis.clients.jedis.GeoUnit;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.params.geo.GeoRadiusParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JedisUtils {

    private JedisPool pool;
    private String namespace;
    private JedisConfiguration jedisConfiguration;
    private static JedisUtils instance;

    private JedisUtils() {
        pool = SpringContext.getBean(JedisPool.class);
        jedisConfiguration = SpringContext.getBean(JedisConfiguration.class);
        namespace = jedisConfiguration.getNamespace() == null ? "" : jedisConfiguration.getNamespace();
    }

    public static JedisUtils getInstance() {
        if (instance == null) {
            instance = new JedisUtils();
        }
        return instance;
    }

    /**
     * 获得Jedis对象
     */
    private Jedis getJedis() {
        Jedis jedis = null;
        if (pool != null) {
            jedis = pool.getResource();
        }
        return jedis;
    }

    private String getKey(String key) {
        return String.format("%s:%s", namespace, key);
    }

    /**
     * 释放资源(归还操作)
     */

    public void close(Jedis jedis) {
        if (jedis != null)
            jedis.close();
    }

    public Long incr(String key) {
        Jedis jedis = getJedis();
        Long result = jedis.incr(getKey(key));
        jedis.close();
        return result;
    }

    public Long decr(String key) {
        Jedis jedis = getJedis();
        Long result = jedis.decr(getKey(key));
        jedis.close();
        return result;
    }

    public void set(String key, Object value) {
        Jedis jedis = getJedis();
        boolean keyExist = jedis.exists(getKey(key));
        // NX是不存在时才set， XX是存在时才set， EX是秒，PX是毫秒
        if (keyExist) {
            jedis.del(getKey(key));
        }
        jedis.set(getKey(key), JSON.toJSONString(value), "NX", "EX",
                jedisConfiguration.getExpiration().intValue());
        jedis.close();
    }
    //设置过期时间的key
    public boolean setexpire(String key, Object value,int time) {
    	 Jedis jedis = getJedis();
    	 try {
    		   String state = jedis.set(getKey(key), JSON.toJSONString(value), "NX", "EX",time);    
    		   if ("OK".equals(state)) {
    			   return true;
    		   }else 
    			   return false;
		} 
    	 finally {
    		 jedis.close();   		 
		}
    	 
	}
    public <T> T get(String key, Class<T> clazz) {
        Jedis jedis = getJedis();
        boolean keyExist = jedis.exists(getKey(key));
        String json = jedis.get(getKey(key));
        jedis.close();
        if (StringHelper.isBlank(json)) {
            return null;
        }

        if (keyExist) {
            return JSON.parseObject(json, clazz);
        }

        return null;
    }

    public <T> List<T> getList(String key, Class<T> clazz) {
        Jedis jedis = getJedis();
        boolean keyExist = jedis.exists(getKey(key));
        String json = jedis.get(getKey(key));
        jedis.close();
        if (StringHelper.isBlank(json)) {
            return null;
        }

        if (keyExist) {
            return JSON.parseArray(json, clazz);
        }

        return null;
    }

    public void del(String key) {
        Jedis jedis = getJedis();
        boolean keyExist = jedis.exists(getKey(key));
        // NX是不存在时才set， XX是存在时才set， EX是秒，PX是毫秒
        if (keyExist) {
            jedis.del(getKey(key));
        }
        jedis.close();
    }

    public long hSet(String key, String field, Object value) {
        return hSet(key, field, JSON.toJSONString(value));
    }

    public long hSet(String key, String field, String value) {
        Jedis jedis = getJedis();
        Long hSet = jedis.hset(getKey(key), field, value);
        jedis.close();
        return hSet;
    }

    public <T> T hGet(String key, String field, Class<T> clazz) {
        String json = hGet(key, field);
        if (StringHelper.isBlank(json)) {
            return null;
        }
        return JSON.parseObject(json, clazz);
    }

    public String hGet(String key, String field) {
        Jedis jedis = getJedis();
        String hget = jedis.hget(getKey(key), field);
        jedis.close();
        return hget;
    }

    public List<GeoRadiusResponse> geoRadius(String key, double lgt, double lat, double radius, GeoUnit unit) {
        GeoRadiusParam geoRadiusParam = GeoRadiusParam.geoRadiusParam();
        geoRadiusParam.withDist();
        geoRadiusParam.sortAscending();
        Jedis jedis = getJedis();
        return jedis.georadius(getKey(key), lgt, lat, radius, unit, geoRadiusParam);
    }

    public long hDel(String key, String... field) {
        Jedis jedis = getJedis();
        Long hDel = jedis.hdel(getKey(key), field);
        jedis.close();
        return hDel;
    }

    public GeoRadiusParam getRadiusParam() {
        GeoRadiusParam geoRadiusParam = GeoRadiusParam.geoRadiusParam();
        geoRadiusParam.withDist();
        geoRadiusParam.sortAscending();
        return geoRadiusParam;
    }

    public <T> List<T> getAll(String key, Class<T> clazz) {

        Jedis jedis = getJedis();
        Map<String, String> map = jedis.hgetAll(getKey(key));
        jedis.close();
        Set<String> keys = map.keySet();
        List<T> list = new ArrayList<>();
        for (String mapKey : keys) {
            JSONObject jsonObject = JSON.parseObject(map.get(mapKey));
            list.add(JSONObject.toJavaObject(jsonObject, clazz));
        }

        return list;
    }
    public long ttl(String key) {
    	Jedis jedis = getJedis();
    	long vtime = jedis.ttl(getKey(key));
    	jedis.close();
    	return vtime;
    }
}

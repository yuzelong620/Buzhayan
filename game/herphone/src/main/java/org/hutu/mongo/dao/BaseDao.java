package org.hutu.mongo.dao; 
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.WriteResult; 

public abstract class BaseDao<T> {
	 static Logger logger=LoggerFactory.getLogger(BaseDao.class);
	public static void setMongoTemplate(MongoTemplate mongoTemplate){
		BaseDao.mongoTemplate=mongoTemplate;
	}
	protected static MongoTemplate mongoTemplate;
	public Class<T> entityClass; 
	MongoTemplate getPlayerMongoTemplate(){
		return mongoTemplate;
	}
	/**
	 * 通过反射获取子类确定的泛型类
	 */
	@SuppressWarnings("unchecked")
	public BaseDao(){
		Type genType = getClass().getGenericSuperclass();
		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
		entityClass = (Class<T>) params[0];
	} 

	public long count(Query query) {

		return getPlayerMongoTemplate().count(query, entityClass);
	}

	public List<T> find(Query query) {

		return getPlayerMongoTemplate().find(query, entityClass);
	}

	public T findOne(Query query) {

		return getPlayerMongoTemplate().findOne(query, entityClass);
	}

	public T findByID(Object id) {
		
		return getPlayerMongoTemplate().findById(id, entityClass);
	}

	public T findByID(int id, String dbname) {

		return getPlayerMongoTemplate().findById(id, entityClass, dbname);
	}

	public void insert(T obj) {
		getPlayerMongoTemplate().insert(obj);
	}
	
	public void updateInsert(Query query, Update update){
		getPlayerMongoTemplate().upsert(query, update, entityClass);
	}
	
	public void insertAll(Collection<T> objectsToSave) {
		getPlayerMongoTemplate().insertAll(objectsToSave);
	}

	public void remove(Query query) {
		getPlayerMongoTemplate().remove(query, entityClass);
	}
	
	public void delete(T obj) {
		getPlayerMongoTemplate().remove(obj);
	}

	public void deleteById(Object id) {
		Query query = new Query(Criteria.where("_id").is(id));
		getPlayerMongoTemplate().remove(query, entityClass);
	}

	public String getCollectionName(){
		return getPlayerMongoTemplate().getCollectionName(entityClass);
	}
	
	public DBCollection getCollection(String collectionName){
		return getPlayerMongoTemplate().getCollection(collectionName);
	}
	
	/**
	 * 
	 * @param query
	 * @param update
	 * @return the old object
	 */
	public T findAndModify(Query query, Update update) {

		return getPlayerMongoTemplate().findAndModify(query, update, entityClass);
	}

	public T findAndModify(Query query, Update update, int serverId) {

		return getPlayerMongoTemplate().findAndModify(query, update, entityClass);
	}

	public boolean updateFirst(Query query, Update update) {
		WriteResult result=getPlayerMongoTemplate().updateFirst(query, update, entityClass);
		
		return result.isUpdateOfExisting();
	}

	public void updateMulti(Query query, Update update) {
		getPlayerMongoTemplate().updateMulti(query, update, entityClass);

	}
	/**
	 * 获取异步存储对象
	 */
	public Runnable getAsynSaveTask(T obj) {
	    return new Runnable(){
			public void run() {
				 save(obj);
			}
		}; 
	}
	public void save(T obj) {
		getPlayerMongoTemplate().save(obj);
	}
	
	/**
	 * 将所有String参数链接成为一个MongoDB查询字段要求的格式字符串
	 * 例如：将“player”和“diamond”连接成“player.diamond”
	 * @param fieldSubs
	 * @return
	 */
	public static String formatKey(String... fieldSubs) {
		StringBuffer strBuf = new StringBuffer();
		for (String sub : fieldSubs) {
			strBuf.append(sub).append(".");
		}
		String ret = strBuf.toString();
		return ret.substring(0, ret.length()-1);
	}
	 @Deprecated//咋时注释掉
	public void updateVersionTransaction(Query query, Update update, boolean save) {
        update.inc("version", 1);
        FindAndModifyOptions findAndModifyOptions = FindAndModifyOptions.options().upsert(save);
        T checkUpdate = getPlayerMongoTemplate().findAndModify(query, update, findAndModifyOptions, entityClass);
        if (checkUpdate == null) {
            if (save) {
                // 第一次保存总是返回 checkUpdate == null， 忽略
                // logger.info("updateTransaction null, PaymentService, query=" + query + ", update=" + update);
            } else {
//                logger.info("updateTransaction exception, PaymentService, query=" + query + ", update=" + update);
            }
        }
    }
	/**
	 * 原子操作 加减数值。将query 查询到的第一个文档，字段名为key的值 加 num ，key字段必须为数字，否则会报错
	 * @param query 
	 * @param key
	 * @param num 
	 */
	public void inc(Query query, String key, Number num){
//	Query query = new Query(Criteria.where("_id").is(playerId).and("version").is(version));
	Update update = new Update();
	update.inc(key,(Number) num);
	mongoTemplate.updateFirst(query, update,entityClass);//更新第一个
}

//	public void incByPlayerId(long playerId, String key, Number num, long version) throws ErrorException {
//		Query query = new Query(Criteria.where("_id").is(playerId).and("version").is(version));
//		Update update = new Update();
//		update.inc(key, (Number) num);
//		updateVersion(query, update);
//	}
	
//	/**
//	 * 一次更新多个数值
//	 * @param playerId
//	 * @param map
//	 * @param version
//	 * @throws ErrorException
//	 */
//	public void incByPlayerId(long playerId, Map<String, Number> map, long version) throws ErrorException {
//		Query query = new Query(Criteria.where("_id").is(playerId).and("version").is(version));
//		Update update = new Update();
//		for (String key : map.keySet()) {
//			update.inc(key, (Number)map.get(key));
//		}
//		updateVersion(query, update);
//	}
	
  public  List<T> distinct(String fieldName){
	  return getPlayerMongoTemplate().getCollection(this.getCollectionName()).distinct(fieldName);
  }
	
}

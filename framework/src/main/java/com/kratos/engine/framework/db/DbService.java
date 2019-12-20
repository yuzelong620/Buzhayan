package com.kratos.engine.framework.db;

import java.util.concurrent.atomic.AtomicBoolean;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskTimeoutException;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.kratos.engine.framework.common.thread.NamedThreadFactory;
import com.kratos.engine.framework.common.utils.BlockingUniqueQueue;
import com.kratos.engine.framework.thread.TaskInterface;
import com.kratos.engine.framework.thread.WorkTask;
import com.kratos.engine.framework.thread.TaskGroup;

import lombok.extern.log4j.Log4j;

/**
 * 用户数据异步持久化的服务
 * 
 * @author herton
 */
@Log4j
@Component
public class DbService {
	
    @Autowired
    private EntityManagerFactory entityManagerFactory;

	public DbService() {
//        new NamedThreadFactory("db-save-service").newThread(new Worker()).start();
	}

	private BlockingUniqueQueue<BaseEntity> queue = new BlockingUniqueQueue<>();

//	private final AtomicBoolean run = new AtomicBoolean(true);
	/**
	 * 存入任务。如果需要使用 hash一致性 操作，请重写run参数的hashcode方法。
	 * @param run
	 */
	@Deprecated
	public  static void putTask(Runnable run){
		DbService.group.hashPut(run);
	}
    static TaskGroup<TaskInterface> group;
    public static void close(){
    	group.close();
    }
	public synchronized static void init(int size){
		if(group!=null){
			return;
		}
		String name= "db-save-service";
		TaskInterface[] threads=new TaskInterface[size]; 
		for(int  i=0;i<size;i++){
			WorkTask t=new WorkTask(name+"_"+i);
			threads[i]=t;
		}
		group=new TaskGroup<>(threads); 
		group.start();
	}
	
	public Runnable getRunnbaleTask(BaseEntity entity){
		Runnable run=new Runnable(){
			@Override
			public void run() {
				saveToDb(entity);
			}
			@Override
			public String toString() { 
				return JSONObject.toJSONString(entity);
			}
			@Override
			public int hashCode() {
				return entity.getId().hashCode();
			}
		};
		return run;
	}
	
	public void add2Queue(BaseEntity entity) {
		DbService.group.hashPut(getRunnbaleTask(entity));
//		this.queue.add(entity);
	}

//	private class Worker implements Runnable {
//		@Override
//		public void run() {
//			while (run.get()) {
//				BaseEntity entity = null;
//				try {
//					entity = queue.take();
//					saveToDb(entity);
//				} catch (Throwable e) {
//					log.error("报错重新放入队列，对象"+JSONObject.toJSONString(entity), e);
//					// 有可能是并发抛错，重新放入队列
//					add2Queue(entity);
//				}
//			}
//		}
//	}

	/**
	 * 数据真正持久化
	 * 
	 * @param entity
	 */
	private void saveToDb(BaseEntity entity) {
		entity.doBeforeSave();
        entity.autoSetStatus();
        EntityManager em =null;
        EntityTransaction transaction=null;
        try {
        	em = entityManagerFactory.createEntityManager();
            transaction = em.getTransaction();
            transaction.begin();
            if(entity.isInsert()) {
				entity.markPersistent();
				em.merge(entity);
			} else if(entity.isUpdate()) {
				em.merge(entity);
			} else if(entity.isDelete()) {
				entity = em.merge(entity);
            	em.remove(entity);
			}
            transaction.commit();//提交事务
            entity.resetDbStatus();
        }
        catch (Exception e) {
            log.error("", e);
            if(transaction!=null){//报错回滚数据
            	transaction.rollback();
            }
        } 
        finally {
        	if(em!=null){
               em.close();
        	}
        }
	}

}

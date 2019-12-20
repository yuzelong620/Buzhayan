package com.kratos.engine.framework.thread;

import org.apache.log4j.Logger;

public class TaskGroup<T extends TaskInterface> {
	
    Logger loger=Logger.getLogger(TaskGroup.class);
    public T[] threads;
	public TaskGroup(T[] threads) {
		this.threads=threads;
	}
	public void start(){
		for(T t:threads){
			t.start();
		}
	}
	
	public void close(){
		//关闭所有线程的开关
		for(T t:threads){
			t.close();
		}
		for(T t:threads){
			while(t.isOpen()){//一直等到 所有线程都关闭
				try{
				    Thread.sleep(4000);
				    loger.info("正在关闭。。。。。。"+t.getName()+"线程，还有任务数量："+t.getTaskSize());
				}
				catch(Exception e){
					loger.error("",e);
				}
			}
		}
	} 
 
	public T getLeastTaskThread() {
		T temp=null;
		int size=0;
		for(T t:threads){
			 if(t.getTaskSize()==0){//线程中没有等待执行的任务
				 return t;
			 }
			 if(temp==null||size>t.getTaskSize()){//寻找任务少的线程
				 size=t.getTaskSize();
				 temp=t;
			 }
		}
		return temp;
	}
   
   public T getThreadByHash(int hash){
	   if(hash<0){
		   hash=-hash;
	   }
	   int index=hash%threads.length;
	   return threads[index];
   }
	 
   public void hashPut(Runnable task){
	   T t=getThreadByHash(task.hashCode());
	   t.put(task);
   }

}

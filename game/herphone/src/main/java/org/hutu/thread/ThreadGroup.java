package org.hutu.thread;

import org.apache.log4j.Logger;

public class ThreadGroup<T extends Work> {
	
    Logger loger=Logger.getLogger(ThreadGroup.class);
    public T[] threads;
	public ThreadGroup(T[] threads) {
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
				    Thread.sleep(5);
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

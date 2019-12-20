package com.kratos.game.herphone.event;

import org.apache.log4j.Logger;
import org.hutu.thread.ThreadGroup;
import org.hutu.thread.Work;
import org.hutu.thread.WorkThread;

/**
 * 工作任务管理器
 */
public class WorkManager {
    Logger log=Logger.getLogger(WorkManager.class);
	private static WorkManager instance = new WorkManager();
	public static WorkManager getInstance() {
		return instance;
	}
	private WorkManager() {
	}
	

	ThreadGroup<Work> logicGroup;//逻辑组 ，用于异步执行逻辑
	ThreadGroup<Work> noticeGroup;//http组  用于异步发送http


	
	
	boolean open = false; 
	/** 初始化 */
	public synchronized void init(int workSize) {
		if (open) {
			return;
		}
		ThreadGroup<Work> group = initThread(workSize, "main");
		group.start();
		this.logicGroup = group;
		
		ThreadGroup<Work> noticeGroup = initThread(workSize, "main");
		noticeGroup.start();
		this.noticeGroup=noticeGroup;
		open = true;
	}

	public void stop() {
		this.logicGroup.close();
	}

	private ThreadGroup<Work> initThread(int size, String threadName) {
		Work[] threads = new Work[size];
		for (int i = 0; i < size; i++) {
			threads[i] = new WorkThread(threadName + "_" + i);
		}
		ThreadGroup<Work> group = new ThreadGroup<>(threads);
		return group;
	}
    /**
     * 存入逻辑任务
     * @param task
     */
	public void putEvent(Runnable task) {
		if(!open){//开关没有开启
			task.run();
			return;
		}
		try{
			this.logicGroup.hashPut(task);
		}
		catch(Exception e){
			log.error("",e);
		}
	}
	/**
	 * 存入通知任务
	 */
	public void putNoticeTask(Runnable task){
		if(!open){//开关没有开启
			return;
		}
		try{
			this.noticeGroup.hashPut(task);
		}
		catch(Exception e){
			log.error("",e);
		}
	}

}

package org.hutu.timer.manager;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import org.hutu.timer.event.MarkTask;
 
/**  
 * 定时管理器
 * @author zxd  
 */
public final class TimerManager{
	
	public static final TimerManager instance = new TimerManager();
	
	final Timer timer;

	TimerManager() {
		timer = new Timer();
	}

	// 只执行一次
	public void schedule(Runnable task, long delay) {
        timer.schedule(getTask(task), delay);
	}

	// 循环执行
	public void schedule(Runnable task, long delay, long period) {
		timer.schedule(getTask(task), delay,period);
	}
	
	//异步执行任务
	private TimerTask  getTask(Runnable run) {
		return new TimerTask() {
			public void run() {
//				TaskManager.instance.hashPutOtherTask(run);
				runTask(run);
			}
		};
	}
	
	private void runTask(Runnable run){
		run.run();
	}
	
	ConcurrentHashMap<String, MarkTask> taskMap=new ConcurrentHashMap<>();
	
	public void scheduleMarkTask(MarkTask task, long delay) {
		MarkTask oldTask=taskMap.remove(task.getMark());
		if(oldTask!=null){
			oldTask.cancel();
		}
		taskMap.put(task.getMark(),task);
		schedule(task, delay);
	}
	public boolean cancelMarkTask(MarkTask markTask){
		return taskMap.remove(markTask.getMark(), markTask); 
	}
}

package org.hutu.thread;
 
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

public class WorkThread extends Work {
	 
	public WorkThread(String name) {
		super.setName(name);
	}
	Logger logger = Logger.getLogger(WorkThread.class);

	/* (non-Javadoc)
	 * @see com.crxl.xllxj.module.core.thread.Work#getTaskSize()
	 */
	@Override
	public  int getTaskSize(){
		return queue.size();
	}
	protected LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>();
	protected volatile boolean open = true;// 关闭装填

	/* (non-Javadoc)
	 * @see com.crxl.xllxj.module.core.thread.Work#isOpen()
	 */
	@Override
	public boolean isOpen(){
		return open;
	}
	/* (non-Javadoc)
	 * @see com.crxl.xllxj.module.core.thread.Work#put(java.lang.Runnable)
	 */
	@Override
	public void put(Runnable run) {
		if(!open){//开关没有打开 ，不做处理
			logger.error("线程开关关闭，不能发消息");
			return;
		}
		queue.add(run);
	}
	/* (non-Javadoc)
	 * @see com.crxl.xllxj.module.core.thread.Work#close()
	 */
	@Override
	public void close(){
		WorkThread work=this;
	   Runnable closeTask=new Runnable(){
		public void run() {
			work.open=false;
		}
	   };
 	   queue.add(closeTask);
	}

	/* (non-Javadoc)
	 * @see com.crxl.xllxj.module.core.thread.Work#run()
	 */
	@Override
	public void run() {
		while (open) {
			try {
				Runnable task = queue.take();
				task.run();
			}
			catch (Exception e) {
				logger.error("", e);
			} 
			catch (Throwable e){
				logger.error("", e);
			}
		}
	}
	
   

}

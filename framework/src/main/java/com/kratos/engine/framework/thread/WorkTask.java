package com.kratos.engine.framework.thread;
 
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

public class WorkTask extends TaskInterface {
	 
	public WorkTask(String name) {
		super.setName(name);
	}
	Logger logger = Logger.getLogger(WorkTask.class);

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
		queue.add(run);
	}
	/* (non-Javadoc)
	 * @see com.crxl.xllxj.module.core.thread.Work#close()
	 */
	@Override
	public void close(){
		WorkTask work=this;
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
			Runnable task=null;
			try {
				task = queue.take();
				task.run();
			}
			catch (Exception e) {
				if(task!=null){
					logger.error("报错："+task.toString(),e);
				}
				else{
					logger.error("", e);
				}
			} 
			catch (Throwable e){
				logger.error("", e);
			}
		}
	}
	
   

}

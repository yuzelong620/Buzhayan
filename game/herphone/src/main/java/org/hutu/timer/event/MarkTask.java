package org.hutu.timer.event; 
import org.hutu.timer.manager.TimerManager;
public abstract class MarkTask implements Runnable{
	
	private boolean cancelled;

 
	public abstract String getMark();

    @Override
    public final int hashCode() {
    	return this.getMark().hashCode();
    }
    
	public final void run() {
		if (!cancelled) {
			execute();
		}
		TimerManager.instance.cancelMarkTask(this);
	}
	
	public final void cancel(){
		this.cancelled=true;
	}
	
	public abstract void execute();
	 
}

package com.kratos.engine.framework.thread;

public abstract class TaskInterface extends Thread{

	public abstract	int getTaskSize();

	public abstract	boolean isOpen();

	public abstract void put(Runnable run);

	public abstract	void close(); 
	 

}
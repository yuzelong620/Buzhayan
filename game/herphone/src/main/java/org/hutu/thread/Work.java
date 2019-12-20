package org.hutu.thread;

public abstract class Work extends Thread{

	public abstract	int getTaskSize();

	public abstract	boolean isOpen();

	public abstract void put(Runnable run);

	public abstract	void close(); 
	 

}
package com.kratos.engine.framework.net.socket.task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class GameExector {

	private static GameExector instance = new GameExector();
	
	private final int CORE_SIZE = Runtime.getRuntime().availableProcessors();
	/** task worker pool */
	private final ExecutorService[] workerPool = new ExecutorService[CORE_SIZE];

	private final AtomicBoolean run = new AtomicBoolean(true);

	private GameExector () {
		for (int i=0; i<CORE_SIZE; i++) {
			workerPool[i] = Executors.newSingleThreadExecutor();
		}
		instance = this;
	}
	
	public static GameExector getInstance() {
		return instance;
	}

	/**
	 * @param task
	 */
	public void acceptTask(BaseTask task) {
		if (task == null) {
			throw new NullPointerException("task is null");
		}
		int distributeKey = task.dispatchMap % CORE_SIZE;
		workerPool[distributeKey].submit(task::run);
	}

	/**
	 * shut context
	 */
	public void shutDown() {
		run.set(false);
	}

}

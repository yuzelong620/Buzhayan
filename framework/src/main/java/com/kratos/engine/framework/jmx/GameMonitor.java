package com.kratos.engine.framework.jmx;

import com.kratos.engine.framework.net.socket.SessionManager;
import lombok.extern.log4j.Log4j;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.lang.management.*;
import java.util.List;

import static java.lang.management.ManagementFactory.*;

@Log4j
@Component
@ManagedResource(objectName = "GameMXBean:name=gameMonitor")
public class GameMonitor implements GameMonitorMBean {
	
	@Override
	@ManagedOperation
	public int getOnlinePlayerSum() {
		return SessionManager.getInstance().getOnlinePlayerCount();
	}

	@Override
	@ManagedOperation
	public String printServerState() {
		final long ONE_MB = 1024 * 1024;
		String newLine = "\n";
		StringBuilder result = new StringBuilder();

		try {
			// 空闲内存
			long freeMemory = Runtime.getRuntime().freeMemory() / ONE_MB;
			// 当前内存
			long totalMemory = Runtime.getRuntime().totalMemory() / ONE_MB;
			// 最大可使用内存
			long maxMemory = Runtime.getRuntime().maxMemory() / ONE_MB;
			result.append(String.format("freeMemory: %s mb", freeMemory)).append(newLine);
			result.append(String.format(String.format("usedMemory: %s mb", (totalMemory - freeMemory))))
					.append(newLine);
			result.append(String.format("totalMemory: %s mb", totalMemory)).append(newLine);
			result.append(String.format("maxMemory: %s mb", maxMemory)).append(newLine);

			MemoryMXBean memoryMXBean = getMemoryMXBean();
			result.append(
					String.format("heap memory used: %s mb", memoryMXBean.getHeapMemoryUsage().getUsed() / ONE_MB))
					.append(newLine);
			result.append(String.format("heap memory usage: %s", memoryMXBean.getHeapMemoryUsage())).append(newLine);
			result.append(String.format("nonHeap memory usage: %s", memoryMXBean.getNonHeapMemoryUsage()))
					.append(newLine);

			List<BufferPoolMXBean> buffMXBeans = (List<BufferPoolMXBean>) getPlatformMXBeans(BufferPoolMXBean.class);
			for (BufferPoolMXBean buffMXBean : buffMXBeans) {
				result.append(String.format("buffer pool[%s]: used %s mb, total %s mb", buffMXBean.getName(),
						buffMXBean.getMemoryUsed() / ONE_MB, buffMXBean.getTotalCapacity() / ONE_MB)).append(newLine);
			}

			List<GarbageCollectorMXBean> gcMXBeans = getGarbageCollectorMXBeans();
			for (GarbageCollectorMXBean gcBean : gcMXBeans) {
				result.append(String.format("%s 发生 %s 次 gc, gc 总共消耗 %s 毫秒", gcBean.getName(),
						gcBean.getCollectionCount(), gcBean.getCollectionTime())).append(newLine);
			}

			ThreadMXBean threadMXBean = getThreadMXBean();
			int nThreadRun = 0;
			int nThreadBlocked = 0;
			int nThreadWaiting = 0;
			for (long threadId : threadMXBean.getAllThreadIds()) {
				ThreadInfo threadInfo = threadMXBean.getThreadInfo(threadId);
				if (threadInfo.getThreadState() == Thread.State.RUNNABLE) {
					nThreadRun++;
				}
				if (threadInfo.getThreadState() == Thread.State.BLOCKED) {
					nThreadBlocked++;
				}
				if (threadInfo.getThreadState() == Thread.State.WAITING) {
					nThreadWaiting++;
				}
			}
			result.append(String.format("活跃线程数 %s, 阻塞线程数 %s, 等待线程数 %s", nThreadRun, nThreadBlocked, nThreadWaiting))
					.append(newLine);

		} catch (Exception e) {
			log.error("", e);
		}

		return result.toString();
	}

	@Override
	@ManagedOperation(description="执行groovy脚本")
	public String execGroovyScript(String groovyCode) {
		String msg = "执行成功";
		try {
			ScriptEngineManager engineManager = new ScriptEngineManager();
			ScriptEngine scriptEngine = engineManager.getEngineByName("groovy");
			return scriptEngine.eval(groovyCode).toString();
		} catch (Exception e) {
			msg = e.getMessage();
		}
		return msg;
	}

}

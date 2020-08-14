package org.javaknights.crawler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;

@Component
public class ThreadPool {
	
	private ExecutorService pool = Executors.newFixedThreadPool(100);
	
	public void execute(Runnable task) {
		pool.execute(task);
	}
	
	public void shutdown() throws InterruptedException {
		pool.awaitTermination(20, TimeUnit.SECONDS);
		pool.shutdown();
	}
	
}

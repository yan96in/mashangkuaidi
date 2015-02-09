package com.striveen.express.net;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池管理
 * 
 * @author Jason.Xie 
 */
public final class ThreadPoolManager {

	/**
	 * 核心线程数
	 */
	private static final int CORE_SIZE = 5;

	/**
	 * 最大线程数
	 */
	private static final int MAX_SIZE = 20;

	/**
	 * 线程队列的Capacity
	 */
	private static final int CAPACITY = (CORE_SIZE + MAX_SIZE) / 2;

	/**
	 * 保持时间
	 */
	private static final long KEEP_ALIVETIME = 3000;

	/**
	 * 计数单位
	 */
	private static final TimeUnit TIME_UNIT = TimeUnit.SECONDS;

	/**
	 * 初始化
	 */
	private static ThreadPoolManager threadPoolManager = new ThreadPoolManager();

	/**
	 * 线程队列
	 */
	private BlockingQueue<Runnable> queueBlock = new ArrayBlockingQueue<Runnable>(CAPACITY);

	/**
	 * 处理机制
	 */
	private RejectedExecutionHandler rejectedExecutionHandler = new ThreadPoolExecutor.DiscardOldestPolicy();

	/**
	 * ThreadPoolExecutor 执行器
	 */
	private ThreadPoolExecutor execute;

	/**
	 * 获取线程池单例
	 * 
	 * @return
	 */
	public static ThreadPoolManager getInstance() {

		if (null == threadPoolManager.execute || threadPoolManager.execute.isShutdown()) {
			threadPoolManager.execute = new ThreadPoolExecutor(CORE_SIZE,
					MAX_SIZE, KEEP_ALIVETIME, TIME_UNIT,
					threadPoolManager.queueBlock,
					threadPoolManager.rejectedExecutionHandler);
		}

		return threadPoolManager;
	}

	/**
	 * 执行线程
	 * 
	 * @param runnable
	 */
	public void execute(Runnable runnable) {
		execute.execute(runnable);
	}

	/**
	 * 释放线程池
	 */
	public void release() {
		if (null != execute && !execute.isShutdown()) {
			execute.shutdown();
		}
	}

}

package com.simplesalesman.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Asynchronous processing configuration for the SimpleSalesman application.
 * 
 * This configuration class sets up a custom thread pool executor for handling
 * asynchronous operations throughout the application. It enables better control
 * over thread management and resource utilization.
 * 
 * Key features:
 * - Custom thread pool sizing optimized for typical workloads
 * - Named threads for easier debugging and monitoring
 * - Queue management to handle burst loads
 * - Graceful rejection handling for overloaded scenarios
 * 
 * Usage: Methods annotated with @Async will use this executor automatically.
 * 
 * @author SimpleSalesman Team
 * @version 0.0.6
 * @since 0.0.4
 */
@Configuration
@EnableAsync // Enables Spring's asynchronous method execution capability
public class AsyncConfig {

	/**
	 * Logger for configuration-related information and debugging.
	 */
	private static final Logger log = LoggerFactory.getLogger(AsyncConfig.class);

	// Thread pool configuration constants - extracted for easier maintenance
	private static final int CORE_POOL_SIZE = 4;      // Always-active threads
	private static final int MAX_POOL_SIZE = 8;       // Maximum threads under load
	private static final int QUEUE_CAPACITY = 100;    // Tasks queued when all threads busy
	private static final String THREAD_NAME_PREFIX = "SimpleSalesman-Async-";
	private static final int KEEP_ALIVE_SECONDS = 60; // Idle thread timeout

	/**
	 * Creates and configures the primary task executor for asynchronous operations.
	 * 
	 * Thread Pool Sizing Strategy:
	 * - Core Pool Size (4): Based on typical server CPU cores, keeps threads alive
	 * - Max Pool Size (8): Allows scaling during peak load (2x core size)
	 * - Queue Capacity (100): Buffers tasks during temporary spikes
	 * 
	 * This configuration balances:
	 * - Resource efficiency (not too many idle threads)
	 * - Responsiveness (enough threads for concurrent operations)
	 * - System stability (bounded queues prevent memory issues)
	 * 
	 * @return Configured ThreadPoolTaskExecutor for async operations
	 */
	@Bean(name = "taskExecutor")
	public ThreadPoolTaskExecutor taskExecutor() {
		log.info("Configuring async task executor...");
		
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		
		// Core thread pool configuration
		executor.setCorePoolSize(CORE_POOL_SIZE);
		executor.setMaxPoolSize(MAX_POOL_SIZE);
		executor.setQueueCapacity(QUEUE_CAPACITY);
		
		// Thread naming for easier debugging and monitoring
		executor.setThreadNamePrefix(THREAD_NAME_PREFIX);
		
		// Advanced configuration for better resource management
		executor.setKeepAliveSeconds(KEEP_ALIVE_SECONDS);
		executor.setAllowCoreThreadTimeOut(false); // Core threads stay alive
		
		// Rejection policy when queue is full and max threads reached
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		
		// Graceful shutdown configuration
		executor.setWaitForTasksToCompleteOnShutdown(true);
		executor.setAwaitTerminationSeconds(30);
		
		// Initialize the executor
		executor.initialize();
		
		// Log configuration for troubleshooting and monitoring
		logExecutorConfiguration(executor);
		
		return executor;
	}

	/**
	 * Logs the thread pool executor configuration for monitoring and debugging.
	 * This information is crucial for performance tuning and troubleshooting.
	 * 
	 * @param executor The configured ThreadPoolTaskExecutor
	 */
	private static void logExecutorConfiguration(ThreadPoolTaskExecutor executor) {
		log.debug("=== Async Task Executor Configuration ===");
		log.info("Core Pool Size: {}", executor.getCorePoolSize());
		log.info("Max Pool Size: {}", executor.getMaxPoolSize());
		log.info("Queue Capacity: {}", executor.getQueueCapacity());
		log.info("Thread Name Prefix: {}", executor.getThreadNamePrefix());
		log.info("Keep Alive Seconds: {}", executor.getKeepAliveSeconds());
		
		// Log configured values (these don't have getters on ThreadPoolTaskExecutor)
		log.info("Allow Core Thread Timeout: false (configured)");
		log.info("Wait for Tasks on Shutdown: true (configured)");
		log.info("Await Termination Seconds: 30 (configured)");
		log.info("Rejection Policy: CallerRunsPolicy (configured)");
		
		log.info("Async task executor configured successfully");
		log.info("=== End Async Configuration ===");
	}

	/*
	 * PERFORMANCE TUNING NOTES:
	 * 
	 * For high-load scenarios, consider:
	 * - Increasing MAX_POOL_SIZE (monitor CPU usage)
	 * - Adjusting QUEUE_CAPACITY based on memory constraints
	 * - Using different rejection policies (AbortPolicy, DiscardPolicy)
	 * 
	 * For low-resource environments:
	 * - Reduce CORE_POOL_SIZE to 2
	 * - Lower MAX_POOL_SIZE to 4
	 * - Decrease QUEUE_CAPACITY to 50
	 * 
	 * Monitoring recommendations:
	 * - Track active thread count via actuator metrics
	 * - Monitor queue size and rejected task count
	 * - Watch for CallerRunsPolicy executions (indicates overload)
	 */
}
package com.mediaplanet.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
public class SchedulerConfig {

    @Bean
    public ThreadPoolTaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(20); // Adjust based on expected number of concurrent channels
        scheduler.setThreadNamePrefix("ChannelWorker-");
        scheduler.initialize();
        return scheduler;
    }
}

package pl.springboot.projekt09;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;

@SpringBootApplication
@ComponentScan(basePackages = {"pl.springboot.projekt09.Controller"} )
@EnableAsync
public class Projekt09Application {

	public static void main(String[] args) {
		SpringApplication.run(Projekt09Application.class, args);
	}

	@Bean(name ="mainExecutor")
	public Executor asyncMainExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(50);
		executor.setMaxPoolSize(50);
		executor.setQueueCapacity(500);
		executor.setThreadNamePrefix("mainExecutor-");
		executor.initialize();
		return executor;
	}


	@Bean(name = "actorMovieSearch")
	public Executor asyncActorMovieSearch() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(300);
		executor.setMaxPoolSize(300);
		executor.setQueueCapacity(1000);
		executor.setThreadNamePrefix("actorMovie-");
		executor.initialize();
		return executor;
	}

}

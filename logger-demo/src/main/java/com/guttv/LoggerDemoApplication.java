package com.guttv;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LoggerDemoApplication {


	public static void main(String[] args) {
		SpringApplication.run(LoggerDemoApplication.class, args);

		for (int i = 0; i < 10; i++) {
			new Thread(new Head())
			.start();
		}




	}

}

package com.is.lw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication
public class LwApplication {

	public static void main(String[] args) {
		SpringApplication.run(LwApplication.class, args);
	}

}

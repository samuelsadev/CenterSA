package com.saproject.centerSA;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class CenterSaApplication {

	public static void main(String[] args) {
		SpringApplication.run(CenterSaApplication.class, args);
	}

}

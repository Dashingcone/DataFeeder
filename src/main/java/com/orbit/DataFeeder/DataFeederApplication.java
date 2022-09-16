package com.orbit.DataFeeder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class DataFeederApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataFeederApplication.class, args);
	}

}

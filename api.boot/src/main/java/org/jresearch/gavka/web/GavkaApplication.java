package org.jresearch.gavka.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = { "conf" })
public class GavkaApplication {

	public static void main(final String[] args) {
		SpringApplication.run(GavkaApplication.class, args);
	}

}

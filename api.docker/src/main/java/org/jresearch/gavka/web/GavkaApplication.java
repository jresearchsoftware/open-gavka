package org.jresearch.gavka.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jooq.JooqAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableAutoConfiguration(exclude = { JooqAutoConfiguration.class })
@ComponentScan(basePackages = { "conf" })
public class GavkaApplication {

	public static void main(final String[] args) { SpringApplication.run(GavkaApplication.class, args); }

}

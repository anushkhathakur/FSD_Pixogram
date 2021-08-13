package com.pixogram.media;

import javax.annotation.Resource;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

import com.pixogram.media.service.MediaService;

/**
 * @author Anushkha Thakur
 *
 */
@SpringBootApplication(exclude = JmxAutoConfiguration.class)
@EnableEurekaClient
public class MediaApplication implements CommandLineRunner {
	@Resource
	MediaService mediaService;

	public static void main(String[] args) {
		SpringApplication.run(MediaApplication.class, args);
	}

	@Override
	public void run(String... arg) throws Exception {
		mediaService.deleteAll();
		mediaService.init();
	}
}

package com.goldmediatech.vms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.goldmediatech.vms.util.MessageQueue;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class VmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(VmsApplication.class, args);
	}

	@PostConstruct
	public void init() {
		MessageQueue.createTopic(MessageQueue.TOPIC_VIDEO_METADATA);
	}

}

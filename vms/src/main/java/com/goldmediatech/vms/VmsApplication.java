package com.goldmediatech.vms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.goldmediatech.vms.persistence.UserLoader;
import com.goldmediatech.vms.service.dto.UserDto;
import com.goldmediatech.vms.util.MessageQueue;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class VmsApplication {

	private final PasswordEncoder encoder;
	private final UserLoader userLoader;

	public VmsApplication(PasswordEncoder encoder, UserLoader userLoader) {
		this.encoder = encoder;
		this.userLoader = userLoader;
	}

	public static void main(String[] args) {
		SpringApplication.run(VmsApplication.class, args);
	}

	@PostConstruct
	public void init() {
		MessageQueue.createTopic(MessageQueue.TOPIC_VIDEO_METADATA);
		userLoader.registerUser(UserDto.builder().id(null).username("system").password(encoder.encode("SystemPassword1@3")).role("SYSTEM").build());
		userLoader.registerUser(UserDto.builder().id(null).username("odin").password(encoder.encode("OdinPassword1@3")).role("ADMIN").build());
		userLoader.registerUser(UserDto.builder().id(null).username("thor").password(encoder.encode("ThorPassword1@3")).role("USER").build());
	}

}

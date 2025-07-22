package com.goldmediatech.vms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.goldmediatech.vms.persistence.UserRepository;
import com.goldmediatech.vms.persistence.UserRepository.UserEntity;
import com.goldmediatech.vms.util.MessageQueue;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class VmsApplication {

	private final PasswordEncoder encoder;
	private final UserRepository userRepository;

	public VmsApplication(PasswordEncoder encoder, UserRepository userRepository) {
		this.encoder = encoder;
		this.userRepository = userRepository;
	}

	public static void main(String[] args) {
		SpringApplication.run(VmsApplication.class, args);
	}

	@PostConstruct
	public void init() {
		MessageQueue.createTopic(MessageQueue.TOPIC_VIDEO_METADATA);
		userRepository.save(new UserEntity(1, "system", encoder.encode("roboto"), "SYSTEM"));
		userRepository.save(new UserEntity(2, "odin", encoder.encode("OdinPassword1@3"), "ADMIN"));
		userRepository.save(new UserEntity(3, "thor", encoder.encode("ThorPassword1@3"), "USER"));
	}

}

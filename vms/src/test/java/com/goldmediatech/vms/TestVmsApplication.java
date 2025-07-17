package com.goldmediatech.vms;

import org.springframework.boot.SpringApplication;

public class TestVmsApplication {

	public static void main(String[] args) {
		SpringApplication.from(VmsApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}

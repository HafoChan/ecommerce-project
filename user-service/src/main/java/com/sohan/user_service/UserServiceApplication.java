package com.sohan.user_service;

import com.sohan.user_service.config.VaultConfig;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients
@EnableJpaAuditing
@AllArgsConstructor
@EnableConfigurationProperties(VaultConfig.class)
public class UserServiceApplication implements CommandLineRunner {

	private VaultConfig vaultConfig;

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
	    System.out.println("Vault Config: " + vaultConfig.getUsername());
		System.out.println("Vault Config: " + vaultConfig.getPassword());
	}
}

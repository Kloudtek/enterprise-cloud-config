package com.kloudtek.enterprisecloudconfig.server.enterprisecloudconfigserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@EnableConfigServer
@SpringBootApplication
public class EnterpriseCloudConfigServerApplication {
	public static void main(String[] args) {
		SpringApplication.run(EnterpriseCloudConfigServerApplication.class, args);
	}
}

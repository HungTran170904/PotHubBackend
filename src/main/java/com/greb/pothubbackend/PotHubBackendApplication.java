package com.greb.pothubbackend;

import com.greb.pothubbackend.configs.JwtConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(JwtConfig.class)
public class PotHubBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(PotHubBackendApplication.class, args);
    }

}

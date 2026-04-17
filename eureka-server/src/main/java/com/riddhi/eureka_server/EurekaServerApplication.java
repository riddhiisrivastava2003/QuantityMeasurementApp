package com.riddhi.eureka_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer //ye annotation eureka server banata h
@SpringBootApplication  // yha  se start ho rha ha
public class EurekaServerApplication {
    //run start karega app ko aur tomcat server ko
    public static void main(String[] args) {
        SpringApplication.run(EurekaServerApplication.class, args);
    }
}

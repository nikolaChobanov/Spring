package com.example.demo;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
class LoadDatabase {

    @Bean
    CommandLineRunner initDatabase(RobotRepository repository) {
        return args -> {
            log.info("Preloading " + repository.save(new Robot(7,3,Directions.NORTH)));
            log.info("Preloading " + repository.save(new Robot(1,1,Directions.EAST)));
        };
    }
}
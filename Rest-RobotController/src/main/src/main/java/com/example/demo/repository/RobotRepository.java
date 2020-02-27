package com.example.demo.repository;


import com.example.demo.model.Robot;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RobotRepository extends MongoRepository<Robot, String> {

    /*public Robot findByFirstName(String firstName);
    public List<Robot> findByLastName(String lastName);*/
    Optional<Robot> findRobotById(@Param("id") String name);
  //  List<Robot> findAllRobots();

}
package com.example.demo.repository;

import com.example.demo.model.Robot;

import java.util.List;
import java.util.Optional;

public interface RobotService {

    Robot insert(Robot robot);
    Optional<Robot> getRobot(String id);
    void deleteRobot(String id);
    List<Robot> findAll();
}

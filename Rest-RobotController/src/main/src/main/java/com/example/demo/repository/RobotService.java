package com.example.demo.repository;

import com.example.demo.model.Robot;

import java.util.List;
import java.util.Optional;

public interface RobotService {

    Robot insert(Robot robot);
    Optional<Robot> getRobot(String id);
    void updateDirection(Robot robot);
    void updateX(Robot robot);
    void updateY(Robot robot);
    void deleteRobot(String id);

    List<Robot> findAll();
}

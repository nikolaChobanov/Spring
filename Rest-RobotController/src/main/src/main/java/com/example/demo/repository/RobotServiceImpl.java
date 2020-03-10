package com.example.demo.repository;

import com.example.demo.model.Robot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RobotServiceImpl implements RobotService {

    @Autowired
    private RobotRepository robotRepository;

    @Autowired
    public RobotServiceImpl(RobotRepository robotRepository) {
        this.robotRepository = robotRepository;
    }

    @Override
    public Robot insert(Robot robot) {
        return robotRepository.save(robot);
    }

    @Override
    public Optional<Robot> getRobot(String id) {
        return robotRepository.findRobotById(id);
    }

    @Override
    public void deleteRobot(String id) {
        robotRepository.deleteById(id);
    }

    @Override
    public List<Robot> findAll() {
        return robotRepository.findAll();
    }
}

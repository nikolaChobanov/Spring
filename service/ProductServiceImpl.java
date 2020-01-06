package com.tutorialspoint.demo.service;

import com.tutorialspoint.demo.model.Robot;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class ProductServiceImpl implements ProductService {
    private static Map<String, Robot> productRepo = new HashMap<>();
    Robot currentRobot=new Robot();
    {
       constructRobot();

    }
    @Override
    public void createProduct(Robot robot) {
        constructRobot();
        productRepo.put(robot.getId(), robot);

    }
    @Override
    public void updateProduct(String id, String command) {

        String[] split=command.split("");

        for(String ch:split){
            String pos=currentRobot.getDirection();



            if(ch.contains("R")) {
                if (pos.contains("North")) {
                    currentRobot.setDirection("East");
                    continue;

                }else {
                    currentRobot.setDirection("North");
                    continue;

                }
            }

            if(ch.contains("L")) {
                if (pos.contains("East")) {
                    currentRobot.setDirection("North");
                    continue;

                }else {
                    currentRobot.setDirection("East");
                    continue;

                }

            }
            if(ch.contains("A")) {

                if (pos.contains("North")) {
                    currentRobot.setY(currentRobot.getY()+1);
                    continue;

                }
                if (pos.contains("East")) {
                    currentRobot.setX(currentRobot.getX() +1);
                    continue;

                }
            }

        }



    }


    @Override
    public void deleteProduct(String id) {
        productRepo.remove(id);
        constructRobot();

    }
    @Override
    public Collection<Robot> getProducts() {
        return productRepo.values();
    }


    public void constructRobot(){

        currentRobot.setId("1");
        currentRobot.setDirection("North");
        Random rand = new Random();
        int min=0;
        int max =10;
        currentRobot.setX(rand.nextInt((max - min) + 1) + min);
        currentRobot.setY(rand.nextInt((max - min) + 1) + min);
        productRepo.put(currentRobot.getId(), currentRobot);
    }
}
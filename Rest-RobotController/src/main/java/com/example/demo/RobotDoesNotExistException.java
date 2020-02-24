package com.example.demo;

public class RobotDoesNotExistException extends RuntimeException {

    RobotDoesNotExistException(int id) {
        super("Could not find robot " + id);
    }
}
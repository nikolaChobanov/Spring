package com.example.demo.service;

public class RobotDoesNotExistException extends RuntimeException {

    public RobotDoesNotExistException(String id) {
        super("Could not find robot " + id);
    }
}
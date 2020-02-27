package com.example.demo.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
class RobotDoesNotExistAdvice {

    @ResponseBody
    @ExceptionHandler(RobotDoesNotExistException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String employeeNotFoundHandler(RobotDoesNotExistException ex) {
        return ex.getMessage();
    }
}
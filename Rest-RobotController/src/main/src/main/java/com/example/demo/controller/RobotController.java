package com.example.demo.controller;

import com.example.demo.model.Directions;
import com.example.demo.model.Robot;
import com.example.demo.repository.RobotService;
import com.example.demo.service.RobotDoesNotExistException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.mediatype.vnderrors.VndErrors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

//Controller class consisting of all the RESTful services
@RestController
@RequestMapping("/robots")
public class RobotController {

    private static final Logger LOGGER = LogManager.getLogger(RobotController.class.getName());
    private final RobotModelAssembler assembler;

    @Autowired
    private RobotService robotService;

    public RobotController(RobotModelAssembler assembler) {
        this.assembler = assembler;
    }


    @PostMapping(value = "/")
    ResponseEntity<EntityModel<Robot>> newRobot(@RequestBody Robot robot) {

        robotService.insert(robot);

        robotService.getRobot(robot.getId()).orElseThrow(() -> {
            LOGGER.error("Robot with id: " + robot.getId() + " was not added successfully");
            return new RobotDoesNotExistException(robot.getId());
        });
        LOGGER.info("New robot entity added to database");
        return ResponseEntity
                .created(linkTo(methodOn(RobotController.class).one(robot.getId())).toUri())
                .body(assembler.toModel(robot));
    }

    //return data for a single robot
    @GetMapping(value = "/{id}")
    EntityModel<Robot> one(@PathVariable String id) {

        Robot robot = robotService.getRobot(id).orElseThrow(() -> {
            LOGGER.error("Robot with id: " + id + " does not exist");
            return new RobotDoesNotExistException(id);
        });

        LOGGER.info("Robot with id: " + id + "looked up from database");
        return assembler.toModel(robot);
    }

    //return data for all the robots
    @GetMapping(value = "/all")
    CollectionModel<EntityModel<Robot>> all() {

        List<EntityModel<Robot>> robots = robotService.findAll().stream()
                .map(assembler::toModel).collect(Collectors.toList());

        if (robots.isEmpty()) {
            LOGGER.error("Robot database is empty");
            throw new NullPointerException();
        }

        LOGGER.info("Lookup on data for all robots on database");
        return new CollectionModel<>(robots,
                linkTo(methodOn(RobotController.class).all()).withSelfRel());
    }

    //Executes movement command
    @PutMapping(value = "/{id}")
    EntityModel<Robot> replaceRobot(@RequestBody String movement, @PathVariable String id) {

        String[] split = movement.split("");
        Robot robot = robotService.getRobot(id).orElseThrow(() -> {
            LOGGER.error("Robot with id: " + id + " does not exist");
            return new RobotDoesNotExistException(id);
        });

        for (String ch : split) {

            if (ch.contains("R")) {
                robot.turnRight();
            }
            if (ch.contains("L")) {
                robot.turnLeft();
            }
            if (ch.contains("A")) {
                robot.advance();
            }
        }

        robotService.insert(robot);
        LOGGER.info("Robot with id: " + id + "received new movement instruction");
        return assembler.toModel(robot);
    }

    //Remove a robot from the repository
    @DeleteMapping(value = "/{id}")
    ResponseEntity<VndErrors.VndError> deleteRobot(@PathVariable String id) {

        robotService.deleteRobot(id);
        LOGGER.info("Robot with id: " + id + "was deleted successfully");
        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(new VndErrors.VndError("Robot deletion", " robot was deleted successfully"));
    }

    //Remove robot pointing to a wrong direction
    @DeleteMapping(value = "/{id}/del")
    ResponseEntity<RepresentationModel> delete(@PathVariable String id) {

        Robot robot = robotService.getRobot(id).orElseThrow(() -> {
            LOGGER.error("Robot with id: " + id + " does not exist");
            return new RobotDoesNotExistException(id);
        });
        if (robot.getDirection() == Directions.SOUTH) {
            robotService.deleteRobot(id);
            deleteRobot(id);
        }
        if (robot.getDirection() == Directions.WEST) {
            robotService.deleteRobot(id);
            deleteRobot(id);
        }
        LOGGER.info("Robot with id: " + id + "was deleted successfully");
        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(new VndErrors.VndError("Robot was deleted", "Due to backwards movement in " + robot.getDirection().name() + " direction the robot was deleted"));
    }

    //Fix robot pointing direction
    @PutMapping(value = "/{id}/complete")
    ResponseEntity<RepresentationModel> complete(@PathVariable String id) {

        Robot robot = robotService.getRobot(id).orElseThrow(() -> {
            LOGGER.error("Robot with id: " + id + " does not exist");
            return new RobotDoesNotExistException(id);
        });
        if (robot.getDirection() == Directions.SOUTH) {
            robot.setDirection(Directions.NORTH);
            LOGGER.info("Robot with id: " + id + "changed its direction to NORTH");
            return ResponseEntity.ok(assembler.toModel(robotService.insert(robot)));
        } else if (robot.getDirection() == Directions.WEST) {
            robot.setDirection(Directions.EAST);
            LOGGER.info("Robot with id: " + id + "changed its direction to EAST");
            return ResponseEntity.ok(assembler.toModel(robotService.insert(robot)));
        }
        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(new VndErrors.VndError("Movement not allowed", "robot cannot move backwards currently in:" + robot.getDirection().name() + " position."));
    }
}

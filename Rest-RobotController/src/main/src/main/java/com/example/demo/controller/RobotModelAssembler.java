package com.example.demo.controller;

import com.example.demo.model.Directions;
import com.example.demo.model.Robot;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class RobotModelAssembler implements RepresentationModelAssembler<Robot, EntityModel<Robot>> {

    @Override
    public EntityModel<Robot> toModel(Robot robot) {
        EntityModel<Robot> robotModel = new EntityModel<>(robot,
                linkTo(methodOn(RobotController.class).one(robot.getId())).withSelfRel(),
                linkTo(methodOn(RobotController.class).all()).withRel("allRobots"));


        if (robot.getDirection() == Directions.SOUTH) {
            robotModel.add(
                    linkTo(methodOn(RobotController.class)
                            .delete(robot.getId())).withRel("Delete robot due to backwards movement"));
            robotModel.add(
                    linkTo(methodOn(RobotController.class)
                            .complete(robot.getId())).withRel("Fix direction to north and continue"));
        }


        if (robot.getDirection() == Directions.WEST) {
            robotModel.add(
                    linkTo(methodOn(RobotController.class)
                            .delete(robot.getId())).withRel("Delete robot due to backwards movement"));
            robotModel.add(
                    linkTo(methodOn(RobotController.class)
                            .complete(robot.getId())).withRel("Fix direction to east and continue"));
        }

        return robotModel;
    }
}



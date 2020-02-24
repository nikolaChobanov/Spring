package com.example.demo;

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

@RestController
public class RobotController {

    private final RobotRepository robotRepository;
    private final RobotModelAssembler assembler;

    public RobotController(RobotRepository repository, RobotModelAssembler assembler) {
        this.robotRepository = repository;
        this.assembler = assembler;
    }


    @PostMapping("/robot")
    ResponseEntity<EntityModel<Robot>> newRobot(@RequestBody Robot robot) {


        Robot newRobot = robotRepository.save(robot);
        return ResponseEntity
                .created(linkTo(methodOn(RobotController.class).one(newRobot.getId())).toUri())
                .body(assembler.toModel(newRobot));
    }

    @GetMapping("/robot/{id}")
    EntityModel<Robot> one(@PathVariable Integer id) {
        Robot robot = robotRepository.findById(id).orElseThrow(() -> new RobotDoesNotExistException(id));

      /*  return new EntityModel<>(robot,
                linkTo(methodOn(RobotController.class).one(id)).withSelfRel(),
                linkTo(methodOn(RobotController.class).all()).withRel("robot"));
*/
        return assembler.toModel(robot);
    }

    @GetMapping("/robot")
    CollectionModel<EntityModel<Robot>> all() {

      /*  List<EntityModel<Robot>> robots = robotRepository.findAll().stream()
                .map(robot -> new EntityModel<>(robot,
                        linkTo(methodOn(RobotController.class).one(robot.getId())).withSelfRel(),
                        linkTo(methodOn(RobotController.class).all()).withRel("robots")))
                .collect(Collectors.toList());

*/
        List<EntityModel<Robot>> robots = robotRepository.findAll().stream().map(assembler::toModel).collect(Collectors.toList());
        return new CollectionModel<>(robots,
                linkTo(methodOn(RobotController.class).all()).withSelfRel());
    }


    @PutMapping("/robot/{id}")
    EntityModel<Robot> replaceRobot(@RequestBody String movement, @PathVariable Integer id) {

        String[] split = movement.split("");

        Robot robot = robotRepository.findById(id).orElseThrow(() -> new RobotDoesNotExistException(id));
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

        robotRepository.save(robot);
        return assembler.toModel(robot);
    }

    @DeleteMapping("/robot/{id}")
    void deleteRobot(@PathVariable Integer id) {
        robotRepository.deleteById(id);
    }


    @DeleteMapping("/robot/{id}/delete")
    ResponseEntity<RepresentationModel> delete(@PathVariable Integer id) {
        Robot robot = robotRepository.findById(id).orElseThrow(() -> new RobotDoesNotExistException(id));

        if (robot.getDirection() == Directions.SOUTH) {
            robotRepository.deleteById(id);
        }
        if (robot.getDirection() == Directions.WEST) {
            robotRepository.deleteById(id);
        }
        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(new VndErrors.VndError("Robot was deleted", "Due to backwards movement in " + robot.getDirection().name() + " direction the robot was deleted"));
    }


    @PutMapping("/robot/{id}/complete")
    ResponseEntity<RepresentationModel> complete(@PathVariable Integer id) {
        Robot robot = robotRepository.findById(id).orElseThrow(() -> new RobotDoesNotExistException(id));

        if (robot.getDirection() == Directions.SOUTH) {
            robot.setDirection(Directions.NORTH);
            return ResponseEntity.ok(assembler.toModel(robotRepository.save(robot)));
        } else if (robot.getDirection() == Directions.WEST) {
            robot.setDirection(Directions.EAST);
            return ResponseEntity.ok(assembler.toModel(robotRepository.save(robot)));

        }

        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(new VndErrors.VndError("Movement not allowed", "robot cannot move backwards currently in:" + robot.getDirection().name() + " position."));
    }


}
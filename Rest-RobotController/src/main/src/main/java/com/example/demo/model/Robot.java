package com.example.demo.model;


import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

@Data
//@Entity
@Document(collection = "robots")
public class Robot {

    @Id
    private String id;
    private int x;
    private int y;
    private Directions direction;


    Robot() {
    }

    public Robot(int x, int y, Directions direction) {

        this.x = x;
        this.y = y;
        this.direction = direction;
    }

    public Directions turnLeft() {

        if (direction.equals(Directions.NORTH)) {
            return direction = Directions.WEST;
        }

        return direction = direction.getPrevious();
    }

    public Directions turnRight() {

        return direction = direction.getNext();
    }

    public void advance() {
        if (direction.equals(Directions.NORTH)) {
            setY(getY() + 1);
        }
        if (direction.equals(Directions.EAST)) {
            setX(getX() + 1);
        }
    }
}

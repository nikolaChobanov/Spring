package com.example.demo;


import com.example.demo.model.Directions;
import com.example.demo.model.Robot;
import com.example.demo.repository.RobotService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.UnsupportedEncodingException;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.springframework.test.util.AssertionErrors.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc

public class RobotControllerTest {


    @Autowired
    private MockMvc mvc;

    @Autowired
    private RobotService service;


    @Test
    public void testIfRobotIsAddedSuccessfully() throws Exception {

        Robot robot = new Robot(6, 3, Directions.SOUTH);
        service.insert(robot);
        MvcResult result = mvc.perform(get("/robots/all").contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":\"xxx\",\"x\":\"xxx\",\"y\":\"xxx\",\"direction\":\"xxx\"}"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn();

        try {
            String message = "Robot has not been added successfully to the database";
            String content = result.getResponse().getContentAsString();
            assertTrue(message, content.contains(robot.getId()));

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testIfGetRobotByIDReturnsCorrect() throws Exception {

        Robot robot = new Robot(1, 2, Directions.EAST);
        service.insert(robot);
        MvcResult result = mvc.perform(get("/robots/{id}", robot.getId()).contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":\"xxx\",\"x\":\"xxx\",\"y\":\"xxx\",\"direction\":\"xxx\"}"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn();

        try {
            String message = "Robot has not been added successfully to the database";
            String content = result.getResponse().getContentAsString();
            assertTrue(message, content.contains(robot.getId()));

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testIfMovementIsCorrect() throws Exception {
        Robot robot = new Robot(1, 2, Directions.NORTH);
        service.insert(robot);

        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.put("/robots/{id}", robot.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content("A");
        this.mvc.perform(builder)
                .andExpect(MockMvcResultMatchers.status()
                        .isOk())
                .andDo(MockMvcResultHandlers.print());


        String message = "Robot has not moved correctly";
        Robot rob = service.getRobot(robot.getId()).get();
        assertEquals(message, robot.getY() + 1, rob.getY());
    }

    @Test
    public void testIfDeletingRobotClearsDB() throws Exception {
        Robot robot = new Robot(7, 3, Directions.NORTH);
        service.insert(robot);

        this.mvc.perform(delete("/robots/{id}", robot.getId()));

        String message = "Robot has not been deleted from the database";
        Optional<Robot> rob = service.getRobot(robot.getId());
        assertFalse(message, rob.isPresent());

    }


}

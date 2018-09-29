package com.example.demo.controller;

import com.example.demo.model.Appointment;
import com.example.demo.model.MessageError;
import com.example.demo.services.AppointmentService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController()
@RequestMapping(value = "/appointment")
public class AppointmentController {


    private static final Logger LOGGER = LoggerFactory.getLogger(AppointmentController.class);
    AppointmentService appointmentService;

    @Autowired
    public AppointmentController(AppointmentService appointmentService){
        this.appointmentService = appointmentService;
    }


    @HystrixCommand(fallbackMethod = "fallbackError", commandKey = "test", groupKey = "test")
    @GetMapping(value = "/{personName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Appointment> getAppointment(@PathVariable String personName){
        if("error".equalsIgnoreCase(personName)){
            throw new IllegalArgumentException();
        }
        LOGGER.info("try to find appointments of {}",personName);
        return appointmentService.findByName(personName);
    }

    public List<Appointment> fallbackError(String personName) {
        LOGGER.info("{} cannot be used as argument", personName);
        return new ArrayList<>();
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Appointment> getAppointments(){
        LOGGER.info("retrieve all appointments");
        return appointmentService.findAll();
    }

    @PutMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createAppointment(@RequestBody Appointment appointment){
        LOGGER.info("Create appointment : {}", appointment);
        Appointment a = appointmentService.addAppointment(appointment);
        if(a==null){
            return new ResponseEntity<>(new MessageError("Appointment exist during this interval"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(a, HttpStatus.OK);
    }

    @PostMapping(value = "/collision", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public List<Appointment> getCollision(@RequestBody Appointment appointment){
        LOGGER.info("Create appointment : {}", appointment);
        return appointmentService.getCollision(appointment);
    }

}

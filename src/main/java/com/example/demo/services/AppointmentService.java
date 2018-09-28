package com.example.demo.services;

import com.example.demo.model.Appointment;
import com.example.demo.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentService {

    private AppointmentRepository appointmentRepository;

    @Autowired
    public AppointmentService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public Appointment addAppointment(Appointment appointment) {
        if(this.getCollision(appointment).isEmpty()){
            return appointmentRepository.save(appointment);
        }
        return null;
    }

    public List<Appointment> getCollision(Appointment appointment) {
        return appointmentRepository.findByQueryWithExpression(appointment.getDoctor(), appointment.getBegin(),
                appointment.getEnd());
    }

    public List<Appointment> findAll() {
        return appointmentRepository.findAll();
    }

    public List<Appointment> findByName(String doctor) {
        return appointmentRepository.findByDoctor(doctor);
    }

    public List<Appointment> appointmentsAt(LocalDateTime localDateTime) {
        throw new NotImplementedException();
        // return null;
    }

    public List<String> getPersonAvailable(LocalDateTime begin, LocalDateTime end) {
        return null;
    }
}

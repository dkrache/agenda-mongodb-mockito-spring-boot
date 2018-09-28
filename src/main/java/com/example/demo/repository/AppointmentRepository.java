package com.example.demo.repository;

import com.example.demo.model.Appointment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends MongoRepository<Appointment, String> {

    List<Appointment> findByDoctor(String doctor);

    @Query(value = "{'doctor':?0, $or :[ " +
            "{'begin' : { $lt : ?1 }, 'end' : { $gt:?1} }, " +
            "{'begin' : { $lt : ?2 }, 'end' : { $gt:?2} }, " +
            "{'begin' : { $gte : ?1 }, 'end' : { $lte:?2} } ]}")
    List<Appointment> findByQueryWithExpression(String doctor, LocalDateTime begin, LocalDateTime end);
}
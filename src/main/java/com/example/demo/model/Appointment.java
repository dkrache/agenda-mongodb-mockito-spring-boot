package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
@ToString
@Document
public class Appointment {
    @Id
    private String id;

    private String doctor;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime begin;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime end;

    private String title;

    private String description;
}

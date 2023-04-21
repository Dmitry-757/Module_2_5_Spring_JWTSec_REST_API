package com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.DTO;

import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.model.File;
import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.model.User;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public class EventDTO {
    private int id;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime eventDateTime;
    private User user;
    private File file;

    public EventDTO(int id, LocalDateTime eventDateTime, User user, File file) {
        this.id = id;
        this.eventDateTime = eventDateTime;
        this.user = user;
        this.file = file;
    }
}

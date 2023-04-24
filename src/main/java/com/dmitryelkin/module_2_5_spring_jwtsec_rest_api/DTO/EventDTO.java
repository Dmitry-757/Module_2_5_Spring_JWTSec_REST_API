package com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.DTO;

import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.model.Event;
import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.model.File;
import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.model.User;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public class EventDTO {
    private final int id;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private final LocalDateTime eventDateTime;
    private final User user;
    private final File file;

//    public EventDTO() {
//        this.id = id;
//        this.eventDateTime = eventDateTime;
//        this.user = user;
//        this.file = file;
//    }


    public EventDTO(int id, LocalDateTime eventDateTime, User user, File file) {
        this.id = id;
        this.eventDateTime = eventDateTime;
        this.user = user;
        this.file = file;
    }

    public Event toEvent(){
        return new Event(id, eventDateTime, user, file);
    }
}

package com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.DTO;

import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.model.Event;
import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.model.File;
import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.model.User;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;


public class EventDTO {
    private final long id;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private final LocalDateTime eventDateTime;
    private final User user;
    private final File file;


    public EventDTO(Event item) {
        this.id = item.getId();
        this.eventDateTime = item.getEventDateTime();
        this.user = item.getUser();
        this.file = item.getFile();
    }

//    public Event getEvent(){
//        return new Event(id, eventDateTime, user, file);
//    }
}

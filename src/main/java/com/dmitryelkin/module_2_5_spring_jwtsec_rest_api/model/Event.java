package com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Objects;


@Data
@Builder
@AllArgsConstructor
//@RequiredArgsConstructor
@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private final LocalDateTime eventDateTime;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "file_id", unique = true, nullable = true)
    private File file;

    @Enumerated(EnumType.STRING)
    @Value("Status.ACTIVE")
    private Status status;

    @Enumerated(EnumType.STRING)
    private TypeOfEvent typeOfEvent;

    public Event() {
        this.eventDateTime = LocalDateTime.now();
    }
    public Event(User user, File file, TypeOfEvent typeOfEvent) {
        this.eventDateTime = LocalDateTime.now();
        this.user = user;
        this.file = file;
        this.typeOfEvent = typeOfEvent;
        this.status = Status.ACTIVE;
    }

    public Event(LocalDateTime eventDateTime, User user, File file, Status status, TypeOfEvent typeOfEvent) {
        this.eventDateTime = eventDateTime;
        this.user = user;
        this.file = file;
        this.typeOfEvent = typeOfEvent;
        this.status = status;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return id == event.id && user.equals(event.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user);
    }
}

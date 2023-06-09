package com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
//@RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String password;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Event> events;

    @Enumerated(EnumType.STRING)
    @Value("Status.ACTIVE")
    private Status status;

    @Enumerated(EnumType.STRING)
    @Value("Role.USER")
    private Role role;


    public User(String name) {
        this.name = name;
        this.events = new ArrayList<>();
    }
    public User(String name, String pass) {
        this.name = name;
        this.password = pass;
        this.events = new ArrayList<>();
    }

    public User(String name, List<Event> events) {
        this.name = name;
        this.events = events;
    }

    public User(long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && name.equals(user.name) && Objects.equals(events, user.events);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, events);
    }

    public void addEvent(Event event){
        events.add(event);
    }
    public void removeEvent(Event event){
        events.remove(event);
    }

}

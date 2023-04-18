package com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Event> events;

    public User() {
    }

    public User(String name) {
        this.name = name;
        this.events = new ArrayList<>();
    }

    public User(String name, List<Event> events) {
        this.name = name;
        this.events = events;
    }


    public int getId() {
        return id;
    }

//    public void setId(int id) {
//        this.id = id;
//    }

    public String getName() {
        return name;
    }

//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public List<Event> getEvents() {
//        return events;
//    }
//
//    public void setEvents(List<Event> events) {
//        this.events = events;
//    }

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

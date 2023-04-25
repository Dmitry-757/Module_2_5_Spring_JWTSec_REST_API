package com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.model;

import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Value;

import java.util.Objects;

@Entity
@Table(name = "files")
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;

    private String location;

    @Enumerated(EnumType.STRING)
    @Value("Status.ACTIVE")
    private Status status;

    public File() {
    }

    public File(String name, String location) {
        this.name = name;
        this.location = location;
    }

    public File(long id, String name, String location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        File file = (File) o;
        return id == file.id && name.equals(file.name) && location.equals(file.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, location);
    }
}

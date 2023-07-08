package com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import java.util.Objects;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

    public File(String name) {
        this.name = name;
    }
    public File(String name, String location) {
        this.name = name;
        this.location = location;
        this.status = Status.ACTIVE;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof File file)) return false;
        return id == file.id && name.equals(file.name) && location.equals(file.location) && status == file.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, location, status);
    }
}

package com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.DTO;

public class FileDTO {
    private int id;
    private String name;
    private String location;

    public FileDTO(int id, String name, String location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }
}

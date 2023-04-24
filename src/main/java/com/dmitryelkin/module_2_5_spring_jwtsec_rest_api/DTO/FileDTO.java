package com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.DTO;

import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.model.File;

public class FileDTO {
    private final int id;
    private final String name;
    private final String location;

    public FileDTO(int id, String name, String location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }


    public File toFile(){
        return new File( id, name, location);
    }
}

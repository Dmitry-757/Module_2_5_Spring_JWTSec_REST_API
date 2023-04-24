package com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.DTO;

import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.model.File;

public class FileDTO {
    private final int id;
    private final String name;
    private final String location;

    public FileDTO(File item) {
        this.id = item.getId();
        this.name = item.getName();
        this.location = item.getLocation();
    }


    public File getFile(){
        return new File( id, name, location);
    }
}

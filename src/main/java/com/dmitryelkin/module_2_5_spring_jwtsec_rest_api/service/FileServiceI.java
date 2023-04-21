package com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.service;



import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.model.File;

import java.util.List;

public interface FileServiceI {
    File create(File item);
    File update(File item);
    File delete(long id);

    List<File> getAll();
    File getById(long id);
    File getByName(String name);
}

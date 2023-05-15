package com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.service;



import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.model.File;

import java.io.InputStream;
import java.util.List;

public interface FileServiceI {
    void upload(File item);

    List<String> getAll();
    InputStream download(String name);

    void delete(String name);

}

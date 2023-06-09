package com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.service;


import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.model.File;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface FileServiceI {
    File upload(MultipartFile item) throws IOException;

    List<String> getAll();
    InputStream download(String name);

    void delete(String name);

}

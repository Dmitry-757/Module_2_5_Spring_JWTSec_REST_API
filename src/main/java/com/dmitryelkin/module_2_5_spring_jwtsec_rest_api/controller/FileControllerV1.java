package com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.controller;

import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.service.FileServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;

@RestController
@RequestMapping(value = "/api/v1/files/")
public class FileControllerV1 {
    private final FileServiceImpl service;

    @Autowired
    public FileControllerV1(FileServiceImpl service) {
        this.service = service;
    }

    @GetMapping("/{fileName}")
    public ResponseEntity<?> getFilesByName(@PathVariable String fileName) {
        InputStream inputStream = service.download(fileName);
        if (inputStream == null) {
//            throw new FileNotFoundException("Can't get file");
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("File not found");
        }
        InputStreamResource resource = new InputStreamResource(inputStream);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"");

        return ResponseEntity.ok()
                    .headers(headers)
//                    .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}

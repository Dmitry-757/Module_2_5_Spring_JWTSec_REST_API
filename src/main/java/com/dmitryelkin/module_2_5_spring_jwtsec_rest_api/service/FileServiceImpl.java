package com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.model.File;
import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.model.TypeOfEvent;
import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.repository.FileRepositoryI;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class FileServiceImpl implements FileServiceI {
    private final FileRepositoryI repository;
    private final AmazonS3 s3client;

    private final EventServiceI eventService;

//    @Value("${aws.bucketName}")
    private String bucketName = "myBucketName";

//    @Value("${fileService.tmpFilePath}")
    private String tmpFilePath = "d:\\uploadingFile.tmp";


    @Autowired
    public FileServiceImpl(FileRepositoryI repository, AmazonS3 s3client, EventServiceI eventService) {
        this.repository = repository;
        this.s3client = s3client;
        this.eventService = eventService;
    }

//    @Value("${fileService.tmpFilePath}")
//    private void setPropertyBucketName(String value){
//        this.tmpFilePath = value;
//    }


    @Override
    public File upload(MultipartFile multipartFile) {
//        java.io.File uploadingFile = file.getResource().getFile();
        java.io.File targetFile = new java.io.File( tmpFilePath);

        try(OutputStream outputStream = new FileOutputStream(targetFile)){
            IOUtils.copy(multipartFile.getInputStream(), outputStream);
        } catch (FileNotFoundException e) {
            // handle exception here
            log.info("During uploading File : {} FileNotFoundException was occurred {}", multipartFile.getName(), e.getMessage());

        } catch (IOException e) {
            // handle exception here
            log.info("During uploading File : {} IOException was occurred {}", multipartFile.getName(), e.getMessage());
        }
        s3client.putObject(
                bucketName,
                multipartFile.getName(),
                targetFile);
        File modelFile = new File(multipartFile.getName(), bucketName+".s3.amazonaws.com/"+multipartFile.getName());
        File savedModelFile = repository.saveAndFlush(modelFile);
        eventService.setNewEvent(savedModelFile, TypeOfEvent.UPLOAD);

        return savedModelFile;
    }

    @Override
    public List<String> getAll() {

        ObjectListing objects = s3client.listObjects(bucketName);
        List<String> result = new ArrayList<>();
        for (S3ObjectSummary objectSummary : objects.getObjectSummaries()) {
            result.add(objectSummary.getKey());
//            log.info("File name: {}", objectSummary.getKey());
        }

        return result;
    }

    @Override
    public InputStream download(String name) {
        S3ObjectInputStream inputStream = null;
        if (s3client.doesObjectExist(bucketName, name)) {
            S3Object s3object = s3client.getObject(bucketName, name);
            inputStream = s3object.getObjectContent();
            log.info("File {} downloaded", name);

            File modelFile = new File(name, bucketName+".s3.amazonaws.com/"+name);
            eventService.setNewEvent(modelFile, TypeOfEvent.DOWNLOAD);

        }else
            log.info("File {} was not found", name);

        return inputStream;
    }

    @Override
    public void delete(String name) {
        if (s3client.doesObjectExist(bucketName, name)) {
            s3client.deleteObject(bucketName, name);
            log.info("Deleting a File {}", name);
            File modelFile = new File(name, bucketName+".s3.amazonaws.com/"+name);
            eventService.setNewEvent(modelFile, TypeOfEvent.DELETE);
        } else {
            log.info("File {} not found", name);
        }
    }

}

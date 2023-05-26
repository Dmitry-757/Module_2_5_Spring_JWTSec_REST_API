package com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class FileServiceImpl implements FileServiceI {
    private final AmazonS3 s3client;
    @Value("${aws.bucketName}")
    private String bucketName;

    @Autowired
    public FileServiceImpl(AmazonS3 s3client) {
        this.s3client = s3client;
    }


//    public void createBucket() {
//
//        if (s3client.doesBucketExistV2(bucketName)) {
//            log.info("Bucket {} already exists, use a different name ", bucketName);
//            return;
//        }
//
//        s3client.createBucket(bucketName);
//    }
//
//    public void listBuckets() {
//        List<Bucket> buckets = s3client.listBuckets();
//    }

    @Override
    public void upload(MultipartFile file) throws IOException {
        java.io.File uploadingFile = file.getResource().getFile();
        s3client.putObject(
                bucketName,
                file.getName(),
                uploadingFile);
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
        }
        return inputStream;
    }

    @Override
    public void delete(String name) {
        if (s3client.doesObjectExist(bucketName, name)) {
            s3client.deleteObject(bucketName, name);
            log.info("Deleting a File {}", name);
        } else {
            log.info("File {} not found", name);
        }
    }

}

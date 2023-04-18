package com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.service.AWSS3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

//import java.util.logging.Logger;

@Service
public class S3Service {


    @Autowired
    private AmazonS3 s3client;
    private String bucketName = "myBucket";

    org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(S3Service.class);
    //Logger log = Logger.getLogger(S3Service.class.getName());

//    @Autowired
//    public S3Service(AmazonS3 s3client) {
//        this.s3client = s3client;
//    }

    public void createBucket() {

        if (s3client.doesBucketExistV2(bucketName)) {
            log.info("Bucket {} already exists, use a different name", bucketName);
            return;
        }

        s3client.createBucket(bucketName);
    }

    public void listBuckets(){
        List<Bucket> buckets = s3client.listBuckets();
        log.info("buckets: {}", buckets);
    }

    public void uploadFile(java.io.File file) {
        s3client.putObject(
                bucketName,
                file.getName(),
                file);
    }

    public void listFiles() {

        ObjectListing objects = s3client.listObjects(bucketName);
        for(S3ObjectSummary objectSummary : objects.getObjectSummaries()) {
            log.info("File name: {}", objectSummary.getKey());
        }
    }

    public void downloadFile(File file) {

        if (s3client.doesObjectExist(bucketName, file.getName())) {
            S3Object s3Object = s3client.getObject(bucketName, file.getName());
            log.info("File download {}", file.getName());

        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Requested file does not exist on bucket");
        }
    }

    public void deleteFile(String fileName) {
        s3client.deleteObject(bucketName, fileName);
    }
}

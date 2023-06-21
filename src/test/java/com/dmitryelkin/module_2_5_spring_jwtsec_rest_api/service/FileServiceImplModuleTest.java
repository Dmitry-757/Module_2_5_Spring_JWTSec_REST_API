package com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectListing;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class FileServiceImplModuleTest {

    @Value("${aws.bucketName}")
    private String bucketName;

    private AmazonS3 s3client = Mockito.mock(AmazonS3.class);

    @InjectMocks
    private FileServiceImpl service;
//    @Test
//    void upload() {
//    }

    @Test
    void getAll() {
        // given
        List<String> expectedResult = List.of("file1", "file2", "file3");
        Mockito.doReturn(expectedResult).when(s3client).listObjects(bucketName);

        // when
        var response = service.getAll();

        // then
        assertEquals(expectedResult, response);

    }

    @Test
    void download() {
    }

    @Test
    void delete() {
    }
}
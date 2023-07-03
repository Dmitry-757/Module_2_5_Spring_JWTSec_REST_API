package com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.service.FileServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static com.atlassian.oai.validator.mockmvc.OpenApiValidationMatchers.openApi;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
class FileControllerV1IntegrationTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${aws.bucketName}")
    private String bucketName;


    @MockBean
    AmazonS3 mockS3client;


    @InjectMocks
    private FileServiceImpl service;


    @Test
    @WithMockUser()
    void getAll() throws Exception {
        // given
        var requestBuilder = MockMvcRequestBuilders.get("/api/v1/files/");
        var items = List.of("fileName1", "fileName2", "fileName3");
        List<S3ObjectSummary> mockResult = new ArrayList<>();
        S3ObjectSummary item = new S3ObjectSummary();
        item.setKey("fileName1");
        mockResult.add(item);
        item = new S3ObjectSummary();
        item.setKey("fileName2");
        mockResult.add(item);
        item = new S3ObjectSummary();
        item.setKey("fileName3");
        mockResult.add(item);

        ObjectListing listOfObjects = new ObjectListing();
        try {
            Field field = listOfObjects.getClass().getDeclaredField("objectSummaries");
            field.setAccessible(true);
            field.set(listOfObjects, mockResult);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        Mockito.doReturn(listOfObjects).when(mockS3client).listObjects(bucketName);



        // when
        this.mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(items)))
                .andExpect(openApi().isValid("static/openapi.json"));

    }

    @Test
    void getFilesByName() {
    }

    @Test
    void uploadFile() {
    }

    @Test
    void delete() {
    }
}
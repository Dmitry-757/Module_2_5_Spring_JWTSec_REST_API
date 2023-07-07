package com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.repository.EventRepositoryI;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.apache.http.client.methods.HttpRequestBase;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static com.atlassian.oai.validator.mockmvc.OpenApiValidationMatchers.openApi;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    @MockBean
    EventRepositoryI mockRepository;


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

                // then
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(items)))
                .andExpect(openApi().isValid("static/openapi.json"));

    }

    @Test
    @WithMockUser()
    void getFilesByName() throws Exception {
        // given
        var requestBuilder = MockMvcRequestBuilders.get("/api/v1/files/findByName/fileName1");
        S3Object s3object = new S3Object();

        String name = "fileName1";
        String fileBody = "some char file";
        byte[] initialArray = fileBody.getBytes(StandardCharsets.UTF_8);
        InputStream inputStream = new ByteArrayInputStream(initialArray);
        S3ObjectInputStream targetStream = new S3ObjectInputStream(inputStream, new HttpRequestBase() {
            @Override
            public String getMethod() {
                return null;
            }
        });

        s3object.setObjectContent(targetStream);

        Mockito.doReturn(true).when(mockS3client).doesObjectExist(bucketName, name);
        Mockito.doReturn(s3object).when(mockS3client).getObject(bucketName, name);


        // when
        this.mockMvc.perform(requestBuilder)

                // then
                .andExpect(status().isOk())
                .andExpect(openApi().isValid("static/openapi.json"));

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void uploadFile() throws Exception {
        // given
        MockMultipartFile myMockFile
                = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );
        // when
        mockMvc.perform(
                MockMvcRequestBuilders
                        .multipart("/api/v1/files/")
                        .file(myMockFile)
                        .accept(MediaType.TEXT_PLAIN_VALUE)
                        )
                // then
                .andExpect(status().is(200));
    }

    @Test
    public void uploadWithRestAssured(){
        /*
         * Set a {org.springframework.test.web.servlet.MockMvc} instance that REST Assured will use when making requests unless overwritten
         * by a { MockMvcRequestSpecification}.
         *
         * @param mockMvc The MockMvc instance to use.
         */
        RestAssuredMockMvc.mockMvc(mockMvc);

        File testFile = new File("D:\\proselyte.txt");
        io.restassured.module.mockmvc.response.MockMvcResponse response = RestAssuredMockMvc
                .given()
                .auth().with(SecurityMockMvcRequestPostProcessors.user("user").password("pass345").roles("ADMIN"))
                .multiPart("file", testFile)
                .contentType(MediaType.TEXT_PLAIN_VALUE)
                .accept(MediaType.TEXT_PLAIN_VALUE)
                .when()
                .request("PUT", "/api/v1/events/")
                .then()
                .extract()
                .response();

        assertEquals(200, response.statusCode());
    }

    @Test
    void delete() {
    }
}
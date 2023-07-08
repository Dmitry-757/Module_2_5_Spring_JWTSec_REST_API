package com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.model.File;
import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.model.Status;
import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.repository.FileRepositoryI;
import org.apache.http.client.methods.HttpRequestBase;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


//@ExtendWith(MockitoExtension.class)
@SpringBootTest
class FileServiceImplModuleTest {

    @Value("${aws.bucketName}")
    private String bucketName;

    private final AmazonS3 s3client = Mockito.mock(AmazonS3.class);


    @Mock
    private EventServiceI eventService = Mockito.mock(EventServiceI.class);

    @Mock
    private FileRepositoryI repository = Mockito.mock(FileRepositoryI.class);

    @InjectMocks
    private FileServiceImpl service;


    @Test
    void getAll() {
        // given
        List<String> expectedResult = List.of("file1", "file2", "file3");

        List<S3ObjectSummary> mockResult = new ArrayList<>();
        S3ObjectSummary item = new S3ObjectSummary();
        item.setKey("file1");
        mockResult.add(item);
        item = new S3ObjectSummary();
        item.setKey("file2");
        mockResult.add(item);
        item = new S3ObjectSummary();
        item.setKey("file3");
        mockResult.add(item);

        ObjectListing listOfObjects = new ObjectListing();
        try {
            Field field = listOfObjects.getClass().getDeclaredField("objectSummaries");
            field.setAccessible(true);
            field.set(listOfObjects, mockResult);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        Mockito.doReturn(listOfObjects).when(s3client).listObjects(bucketName);

        // when
        var response = service.getAll();

        // then
        assertEquals(expectedResult, response);

    }

    @Test
    void download() {
        // given
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

        // when
        Mockito.doReturn(true).when(s3client).doesObjectExist(bucketName, name);
        Mockito.doReturn(s3object).when(s3client).getObject(bucketName, name);

        // then
        var response = service.download(name);
        assertEquals(response, targetStream);
    }

    @Test
    void upload() throws IOException {

        // given
        MultipartFile file = new MockMultipartFile(
                "file",
                "fileName.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );
        File modelFile = new File(file.getName(), bucketName+".s3.amazonaws.com/"+file.getName());
        File expected = new File(123L, file.getName(), bucketName+".s3.amazonaws.com/"+file.getName(), Status.ACTIVE);
        Mockito.doReturn(expected).when(repository).saveAndFlush(modelFile);

        // when
        var actual = service.upload(file);

        // then
        assertEquals(expected, actual);

    }
    @Test
    void delete() {
    }
}
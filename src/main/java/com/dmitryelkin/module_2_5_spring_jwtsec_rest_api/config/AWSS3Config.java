package com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AWSS3Config {

    @Value("${aws.accessKey}")
    private String awsSecretKey;
    @Value("${aws.secretKey}")
    private String awsAccessKey;

        @Bean
        public AmazonS3 s3client() {
            //AWS_ACCESS_KEY и AWS_SCRECT_KEY – это данные, которые были сгенерированы в AWS IAM
            AWSCredentials credentials = new BasicAWSCredentials(
                    awsSecretKey,
                    awsAccessKey
            );

            return AmazonS3ClientBuilder
                    .standard()
                    .withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .withRegion(Regions.US_EAST_1)
                    .build();
        }
}

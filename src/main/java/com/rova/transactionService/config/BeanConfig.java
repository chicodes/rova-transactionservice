package com.rova.transactionService.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    // SQS Keys
    @Value("${aws.accessId}")
    private String awsAccessId;

    @Value("${aws.accessKey}")
    private String awsSecret;

    @Bean
    public AmazonSQS sqs() {
        AWSCredentials credentials = new BasicAWSCredentials(awsAccessId, awsSecret);
        return AmazonSQSClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.EU_WEST_1)
                .build();
    }

    @Bean
    public Gson gson() {
        return new Gson();
    }
}

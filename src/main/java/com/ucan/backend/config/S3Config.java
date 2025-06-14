package com.ucan.backend.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Config {

  @Value("${AWS_ENDPOINT_URL}")
  private String endpointUrl;

  @Value("${aws.access.key.id}")
  private String accessKeyId;

  @Value("${aws.secret.access.key}")
  private String secretAccessKey;

  @Value("${aws.s3.region}")
  private String region;

  @Bean
  public AmazonS3 s3Client() {
    BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKeyId, secretAccessKey);
    return AmazonS3ClientBuilder.standard()
        .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpointUrl, region))
        .withPathStyleAccessEnabled(true)
        .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
        .build();
  }
}

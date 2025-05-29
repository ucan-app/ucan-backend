package com.ucan.backend.post.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.IOException;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PostImageService {

  private final AmazonS3 s3Client;

  @Value("${aws.s3.bucket}")
  private String bucketName;

  public PostImageService(AmazonS3 s3Client) {
    this.s3Client = s3Client;
  }

  public String uploadPostImage(MultipartFile file, Long postId) throws IOException {
    String fileName = generateUniqueFileName(file.getOriginalFilename());
    String key = "post-images/" + postId + "/" + fileName;

    ObjectMetadata metadata = new ObjectMetadata();
    metadata.setContentType(file.getContentType());
    metadata.setContentLength(file.getSize());

    PutObjectRequest putObjectRequest =
        new PutObjectRequest(bucketName, key, file.getInputStream(), metadata);

    s3Client.putObject(putObjectRequest);
    return s3Client.getUrl(bucketName, key).toString();
  }

  private String generateUniqueFileName(String originalFileName) {
    String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
    return UUID.randomUUID().toString() + extension;
  }
}

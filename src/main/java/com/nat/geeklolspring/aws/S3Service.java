package com.nat.geeklolspring.aws;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class S3Service {
    //private S3Client s3;
    //
    //@Value("${aws.credential.accessKey}")
    //private String accessKey;
    //
    //@Value("${aws.credentials.secretKey}")
    //private String secretKey;
    //
    //@Value("${aws.region}")
    //private String region;
    //
    //@Value("${aws.bucketName}")
    //private String bucketName;
    //
    //@PostConstruct
    //private void initAmanonS3() {
    //    AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);
    //
    //    this.s3 = S3Client.builder()
    //            .region(Region.of(region))
    //            .credentialsProvider(StaticCredentialsProvider.create(credentials))
    //            .build();
    //}
    //
    //public String uploadUoS3Bucket(byte[] uploadFile, String fileName) {
    //    PutObjectRequest request = PutObjectRequest.builder()
    //            .bucket(bucketName)
    //            .key(fileName)
    //            .build();
    //
    //    s3.putObject(request, RequestBody.fromBytes(uploadFile));
    //
    //    return s3.utilities()
    //            .getUrl(b -> b.bucket(bucketName).key(fileName))
    //            .toString();
    //}
}

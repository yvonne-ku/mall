package com.noinch.mall.springboot.starter.minio;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;

@Slf4j
@SpringBootApplication
class MinioTests {

    private MinioTemplate minioTemplate;

    @BeforeEach
    public void before() {
        ConfigurableApplicationContext context = SpringApplication.run(MinioTests.class);
        minioTemplate = context.getBean(MinioTemplate.class);
    }

    @SneakyThrows
    @Test
    public void assertUpload() {
        File file = new File("src/test/resources/example-img.png");
        MultipartFile multipartFile = new MockMultipartFile(file.getName(), file.getName(), "image/png", new FileInputStream(file));
        String fileName = minioTemplate.upload(multipartFile);
        Assertions.assertFalse(StringUtils.isEmpty(fileName));
    }

}

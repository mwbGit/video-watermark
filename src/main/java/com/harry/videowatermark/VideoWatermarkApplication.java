package com.harry.videowatermark;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class VideoWatermarkApplication {

    public static void main(String[] args) {
        SpringApplication.run(VideoWatermarkApplication.class, args);
    }

}

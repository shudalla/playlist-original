package br.com.shu.playlist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class MsPlaylistApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsPlaylistApplication.class, args);
    }
}
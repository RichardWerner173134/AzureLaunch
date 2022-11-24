package com.werner;

import com.werner.bl.processor.UserInputProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

@SpringBootApplication
@EnableAutoConfiguration
public class Main implements CommandLineRunner{

    private static Logger LOGGER = LoggerFactory
            .getLogger(Main.class);

    public static void main(String[] args) throws FileNotFoundException {
        SpringApplication application = new SpringApplication(Main.class);
        application.setWebApplicationType(WebApplicationType.NONE);
        application.run(args);
    }

    @Override
    public void run(String... args) {
        String filePath = args.length > 0
                ? args[0]
                : "D:\\Masterprojekt\\GenerationProject\\specification\\stage1.json";

        UserInputProcessor processor = new UserInputProcessor(filePath);
        try {
            processor.process();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.exit(0);
    }
}

package com.werner;

import com.werner.bl.processor.UserInputProcessor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class Main implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        String filePath = args.length > 0
                ? args[0]
                : "D:\\Masterprojekt\\GenerationProject\\specification\\stage1.json";

        UserInputProcessor processor = new UserInputProcessor(filePath);
        processor.process();
    }
}

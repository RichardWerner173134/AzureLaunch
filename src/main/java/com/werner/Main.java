package com.werner;

import com.werner.bl.processor.UserInputProcessor;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@EnableAutoConfiguration
@AllArgsConstructor
public class Main implements CommandLineRunner{
    private final UserInputProcessor userInputProcessor;

    private static Logger LOGGER = LoggerFactory
            .getLogger(Main.class);

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(Main.class);
        application.setWebApplicationType(WebApplicationType.NONE);
        ConfigurableApplicationContext run = application.run(args);
    }

    @Override
    public void run(String... args) {
        String filePath = args.length > 0
                ? args[0]
                : "C:\\1_Richard\\htwk\\Vorlesung\\V\\Projekt\\GenerationProject\\specification\\stage1.json";

        try {
            userInputProcessor.process(filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

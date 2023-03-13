package com.werner;

import com.werner.bl.processor.UserInputProcessor;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.List;

@SpringBootApplication
@EnableAutoConfiguration
@AllArgsConstructor
public class Main implements ApplicationRunner {
    private final UserInputProcessor userInputProcessor;

    private final ApplicationArguments applicationArguments;

    private static Logger LOGGER = LoggerFactory
            .getLogger(Main.class);

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(Main.class);
        application.setWebApplicationType(WebApplicationType.NONE);
        ConfigurableApplicationContext run = application.run(args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {


        //"C:\\1_Richard\\htwk\\Vorlesung\\V\\Projekt\\GenerationProject\\specification\\inputFile.json";

        if(args.containsOption("dest") == false) {
            throw new Exception("Please provide the argument --dest");
        }

        List<String> dest = args.getOptionValues("dest");
        String filePath = dest.get(0);

        userInputProcessor.process(filePath);
    }
}

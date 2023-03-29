package com.werner;

import com.werner.bl.exception.InvalidArgumentsException;
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

    private static Logger LOGGER = LoggerFactory
            .getLogger(Main.class);

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(Main.class);
        application.setWebApplicationType(WebApplicationType.NONE);
        try {
            application.run(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run(ApplicationArguments args) {

        String filePath;
        try {
            filePath = extractDestinationArgument(args);
        } catch (InvalidArgumentsException e) {
            LOGGER.error(e.getMessage());
            return;
        }

        userInputProcessor.process(filePath);
    }

    private String extractDestinationArgument(ApplicationArguments args) throws InvalidArgumentsException {
        if (args.containsOption("dest") == false) {
            throw new InvalidArgumentsException("Please provide the argument --dest");
        }
        List<String> dest = args.getOptionValues("dest");
        return dest.get(0);
    }
}

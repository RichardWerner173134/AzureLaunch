package com.werner.bl.codegeneration.helper;

import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@Component
public class TemplateResolver {

    public String resolveTemplate(TemplateName templateName) {
        String filepath = templateName.getFilepath();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filepath);
        String template = readSampleFile(inputStream);
        return template;
    }

    private String readSampleFile(InputStream inputStream) {
        try (InputStreamReader streamReader =
                new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                BufferedReader reader = new BufferedReader(streamReader)){
            String content = "";
            String line = "";
            while ((line = reader.readLine()) != null) {
                content += line + "\n";
            }
            return content;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

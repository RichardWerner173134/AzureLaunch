package com.werner.helper;

import com.werner.bl.exception.InvalidInputFileException;
import org.springframework.stereotype.Component;

import java.io.*;

@Component
public class FileUtil {

    public String readFileContent(String filePath) throws IOException, InvalidInputFileException {
        String content = "";
        BufferedReader bufferedReader;
        try {
            bufferedReader = new BufferedReader(new FileReader(filePath));
        } catch (FileNotFoundException e) {
            throw new InvalidInputFileException("File does not exist: " + filePath);
        }

        String line = "";
        while ((line = bufferedReader.readLine()) != null) {
            content += line;
        }

        return content;
    }

    public void writeContentToFile(String filePath, String content) throws IOException {
        File f = new File(filePath);
        if(!f.exists()) {
            f.createNewFile();
        } else {
            f.delete();
        }

        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(f));
        bufferedWriter.write(content);
        bufferedWriter.close();

    }
}

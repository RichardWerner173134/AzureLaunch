package com.werner.helper;

import org.springframework.stereotype.Component;

import java.io.*;

@Component
public class FileUtil {
    public String readFileContent(String filePath) {
        try {
            String content = "";
            BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));

            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                content += line + "\n";
            }

            bufferedReader.close();
            return content;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeContentToFile(String filePath, String content) {
        try {
            File f = new File(filePath);
            if(!f.exists()) {
                f.createNewFile();
            } else {
                f.delete();
            }

            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(f));
            bufferedWriter.write(content);
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteFile(String filePath) {
        File f = new File(filePath);
        if(f.exists()) {
            f.delete();
        }
    }
}

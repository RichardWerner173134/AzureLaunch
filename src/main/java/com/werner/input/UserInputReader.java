package com.werner.input;

import com.google.gson.Gson;
import com.werner.helper.FileUtil;
import generated.internal.v1_0_0.model.AzCodegenRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserInputReader {

    private FileUtil fileUtil;

    public AzCodegenRequest readUserInput(String filePath) {
        String jsonContent = fileUtil.readFileContent(filePath);
        return new Gson().fromJson(jsonContent, AzCodegenRequest.class);
    }
}

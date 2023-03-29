package com.werner.input;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.werner.bl.exception.InvalidInputFileContentException;
import com.werner.bl.exception.InvalidInputFileException;
import com.werner.helper.FileUtil;
import generated.internal.v1_0_0.model.AzCodegenRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@AllArgsConstructor
public class UserInputReader {

    private FileUtil fileUtil;

    public AzCodegenRequest readUserInput(String filePath)
            throws IOException, InvalidInputFileContentException, InvalidInputFileException {
        String jsonContent = fileUtil.readFileContent(filePath);
        try {
            AzCodegenRequest azCodegenRequest = new Gson().fromJson(jsonContent, AzCodegenRequest.class);
            return azCodegenRequest;
        } catch (JsonParseException e) {
            throw new InvalidInputFileContentException("Invalid Inputfile. Cannot parse as Json ");
        }
    }
}

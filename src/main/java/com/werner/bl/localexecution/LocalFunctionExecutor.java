package com.werner.bl.localexecution;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@NoArgsConstructor
public class LocalFunctionExecutor {

    public void startFunction(String targetFolderPath, int port) {
        String command = "cd " + targetFolderPath + "; func start --port " + port;
        try {
            Runtime.getRuntime().exec("cmd /k start powershell.exe -Command " + command);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

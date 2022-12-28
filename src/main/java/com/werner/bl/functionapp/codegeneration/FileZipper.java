package com.werner.bl.functionapp.codegeneration;

import com.profesorfalken.jpowershell.PowerShellResponse;
import com.werner.powershell.AbstractPowershellCaller;
import org.springframework.stereotype.Component;

@Component
public class FileZipper extends AbstractPowershellCaller {

    private final static String SCRIPT = "Compress-Archive -Path %s\\* -DestinationPath %s; ";

    public void zipFiles(String folderToZip, String zipFile) throws Exception {
        config.put("maxWait", "5000");
        String cmd = String.format(SCRIPT, folderToZip, zipFile);
        executePowershellCommand(cmd);
    }

    @Override
    public void handleResponse(PowerShellResponse powerShellResponse) {
        // exit code of Compress-Archive is somehow always 1 / error
        // TODO only check if File is created at the filesystem
    }
}

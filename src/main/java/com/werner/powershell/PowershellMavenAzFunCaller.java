package com.werner.powershell;

import com.profesorfalken.jpowershell.PowerShellResponse;
import org.springframework.stereotype.Component;

@Component
public class PowershellMavenAzFunCaller extends AbstractPowershellCaller {

    private final String TEMP_DIR = "$home\\temp";

    private final String SCRIPT_GENERATE_PROJECT = "if(-Not (Test-Path -Path %s)) {mkdir %s;} cd %s; " +
            "mvn archetype:generate -B -DarchetypeGroupId=\"com.microsoft.azure\" -DarchetypeArtifactId=\"azure-functions-archetype\" -DgroupId=\"com.werner\" -DartifactId=\"%s\" -Dversion=\"1.0.0\" -Dpackage=\"com.werner.%s\"; " +
            "Remove-Item %s\\%s\\src\\test -Recurse;";

    private final String SCRIPT_BUILD_PROJECT = "cd %s\\%s; mvn clean package;";

    @Override
    public void handleResponse(PowerShellResponse powerShellResponse) {
        // maven response
    }

    public void generateProject(String mavenProjectName) throws Exception {
        executePowershellCommand(String.format(SCRIPT_GENERATE_PROJECT, TEMP_DIR, TEMP_DIR, TEMP_DIR, mavenProjectName, mavenProjectName, TEMP_DIR, mavenProjectName));
    }

    public String getTempDir() {
        return executePowershellWithResponse(String.format("echo %s", TEMP_DIR)).getCommandOutput();
    }

    public void buildProject(String mavenProjectName) throws Exception {
        executePowershellCommand(String.format(SCRIPT_BUILD_PROJECT, TEMP_DIR, mavenProjectName));
    }
}

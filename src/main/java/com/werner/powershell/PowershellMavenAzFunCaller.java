package com.werner.powershell;

import com.profesorfalken.jpowershell.PowerShellResponse;
import com.werner.bl.codegeneration.generators.projectlevel.Project;
import org.springframework.stereotype.Component;

@Component
public class PowershellMavenAzFunCaller extends AbstractPowershellCaller {

    private final String TEMP_DIR = "$home\\temp";

    @Override
    public void handleResponse(PowerShellResponse powerShellResponse) {
        // maven response
    }

    public void generateProject(Project project) throws Exception {
        StringBuilder sb = new StringBuilder();

        sb.append(String.format("if(-Not (Test-Path -Path %s)) {mkdir %s;} cd %s; ", TEMP_DIR, TEMP_DIR, TEMP_DIR));
        sb.append("mvn archetype:generate ");
        sb.append("-B -DarchetypeGroupId=\"com.microsoft.azure\" -DarchetypeArtifactId=\"azure-functions-archetype\" ");
        sb.append(String.format("-DgroupId=\"%s\" -DartifactId=\"%s\" -Dversion=\"1.0.0\" ", project.getGroupId(), project.getArtifactId()));
        sb.append(String.format("-Dpackage=\"%s.%s\"; ", project.getGroupId(), project.getArtifactId()));
        sb.append(String.format("Remove-Item %s\\src\\test -Recurse; ", project.getProjectRoot()));


        executePowershellCommand(sb.toString());
    }

    public String getTempDir() {
        return executePowershellWithResponse(String.format("echo %s", TEMP_DIR)).getCommandOutput();
    }

    public void buildProject(Project project) throws Exception {
        StringBuilder sb = new StringBuilder();

        sb.append(String.format("cd %s; ", project.getProjectRoot()));
        sb.append("mvn clean package; ");;

        executePowershellCommand(sb.toString());
    }

    public void deployProject(Project project) throws Exception {
        StringBuilder sb = new StringBuilder();

        sb.append(String.format("cd %s; ", project.getProjectRoot()));
        sb.append("mvn azure-functions:deploy -f pom.xml;");

        executePowershellCommand(sb.toString());
    }
}

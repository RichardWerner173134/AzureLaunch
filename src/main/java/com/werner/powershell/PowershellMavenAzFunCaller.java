package com.werner.powershell;

import com.werner.bl.codegeneration.generators.projectlevel.Project;
import com.werner.log.PowershellResponse;
import com.werner.log.PowershellTaskLogger;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class PowershellMavenAzFunCaller extends AbstractPowershellCaller {

    public PowershellMavenAzFunCaller(PowershellTaskLogger logger) {
        super(logger);
    }

    public void generateProject(Project project, String resolvedTempDir) {
        Map<String, String> commands = new LinkedHashMap<>();

        // generate new mavenProject
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("cd %s; ", resolvedTempDir));
        sb.append("mvn archetype:generate ");
        sb.append("-B -DarchetypeGroupId='com.microsoft.azure' -DarchetypeArtifactId='azure-functions-archetype' ");
        sb.append(String.format("-DgroupId='%s' -DartifactId='%s' -Dversion='1.0.0' ", project.getGroupId(), project.getArtifactId()));
        sb.append(String.format("-Dpackage='%s.%s' ", project.getGroupId(), project.getArtifactId()));
        sb.append(String.format("-DoutputDirectory='%s'", resolvedTempDir));
        commands.put("Generate New Project from Archetype - " + project.getArtifactId(), sb.toString());

        // delete classes
        String unnessecaryClassFilePath = project.getProjectRoot() + "\\src\\main\\java\\com\\werner\\" + project.getArtifactId() + "\\Function.java";
        commands.put("Removing Archetype Generated Class - " + project.getArtifactId(), String.format("Remove-Item %s", unnessecaryClassFilePath));

        // delete tests
        commands.put("Removing Archetype Generated Tests - " + project.getArtifactId(), String.format("Remove-Item %s\\src\\test -Recurse", project.getProjectRoot()));

        // execute all commands in order
        for (Map.Entry<String, String> task : commands.entrySet()) {
            String taskName = task.getKey();
            String command = task.getValue();
            PowershellResponse powershellResponse = executeSingleCommand(command);
            logger.addLogItem(powershellResponse, taskName);
        }
    }

    public void buildProject(Project project) {
        String command = String.format("mvn clean package -f %s", project.getProjectRoot());
        PowershellResponse powershellResponse = executeSingleCommand(command);
        logger.addLogItem(powershellResponse, "Building Maven Project - " + project.getArtifactId());
    }

    public void deployProject(Project project) {
        String command = String.format("mvn azure-functions:deploy -f %s\\pom.xml", project.getProjectRoot());
        PowershellResponse powershellResponse = executeSingleCommand(command);
        logger.addLogItem(powershellResponse, "Deploying Maven Project - " + project.getArtifactId());
    }
}

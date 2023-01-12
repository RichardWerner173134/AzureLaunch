package com.werner.powershell;

import com.werner.bl.codegeneration.generators.projectlevel.Project;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PowershellMavenAzFunCaller extends AbstractPowershellCaller {

    public void generateProject(Project project, String resolvedTempDir) {
        List<String> commands = new ArrayList<>();

        // generate new mavenProject
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("cd %s; ", resolvedTempDir));
        sb.append("mvn archetype:generate ");
        sb.append("-B -DarchetypeGroupId='com.microsoft.azure' -DarchetypeArtifactId='azure-functions-archetype' ");
        sb.append(String.format("-DgroupId='%s' -DartifactId='%s' -Dversion='1.0.0' ", project.getGroupId(), project.getArtifactId()));
        sb.append(String.format("-Dpackage='%s.%s' ", project.getGroupId(), project.getArtifactId()));
        sb.append(String.format("-DoutputDirectory='%s'", resolvedTempDir));
        commands.add(sb.toString());

        // delete classes
        String unnessecaryClassFilePath = project.getProjectRoot() + "\\src\\main\\java\\com\\werner\\" + project.getArtifactId() + "\\Function.java";
        commands.add(String.format("Remove-Item %s", unnessecaryClassFilePath));

        // delete tests
        commands.add(String.format("Remove-Item %s\\src\\test -Recurse", project.getProjectRoot()));

        executeCommandChain(commands);
    }

    public void buildProject(Project project) {
        String command = String.format("mvn clean package -f %s", project.getProjectRoot());
        executeSingleCommand(command);
    }

    public void deployProject(Project project) {
        String command = String.format("mvn azure-functions:deploy -f %s\\pom.xml", project.getProjectRoot());
        executeSingleCommand(command);
    }
}

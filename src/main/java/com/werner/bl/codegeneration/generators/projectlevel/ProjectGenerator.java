package com.werner.bl.codegeneration.generators.projectlevel;

import com.werner.bl.codegeneration.generators.classlevel.ClassGenerator;
import com.werner.bl.codegeneration.model.FunctionApp;
import com.werner.bl.codegeneration.model.FunctionAppClient;
import com.werner.bl.codegeneration.model.FunctionAppTrigger;
import com.werner.helper.FileUtil;
import com.werner.powershell.PowershellMavenAzFunCaller;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class ProjectGenerator {

    private final FileUtil fileUtil;

    private final ClassGenerator classGenerator;

    private final PowershellMavenAzFunCaller powershellMavenAzFunCaller;

    private final PomGenerator pomGenerator;

    public void generateProject(FunctionApp functionApp) throws Exception {
        String resolvedTempDir = powershellMavenAzFunCaller.getTempDir();

        Project project = initProject(functionApp, resolvedTempDir);

        powershellMavenAzFunCaller.generateProject(project, resolvedTempDir);

        writeClassFile(project, functionApp.getTriggerList(), functionApp.getClientList());
        writePomFile(project, functionApp.getTriggerList(), functionApp.getClientList());

        powershellMavenAzFunCaller.buildProject(project);
        powershellMavenAzFunCaller.deployProject(project);
    }

    private Project initProject(FunctionApp functionApp, String resolvedTempDir) {
        Project result = new Project();

        String mavenArtifactId = functionApp.getFunctionAppName();
        result.setArtifactId(mavenArtifactId);

        String mavenGroupId = "com.werner";
        result.setGroupId(mavenGroupId);

        result.setProjectRoot(resolvedTempDir + "\\" + mavenArtifactId);

        return result;
    }

    private void writeClassFile(Project project, List<FunctionAppTrigger> triggers, List<FunctionAppClient> clients) {
        String classCode = classGenerator.generateClassCode(project, triggers, clients);

        String classFilePath = project.getProjectRoot() + "\\src\\main\\java\\com\\werner\\" + project.getArtifactId() + "\\GeneratedClass.java";
        fileUtil.writeContentToFile(classFilePath, classCode);
    }

    private void writePomFile(Project project, List<FunctionAppTrigger> triggers, List<FunctionAppClient> clients) {
        String pomCode = pomGenerator.generateCode(project, triggers, clients);
        String pomPath = project.getProjectRoot() + "\\pom.xml";

        fileUtil.writeContentToFile(pomPath, pomCode);
    }
}

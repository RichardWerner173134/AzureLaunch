package com.werner.bl.codegeneration.generators.projectlevel;

import com.werner.bl.codegeneration.generators.classlevel.ClassGenerator;
import com.werner.bl.codegeneration.model.FunctionApp;
import com.werner.bl.codegeneration.model.FunctionAppClient;
import com.werner.bl.codegeneration.model.FunctionAppTrigger;
import com.werner.helper.FileUtil;
import com.werner.powershell.PowershellMavenAzFunCaller;
import generated.internal.v1_0_0.model.AppConfig;
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

    private final LocalSettingsGenerator localSettingsGenerator;

    public void generateProject(FunctionApp functionApp, AppConfig appConfig) throws Exception {
        String resolvedTempDir = powershellMavenAzFunCaller.getTempDir();

        Project project = initProject(functionApp, resolvedTempDir);

        powershellMavenAzFunCaller.generateProject(project, resolvedTempDir);

        writeClassFile(project, functionApp.getTriggerList(), functionApp.getClientList(), appConfig);
        writePomFile(project, functionApp.getTriggerList(), functionApp.getClientList());
        writeLocalSettingsFile(project, functionApp.getTriggerList(), functionApp.getClientList());

        powershellMavenAzFunCaller.buildProject(project);

        if(appConfig.isLocalDeploymentOnly() == false) {
            powershellMavenAzFunCaller.deployProject(project);
        }
    }

    private void writeLocalSettingsFile(Project project, List<FunctionAppTrigger> triggerList,
            List<FunctionAppClient> clientList) {
        String localSettingsCode = localSettingsGenerator.generateCode(triggerList, clientList);

        String localSettingsPath = project.getProjectRoot() + "\\local.settings.json";
        fileUtil.writeContentToFile(localSettingsPath, localSettingsCode);
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

    private void writeClassFile(Project project, List<FunctionAppTrigger> triggers, List<FunctionAppClient> clients, AppConfig appConfig) {
        String classCode = classGenerator.generateClassCode(project, triggers, clients, appConfig);

        String classFilePath = project.getProjectRoot() + "\\src\\main\\java\\com\\werner\\" + project.getArtifactId() + "\\GeneratedClass.java";
        fileUtil.writeContentToFile(classFilePath, classCode);
    }

    private void writePomFile(Project project, List<FunctionAppTrigger> triggers, List<FunctionAppClient> clients) {
        String pomCode = pomGenerator.generateCode(project, triggers, clients);
        String pomPath = project.getProjectRoot() + "\\pom.xml";

        fileUtil.writeContentToFile(pomPath, pomCode);
    }
}

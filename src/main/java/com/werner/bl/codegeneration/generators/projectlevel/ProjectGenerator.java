package com.werner.bl.codegeneration.generators.projectlevel;

import com.werner.bl.codegeneration.generators.classlevel.ClassGenerator;
import com.werner.bl.codegeneration.model.FunctionApp;
import com.werner.bl.codegeneration.model.FunctionAppClient;
import com.werner.bl.codegeneration.model.FunctionAppTrigger;
import com.werner.bl.resourcecreation.model.graph.node.ServicePrincipal;
import com.werner.helper.FileUtil;
import com.werner.log.JavaTask;
import com.werner.log.TaskLogger;
import com.werner.powershell.PowershellMavenAzFunCaller;
import generated.internal.v1_0_0.model.AppConfig;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@AllArgsConstructor
public class ProjectGenerator {

    private final static Logger LOG = LoggerFactory.getLogger(ProjectGenerator.class);

    private final TaskLogger taskLogger;

    private final FileUtil fileUtil;

    private final ClassGenerator classGenerator;

    private final PowershellMavenAzFunCaller powershellMavenAzFunCaller;

    private final PomGenerator pomGenerator;

    private final LocalSettingsGenerator localSettingsGenerator;


    public Project generateProject(FunctionApp functionApp, ServicePrincipal servicePrincipal, AppConfig appConfig)
            throws IOException {
        String resolvedTempDir = powershellMavenAzFunCaller.getTempDir();

        Project project = initProject(functionApp, resolvedTempDir);

        powershellMavenAzFunCaller.generateProject(project, resolvedTempDir);

        writeClassFile(project, functionApp.getTriggerList(), functionApp.getClientList(), appConfig);
        writePomFile(project, functionApp.getTriggerList(), functionApp.getClientList(), servicePrincipal);
        writeLocalSettingsFile(project, functionApp.getTriggerList(), functionApp.getClientList());

        powershellMavenAzFunCaller.buildProject(project);

        if(appConfig.isLocalDeploymentOnly() == false) {
            powershellMavenAzFunCaller.deployProject(project);
        }

        return project;
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

    private void writeClassFile(Project project, List<FunctionAppTrigger> triggers, List<FunctionAppClient> clients, AppConfig appConfig)
            throws IOException {
        String classCode = classGenerator.generateClassCode(project, triggers, clients, appConfig);

        String classFilePath = project.getProjectRoot() + "\\src\\main\\java\\com\\werner\\" + project.getArtifactId() + "\\GeneratedClass.java";
        fileUtil.writeContentToFile(classFilePath, classCode);
        JavaTask javaTask = new JavaTask(
                "Generate a file in " + classFilePath + " and where the triggers and bindings are added to as code");
        taskLogger.addLogItem(javaTask, "Generating Java Class for Trigger and Bindings");
    }

    private void writePomFile(Project project, List<FunctionAppTrigger> triggers, List<FunctionAppClient> clients, ServicePrincipal servicePrincipal)
            throws IOException {
        String pomCode = pomGenerator.generateCode(project, triggers, clients, servicePrincipal);
        String pomPath = project.getProjectRoot() + "\\pom.xml";

        fileUtil.writeContentToFile(pomPath, pomCode);
        JavaTask javaTask = new JavaTask("Delete " + pomPath + " and create a new one");
        taskLogger.addLogItem(javaTask, "Generating Pom");
    }

    private void writeLocalSettingsFile(Project project, List<FunctionAppTrigger> triggerList,
            List<FunctionAppClient> clientList) throws IOException {
        String localSettingsCode = localSettingsGenerator.generateCode(triggerList, clientList);

        String localSettingsPath = project.getProjectRoot() + "\\local.settings.json";
        fileUtil.writeContentToFile(localSettingsPath, localSettingsCode);
        JavaTask javaTask = new JavaTask("Delete " + localSettingsPath + " and add a new one");
        taskLogger.addLogItem(javaTask, "Generating localSettings");
    }
}

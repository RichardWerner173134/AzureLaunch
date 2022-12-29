package com.werner.bl.functionapp.codegeneration;

import com.werner.bl.functionapp.codegeneration.generators.CodeGenerationResult;
import com.werner.bl.functionapp.codegeneration.generators.client.GetClientGenerator;
import com.werner.bl.functionapp.codegeneration.generators.triggers.HttpGetTrigger;
import com.werner.bl.functionapp.codegeneration.generators.triggers.ServicebusTopicTriggerGenerator;
import com.werner.bl.functionapp.codegeneration.model.FunctionApp;
import com.werner.bl.functionapp.codegeneration.model.FunctionAppClient;
import com.werner.bl.functionapp.codegeneration.model.FunctionAppTrigger;
import com.werner.bl.resourcecreation.model.graph.node.ResourceGroup;
import com.werner.powershell.PowershellMavenAzFunCaller;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@AllArgsConstructor
public class FunctionAppCodeGenerator {

    private final ServicebusTopicTriggerGenerator servicebusTopicTriggerGenerator;

    private final GetClientGenerator getClientGenerator;

    private final PowershellMavenAzFunCaller powershellMavenAzFunCaller;

    private final HttpGetTrigger httpGetTrigger;

    private final TemplateResolver templateResolver;

    private final FileZipper fileZipper;

    private final String PLACEHOLDER_CODE = "PLACEHOLDER_CODE";

    private final String PLACEHOLDER_IMPORT = "PLACEHOLDER_IMPORT";

    private final String PLACEHOLDER_PACKAGE = "PLACEHOLDER_PACKAGE";

    private static String RESOLVED_TEMP_DIR = null;

    public FunctionAppCodeGenerationResult generateFunctionAppProject(FunctionApp functionApp, ResourceGroup resourceGroup) {

        if(RESOLVED_TEMP_DIR == null) {
            RESOLVED_TEMP_DIR = powershellMavenAzFunCaller.getTempDir();
        }

        String mavenProjectName = functionApp.getFunctionAppName();
        String mavenPackageName = "package com.werner." + mavenProjectName + ";";

        try {
            powershellMavenAzFunCaller.generateProject(mavenProjectName);

            String functionAppBaseClassCode = templateResolver.resolveTemplate(TemplateName.FUNCTION_APP_BASE_CLASS);
            List<CodeGenerationResult> codeComponents = generateCode(functionApp);
            String fullCode = replacePlaceholders(functionAppBaseClassCode, codeComponents, mavenPackageName);

            writeToPom(mavenProjectName, codeComponents);
            writeToClassFile(mavenProjectName, fullCode);

            powershellMavenAzFunCaller.buildProject(mavenProjectName);

            // TODO check if function artifact was built
            String folderToZipPath = new File(RESOLVED_TEMP_DIR + "\\" + mavenProjectName + "\\target\\azure-functions").listFiles()[0].getCanonicalPath();
            String zipPath = RESOLVED_TEMP_DIR + "\\" + mavenProjectName + "\\zippie.zip";
            zipBuildArtifactFolder(folderToZipPath, zipPath);

            return new FunctionAppCodeGenerationResult(zipPath, mavenProjectName, resourceGroup.getName());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void writeToPom(String mavenProjectName, List<CodeGenerationResult> codeComponents) {
        String dependencies = "";

        for (CodeGenerationResult codeComponent : codeComponents) {
            for (String dependency : codeComponent.getNecessaryDependencies()) {
                dependencies += dependency;
            }
        }

        if(dependencies.equals("")) {
            return;
        }

        try {
            String content = "";

            File file = new File(RESOLVED_TEMP_DIR + "\\" + mavenProjectName + "\\pom.xml");
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                content += line + "\n";
            }

            bufferedReader.close();


            String dependencyStartTag = "<dependencies>\n";
            content = content.replace(dependencyStartTag, dependencyStartTag + dependencies);

            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
            bufferedWriter.write(content);
            bufferedWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String replacePlaceholders(String functionAppBaseClassCode, List<CodeGenerationResult> codeComponents, String packageStatement) {
        String result = functionAppBaseClassCode;
        Set<String> imports = new HashSet<>();

        for (CodeGenerationResult codeComponent : codeComponents) {
            result = result.replace(PLACEHOLDER_CODE, codeComponent.getCode() + "\n\n" + PLACEHOLDER_CODE);
            imports.addAll(codeComponent.getNecessaryImports());
        }

        for (String importStatement : imports) {
            result = result.replace(PLACEHOLDER_IMPORT, importStatement + "\n" + PLACEHOLDER_IMPORT);
        }

        result = result.replace(PLACEHOLDER_CODE, "");
        result = result.replace(PLACEHOLDER_IMPORT, "");
        result = result.replace(PLACEHOLDER_PACKAGE, packageStatement);
        return result;
    }

    private List<CodeGenerationResult> generateCode(FunctionApp functionApp) {
        List<CodeGenerationResult> results = new ArrayList<>();

        for (FunctionAppTrigger trigger : functionApp.getTriggerList()) {
            CodeGenerationResult result = null;
            switch (trigger.getTriggerType()) {
                case SERVICE_BUS_PUB_SUB:
                    result = servicebusTopicTriggerGenerator.generateCode(trigger);
                    break;
                case SERVICE_BUS_QUEUE:
                    break;
                case HTTP_GET:
                    result = httpGetTrigger.generateCode(trigger);
                    break;
                case HTTP_POST:
                    break;
            }
            results.add(result);
        }

        for (FunctionAppClient client : functionApp.getClientList()) {
            CodeGenerationResult result = getClientGenerator.generateCode(client);
            results.add(result);
        }

        return results;
    }

    private void writeToClassFile(String functionappName, String fullCode) {
        String classFile = RESOLVED_TEMP_DIR + "\\" + functionappName + "\\src\\main\\java\\com\\werner\\" + functionappName + "\\GeneratedClass.java";
        String unnessecaryClassFile = RESOLVED_TEMP_DIR + "\\" + functionappName + "\\src\\main\\java\\com\\werner\\" + functionappName + "\\Function.java";

        try {
            new File(unnessecaryClassFile).delete();
            new File(classFile).createNewFile();
            BufferedWriter writer = new BufferedWriter(new FileWriter(classFile));
            writer.write(fullCode);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void zipBuildArtifactFolder(String folderToZipPath, String zipPath) {
        try {
            fileZipper.zipFiles(folderToZipPath, zipPath);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

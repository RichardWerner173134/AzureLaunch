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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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

    public FunctionAppCodeGenerationResult generateFunctionAppProject(FunctionApp functionApp, ResourceGroup resourceGroup) {

        String tempDir = powershellMavenAzFunCaller.getTempDir();
        String mavenProjectName = functionApp.getFunctionAppName();
        String mavenPackageName = "package com.werner." + mavenProjectName + ";";

        try {
            powershellMavenAzFunCaller.generateProject(mavenProjectName);
            //Thread.sleep(10000);

            String functionAppBaseClassCode = templateResolver.resolveTemplate(TemplateName.FUNCTION_APP_BASE_CLASS);
            List<CodeGenerationResult> codeComponents = generateCode(functionApp);
            String fullCode = replacePlaceholders(functionAppBaseClassCode, codeComponents, mavenPackageName);

            insertCode(tempDir, mavenProjectName, fullCode);

            powershellMavenAzFunCaller.buildProject(mavenProjectName);

            // TODO check if function artifact was built
            zipBuildArtifactFolder(tempDir, mavenProjectName);

            return new FunctionAppCodeGenerationResult(tempDir + "\\" + mavenProjectName, mavenProjectName, resourceGroup.getName());
        } catch (Exception e) {
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
            //CodeGenerationResult result = clientGenerator.generateClient(client);
            //results.add(result);
        }

        return results;
    }

    private void insertCode(String tempDir, String functionappName, String fullCode) {
        String classFile = tempDir + "\\" + functionappName + "\\src\\main\\java\\com\\werner\\" + functionappName + "\\GeneratedClass.java";
        String unnessecaryClassFile = tempDir + "\\" + functionappName + "\\src\\main\\java\\com\\werner\\" + functionappName + "\\Function.java";

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

    private void zipBuildArtifactFolder(String tempDir, String mavenProjectName) {
        try {
            String folderToZipPath = new File(tempDir + "\\" + mavenProjectName + "\\target\\azure-functions").listFiles()[0].getCanonicalPath();
            String outputFile = tempDir + "\\" + mavenProjectName + "\\zippie.zip";
            fileZipper.zipFiles(folderToZipPath, outputFile);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

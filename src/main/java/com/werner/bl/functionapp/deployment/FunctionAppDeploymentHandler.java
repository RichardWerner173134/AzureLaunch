package com.werner.bl.functionapp.deployment;

import com.profesorfalken.jpowershell.PowerShellResponse;
import com.werner.bl.functionapp.codegeneration.FunctionAppCodeGenerationResult;
import com.werner.powershell.AbstractPowershellCaller;
import org.springframework.stereotype.Component;

@Component
public class FunctionAppDeploymentHandler extends AbstractPowershellCaller {

    private final static String SCRIPT = "Publish-AzWebapp -ResourceGroupName %s -Name %s -ArchivePath %s -Force; ";

    public void deployFunctionAppCode(FunctionAppCodeGenerationResult result) throws Exception {
        String cmd = getScript(result.getResourceGroupName(), result.getFunctionAppName(), result.getArchivePath());
        executePowershellCommand(cmd);
    }

    private String getScript(String resourceGroupName, String functionName, String archivePath) {
        return String.format(SCRIPT, resourceGroupName, functionName, archivePath);
    }

    @Override
    public void handleResponse(PowerShellResponse powerShellResponse) {
        // TODO handle Azure Code Deployment Response
    }
}

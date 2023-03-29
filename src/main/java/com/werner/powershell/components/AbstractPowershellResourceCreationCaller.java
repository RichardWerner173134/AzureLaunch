package com.werner.powershell.components;

import com.werner.bl.exception.AzureResourceCreationFailedException;
import com.werner.bl.resourcecreation.model.graph.node.AbstractResourceNode;
import com.werner.bl.resourcecreation.model.graph.node.ResourceGroup;
import com.werner.log.PowershellTask;
import com.werner.log.TaskLogger;
import com.werner.powershell.AbstractPowershellCaller;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractPowershellResourceCreationCaller extends AbstractPowershellCaller {

    protected final static String TEMPLATE_DIR = "templates\\";

    public AbstractPowershellResourceCreationCaller(TaskLogger logger) {
        super(logger);
    }

    protected abstract String getScript(List<AbstractResourceNode> resourceFamily, String resourceGroup)
            throws IOException;

    protected abstract String getScript(ResourceGroup rgNode);

    public void createResourceGroup(ResourceGroup rg) throws AzureResourceCreationFailedException {
        String command = getScript(rg);
        PowershellTask powershellTask = executeSingleCommand(command);
        logger.addLogItem(powershellTask, "Creating ResourceGroup " + rg.getName());
        validateAzureResourceCreation(powershellTask);
    }

    public void createResourceInResourceGroup(List<AbstractResourceNode> resourceFamily, String resourceGroup)
            throws AzureResourceCreationFailedException, IOException {
        String command = getScript(resourceFamily, resourceGroup);
        PowershellTask powershellTask = executeSingleCommand(command);
        String resourceName = resourceFamily.stream().map(n -> n.getName()).collect(Collectors.joining(", "));
        logger.addLogItem(powershellTask, "Creating Resource {" + resourceName + "} in ResourceGroup " + resourceGroup);
        validateAzureResourceCreation(powershellTask);
    }

    private void validateAzureResourceCreation(PowershellTask powershellTask) throws AzureResourceCreationFailedException {
        if(powershellTask.getExitCode() == 0) {
            return;
        }
        throw new AzureResourceCreationFailedException("Resource deployment failed. \n" + powershellTask.getErrorLog());
    }
}

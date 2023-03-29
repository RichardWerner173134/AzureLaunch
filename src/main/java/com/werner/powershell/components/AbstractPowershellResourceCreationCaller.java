package com.werner.powershell.components;

import com.werner.bl.resourcecreation.model.graph.node.AbstractResourceNode;
import com.werner.bl.resourcecreation.model.graph.node.ResourceGroup;
import com.werner.log.PowershellResponse;
import com.werner.log.PowershellTaskLogger;
import com.werner.powershell.AbstractPowershellCaller;

import java.util.List;

public abstract class AbstractPowershellResourceCreationCaller extends AbstractPowershellCaller {

    protected final static String TEMPLATE_DIR = "src\\main\\resources\\templates\\";

    public AbstractPowershellResourceCreationCaller(PowershellTaskLogger logger) {
        super(logger);
    }

    protected abstract String getScript(List<AbstractResourceNode> resourceFamily, String resourceGroup);

    protected abstract String getScript(ResourceGroup rgNode);

    public void createResourceGroup(ResourceGroup rg) {
        String command = getScript(rg);
        PowershellResponse powershellResponse = executeSingleCommand(command);
        logger.addLogItem(powershellResponse, "Creating ResourceGroup");
    }

    public void createResourceInResourceGroup(List<AbstractResourceNode> resourceFamily, String resourceGroup) {
        String command = getScript(resourceFamily, resourceGroup);
        PowershellResponse powershellResponse = executeSingleCommand(command);
        logger.addLogItem(powershellResponse, "Creating Resource in ResourceGroup");
    }
}

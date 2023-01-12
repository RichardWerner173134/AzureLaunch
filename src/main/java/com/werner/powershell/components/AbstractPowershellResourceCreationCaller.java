package com.werner.powershell.components;

import com.werner.bl.resourcecreation.model.graph.node.AbstractResourceNode;
import com.werner.bl.resourcecreation.model.graph.node.ResourceGroup;
import com.werner.powershell.AbstractPowershellCaller;

import java.util.List;

public abstract class AbstractPowershellResourceCreationCaller extends AbstractPowershellCaller {

    protected final static String TEMPLATE_DIR = "src\\main\\resources\\templates\\";

    protected abstract String getScript(List<AbstractResourceNode> resourceFamily, String resourceGroup);

    protected abstract String getScript(ResourceGroup rgNode);

    public void createResourceGroup(ResourceGroup rg) throws Exception {
        String command = getScript(rg);
        executeSingleCommand(command);
    }

    public void createResourceInResourceGroup(List<AbstractResourceNode> resourceFamily, String resourceGroup) {
        String command = getScript(resourceFamily, resourceGroup);
        executeSingleCommand(command);
    }
}

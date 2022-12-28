package com.werner.powershell.components;

import com.profesorfalken.jpowershell.PowerShellResponse;
import com.werner.bl.resourcecreation.model.graph.node.AbstractResourceNode;
import com.werner.bl.resourcecreation.model.graph.node.ResourceGroup;
import com.werner.powershell.AbstractPowershellCaller;

import java.util.List;

public abstract class AbstractPowershellResourceCreationCaller extends AbstractPowershellCaller {

    protected final static String TEMPLATE_DIR = "src/main/resources/templates/";

    protected abstract String getScript(List<AbstractResourceNode> resourceFamily, String resourceGroup);

    protected abstract String getScript(ResourceGroup rgNode);

    public void createResourceGroup(ResourceGroup rg) throws Exception {
        String cmd = getScript(rg);
        System.out.println(cmd);
        executePowershellCommand(cmd);
    }

    public void createResourceInResourceGroup(List<AbstractResourceNode> resourceFamily, String resourceGroup) throws Exception {
        String cmd = getScript(resourceFamily, resourceGroup);
        System.out.println(cmd);
        executePowershellCommand(cmd);
    }

    @Override
    public void handleResponse(PowerShellResponse powerShellResponse) throws Exception {
        // do nothing, because Powershell Library cant handle errors
        // all resources are created correctly
        // if (powerShellResponse.isError()) {
        //     throw new Exception("An error occured while creating resources: " + powerShellResponse.getCommandOutput());
        // } else if(powerShellResponse.isTimeout()){
        //     throw new Exception("Timeout while creating resources: " + powerShellResponse.getCommandOutput());
        // }
    }
}

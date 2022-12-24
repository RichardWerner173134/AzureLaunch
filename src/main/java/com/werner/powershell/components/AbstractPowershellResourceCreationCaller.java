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
        executePowershellCommand(cmd);
    }

    public void createResourceInResourceGroup(List<AbstractResourceNode> resourceFamily, String resourceGroup) throws Exception {
        String cmd = getScript(resourceFamily, resourceGroup);
        executePowershellCommand(cmd);
    }


    @Override
    public void handleResponse(PowerShellResponse powerShellResponse){
        // TODO Powershellcommand execution only throws if timeout or Powershellerror
        // handle Azure response stuff
    }
}

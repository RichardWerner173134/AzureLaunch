package com.werner.powershell.components;

import com.werner.bl.resourcecreation.model.ResourceGroup;
import com.werner.bl.resourcecreation.model.graph.node.AbstractResourceNode;
import com.werner.powershell.AbstractPowershellCaller;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ResourceGroupPowershellCaller extends AbstractPowershellCaller {

    private final String SCRIPT = "New-AzResourceGroup -Name %s -Location %s -Force";

    @Override
    protected String getScript(List<AbstractResourceNode> resourceFamily, String resourceGroup) {
        throw new UnsupportedOperationException("ResourceGroup cant have a ResourceGroup");
    }

    @Override
    protected String getScript(ResourceGroup rgNode) {
        return String.format(SCRIPT, rgNode.getResourceGroupName(), rgNode.getResourceGroupLocation());
    }
}

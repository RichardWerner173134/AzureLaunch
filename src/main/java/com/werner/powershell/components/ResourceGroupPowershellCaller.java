package com.werner.powershell.components;

import com.werner.bl.resourcecreation.model.graph.node.AbstractResourceNode;
import com.werner.bl.resourcecreation.model.graph.node.ResourceGroup;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ResourceGroupPowershellCaller extends AbstractPowershellResourceCreationCaller {

    private final String SCRIPT = "New-AzResourceGroup -Name %s -Location %s -Force";

    protected String getScript(List<AbstractResourceNode> resourceFamily, String resourceGroup) {
        throw new UnsupportedOperationException("ResourceGroup cant have a ResourceGroup");
    }

    protected String getScript(ResourceGroup rgNode) {
        return String.format(SCRIPT, rgNode.getName(), rgNode.getResourceGroupLocation());
    }
}

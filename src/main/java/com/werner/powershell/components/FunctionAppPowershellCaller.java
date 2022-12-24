package com.werner.powershell.components;

import com.werner.bl.resourcecreation.model.ResourceType;
import com.werner.bl.resourcecreation.model.graph.node.AbstractResourceNode;
import com.werner.bl.resourcecreation.model.graph.node.ResourceGroup;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FunctionAppPowershellCaller extends AbstractPowershellResourceCreationCaller {
    private final String SCRIPT = "$parameters = @{}; "
            + "$parameters.Add('siteName', '%s'); "
            + "New-AzResourceGroupDeployment -ResourceGroupName %s -TemplateFile %s -TemplateParameterObject $parameters";

    private final String SCRIPT_NAME = "functionAppT.json";

    private final String FUNCTIONAPP_TEMPLATE_DIR = TEMPLATE_DIR + "functionapp/";

    protected String getScript(List<AbstractResourceNode> resourceFamily, String resourceGroup) {
        String sitename = resourceFamily.stream().filter(r -> r.getResourceType() == ResourceType.FUNCTION_APP).findFirst().get().getName();
        return String.format(SCRIPT, sitename, resourceGroup, FUNCTIONAPP_TEMPLATE_DIR + SCRIPT_NAME);
    }

    protected String getScript(ResourceGroup rgNode) {
        throw new UnsupportedOperationException("Resources need a ResourceGroup to live in");
    }
}

package com.werner.bl.resourcecreation.model.deployment;

import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import com.werner.bl.resourcecreation.model.graph.node.AbstractResourceNode;
import com.werner.bl.resourcecreation.model.graph.node.ResourceGroup;
import com.werner.powershell.components.FunctionAppPowershellCaller;
import com.werner.powershell.components.ResourceGroupPowershellCaller;
import com.werner.powershell.components.ServiceBusSubscriptionPowershellCaller;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;


@Component
public class DeploymentHandler {

	private String cachedRgNameForCurrentDeployments;

	private ResourceGroupPowershellCaller rgPSCaller;

	private ServiceBusSubscriptionPowershellCaller sbsubPSCaller;

	private FunctionAppPowershellCaller funAppPSCaller;

	private Set<String> commands = new LinkedHashSet<>();

	public DeploymentHandler(ResourceGroupPowershellCaller rgPSCaller,
							 ServiceBusSubscriptionPowershellCaller sbsubPSCaller,
							 FunctionAppPowershellCaller funAppPSCaller) {
		this.rgPSCaller = rgPSCaller;
		this.sbsubPSCaller = sbsubPSCaller;
		this.funAppPSCaller = funAppPSCaller;
	}

	public void writeDeploymentScript(Deployment deployment) throws Exception {

		AbstractResourceNode firstResource = deployment.getDeploymentComposite().get(0);

		switch (firstResource.getResourceType()) {
			case RESOURCE_GROUP:
				commands.add(rgPSCaller.createResourceGroup((ResourceGroup) firstResource));
				cachedRgNameForCurrentDeployments = firstResource.getName();
				break;
			case FUNCTION:
			case FUNCTION_APP:
				commands.add(funAppPSCaller.createResourceInResourceGroup(deployment.getDeploymentComposite(), cachedRgNameForCurrentDeployments));
				break;
			case KEYVAULT:
				break;
			case KEYVAULT_SECRET:
				break;
			case SERVICEBUS_SUBSCRIPTION:
			case SERVICEBUS_TOPIC:
			case SERVICEBUS_NAMESPACE:
				commands.add(sbsubPSCaller.createResourceInResourceGroup(deployment.getDeploymentComposite(), cachedRgNameForCurrentDeployments));
				break;
			case VNET:
				break;
			case STORAGE_ACCOUNT:
				break;
			case APP_SERVICE_ENVIRONMENT:
				break;
		}
	}

	public void executeDeploymentScript() {
		StringBuilder s = new StringBuilder();
		for (String command : commands) {
			s.append(command);
		}

		Map<String, String> configMap = new HashMap<>();
		configMap.put("maxWait", "150000");

		PowerShell powerShell = PowerShell.openSession();
		PowerShellResponse powerShellResponse = powerShell.configuration(configMap).executeCommand(s.toString());
		powerShell.close();
	}

	public void executeDeploymentScriptOld() {
		PowerShellResponse powerShellResponse = PowerShell.executeSingleCommand("echo $home/temp");
		String tempDir = powerShellResponse.getCommandOutput();

		File file = new File(tempDir + "\\create.ps1");

		try {

			if (!file.exists()) {
				file.createNewFile();
			}

			BufferedWriter br = new BufferedWriter(new FileWriter(file));

			for (String command : commands) {
				br.append(command);
			}

			br.close();
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
	}
}

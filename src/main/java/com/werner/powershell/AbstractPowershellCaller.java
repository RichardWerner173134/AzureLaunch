package com.werner.powershell;

import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import com.werner.bl.resourcecreation.model.graph.node.AbstractResourceNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;
import java.util.Map;

public abstract class AbstractPowershellCaller {

	protected final static String TEMPLATE_DIRECTORY = "src/main/resources/templates/";

	@Autowired
	@Qualifier("configMap")
	private Map<String, String> test;

	protected abstract String getScript(List<AbstractResourceNode> resourceFamily, String resourceGroup);

	public void executeScriptWithParams(List<AbstractResourceNode> resourceFamily, String resourceGroup) throws Exception {
		String cmd = getScript(resourceFamily, resourceGroup);

		PowerShell powerShell = PowerShell.openSession();
		PowerShellResponse powerShellResponse = powerShell
				.configuration(test)
				.executeCommand(cmd);
		powerShell.close();

		if (powerShellResponse.isError()) {
			throw new Exception("An error occured while creating resources: " + powerShellResponse.getCommandOutput());
		} else if(powerShellResponse.isTimeout()){
			throw new Exception("Timeout while creating resources: " + powerShellResponse.getCommandOutput());
		}

		handleResponse(powerShellResponse);
	}

	private void handleResponse(PowerShellResponse powerShellResponse){
		// TODO Powershellcommand execution only throws if timeout or Powershellerror
		// handle Azure response stuff
	}
}

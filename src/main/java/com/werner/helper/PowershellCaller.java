package com.werner.helper;

import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class PowershellCaller {
	private static final String SCRIPT = ""
			+ "$parameters = @{}; "
			+ "$parameters.Add('service_BusNamespace_Name', '%s'); "
			+ "$parameters.Add('serviceBusTopicName', '%s'); "
			+ "$parameters.Add('serviceBusSubscriptionName', '%s'); "
			+ "New-AzResourceGroupDeployment -ResourceGroupName %s -TemplateFile %s -TemplateParameterObject $parameters";

	private static Logger LOGGER = LoggerFactory
			.getLogger(PowershellCaller.class);

	public String executeScriptWithParams(String scriptPath, String[] params) throws Exception {
		// creating azure resources can take some time
		Map<String, String> myConfig = new HashMap<>();
		myConfig.put("maxWait", "100000");
		String cmd = String.format(SCRIPT, params[0], params[1], params[2], params[3], scriptPath + "serviceBusT.json");

		PowerShell powerShell = PowerShell.openSession();
		PowerShellResponse powerShellResponse = powerShell.configuration(myConfig)
				.executeCommand(cmd);
		powerShell.close();

		if (powerShellResponse.isError()) {
			throw new Exception("An error occured while creating resources: " + powerShellResponse.getCommandOutput());
		} else if(powerShellResponse.isTimeout()){
			throw new Exception("Timeout while creating resources: " + powerShellResponse.getCommandOutput());
		}
		return powerShellResponse.getCommandOutput();
	}
}

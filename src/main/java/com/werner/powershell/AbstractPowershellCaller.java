package com.werner.powershell;

import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Map;

public abstract class AbstractPowershellCaller {

	@Autowired
	@Qualifier("configMap")
	private Map<String, String> test;

	protected void executePowershellCommand(String command) throws Exception {
		PowerShell powerShell = PowerShell.openSession();
		PowerShellResponse powerShellResponse = powerShell
				.configuration(test)
				.executeCommand(command);
		powerShell.close();

		if (powerShellResponse.isError()) {
			throw new Exception("An error occured while creating resources: " + powerShellResponse.getCommandOutput());
		} else if(powerShellResponse.isTimeout()){
			throw new Exception("Timeout while creating resources: " + powerShellResponse.getCommandOutput());
		}

		handleResponse(powerShellResponse);
	}

	public abstract void handleResponse(PowerShellResponse powerShellResponse);
}

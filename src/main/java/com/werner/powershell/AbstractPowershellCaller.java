package com.werner.powershell;

import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Map;

public abstract class AbstractPowershellCaller {

	@Autowired
	@Qualifier("configMap")
	protected Map<String, String> config;

	protected void executePowershellCommand(String command) throws Exception {
		PowerShell powerShell = PowerShell.openSession();
		PowerShellResponse powerShellResponse = powerShell
				.configuration(config)
				.executeCommand(command);
		powerShell.close();

		handleResponse(powerShellResponse);
	}

	protected PowerShellResponse executePowershellWithResponse(String command) {
		PowerShell powershell = PowerShell.openSession();
		PowerShellResponse powerShellResponse = powershell.executeCommand(command);
		powershell.close();
		return powerShellResponse;
	}

	public abstract void handleResponse(PowerShellResponse powerShellResponse) throws Exception;
}

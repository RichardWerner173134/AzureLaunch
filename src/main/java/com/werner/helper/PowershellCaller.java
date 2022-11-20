package com.werner.helper;

import com.profesorfalken.jpowershell.PowerShell;

public class PowershellCaller {
	public String executeScript(String command) {
		return PowerShell.executeSingleCommand(command).getCommandOutput();
	}
}

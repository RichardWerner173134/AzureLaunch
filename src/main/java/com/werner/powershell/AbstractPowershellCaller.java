package com.werner.powershell;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public abstract class AbstractPowershellCaller {
	protected String resolvedTempDir;

	public String getTempDir() {
		if(resolvedTempDir == null) {
			executeSingleCommand(String.format("if(-Not (Test-Path -Path %s)) {mkdir %s}",
					"$home\\temp", "$home\\temp"));
			resolvedTempDir = executeSingleCommandWithResponse("Write-Host $home\\temp");
		}
		return resolvedTempDir;
	}

	protected void executeSingleCommand(String command) {

		ProcessBuilder  pb = new ProcessBuilder("powershell.exe", "-Command", command);

		try {
			Process process = pb.start();

			BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));

			BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));

			String s = null;
			while ((s = stdInput.readLine()) != null) {
				System.out.println(s);
			}

			while ((s = stdError.readLine()) != null) {
				System.out.println(s);
			}

			// wait to complete underlying command execution
			int exitCode = process.waitFor();
			System.out.println("\nTerminated with error code : " + exitCode);

		} catch (IOException | InterruptedException ex) {
			ex.printStackTrace();
		}
	}

	protected void executeCommandChain(List<String> commands) {
		for (String cmd : commands) {
			executeSingleCommand(cmd);
		}
	}

	protected String executeSingleCommandWithResponse(String command) {
		ProcessBuilder pb = new ProcessBuilder("powershell.exe", "-Command", command);

		String result = "";

		try {
			Process process = pb.start();

			BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));

			BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));

			String s = null;
			while ((s = stdInput.readLine()) != null) {
				System.out.println(s);
				result += s;
			}

			while ((s = stdError.readLine()) != null) {
				System.out.println(s);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
}

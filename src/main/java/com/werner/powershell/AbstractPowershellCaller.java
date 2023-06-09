package com.werner.powershell;

import com.werner.log.PowershellTask;
import com.werner.log.TaskLogger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public abstract class AbstractPowershellCaller {
	protected String resolvedTempDir;

	protected TaskLogger logger;

	public AbstractPowershellCaller(TaskLogger logger) {
		this.logger = logger;
	}

	public String getTempDir() {
		if(resolvedTempDir == null) {
			executeSingleCommand(String.format("if(-Not (Test-Path -Path %s)) {mkdir %s}",
					"$env:TEMP\\azure-generated-files", "env:TEMP\\azure-generated-files"));
			PowershellTask response = executeSingleCommandWithResponse("Write-Host $env:TEMP\\azure-generated-files");

			// TODO maybe add to log
			resolvedTempDir = response.getLog().substring(0, response.getLog().indexOf("\n"));
		}
		return resolvedTempDir;
	}

	protected PowershellTask executeSingleCommand(String command) {

		ProcessBuilder pb = new ProcessBuilder("powershell.exe", "-Command", command);

		try {
			Process process = pb.start();

			BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));

			BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));

			String s;
			String log = "";
			while ((s = stdInput.readLine()) != null) {
				log += s + "\n";
			}

			String errorLog = "";
			while ((s = stdError.readLine()) != null) {
				errorLog += s + "\n";
			}

			// wait to complete underlying command execution
			int exitCode = process.waitFor();

			return new PowershellTask(log, errorLog, exitCode);
		} catch (IOException | InterruptedException ex) {
			ex.printStackTrace();
			return null;
			// TODO exception handling
		}
	}

	protected PowershellTask executeSingleCommandWithResponse(String command) {
		ProcessBuilder pb = new ProcessBuilder("powershell.exe", "-Command", command);


		try {
			Process process = pb.start();

			BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));

			BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));

			String s = "";
			String log = "";
			while ((s = stdInput.readLine()) != null) {
				log += s + "\n";
			}

			String errorLog = "";
			while ((s = stdError.readLine()) != null) {
				errorLog += s + "\n";
			}

			// wait to complete underlying command execution
			int exitCode = process.waitFor();

			return new PowershellTask(log, errorLog, exitCode);
		} catch (InterruptedException | IOException e) {
			e.printStackTrace();
			return null;
			// TODO exception handling
		}
	}
}

package com.werner.bl.input;

import com.google.gson.Gson;
import generated.internal.v1_0_0.model.AzCodegenRequest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class UserInputReader {
	public AzCodegenRequest readUserInput(File file) throws IOException {
		String jsonContent = readSampleFile(file);
		return new Gson().fromJson(jsonContent, AzCodegenRequest.class);
	}

	private String readSampleFile(File file) throws IOException {
		String content = "";
		BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
		String line = "";
		while((line = bufferedReader.readLine()) != null) {
			content += line;
		}
		return content;
	}
}

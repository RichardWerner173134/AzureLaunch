package com.werner.bl.input;

import com.google.gson.Gson;
import com.werner.bl.input.generated.ParseFileRequest;

import java.io.*;

public class UserInputReader {
	public ParseFileRequest readUserInput(File file) throws IOException {
		String jsonContent = readSampleFile(file);
		return new Gson().fromJson(jsonContent, ParseFileRequest.class);
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

package com.werner;

import com.werner.bl.processor.UserInputProcessor;

public class Main {

    public static void main(String[] args) {
        String filePath = args.length > 0
                ? args[0]
                : "D:\\Masterprojekt\\GenerationProject\\specification\\stage1.json";

        UserInputProcessor processor = new UserInputProcessor(filePath);
        processor.process();

    }
}

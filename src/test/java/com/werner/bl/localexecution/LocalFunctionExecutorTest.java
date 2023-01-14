package com.werner.bl.localexecution;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class LocalFunctionExecutorTest {

    @InjectMocks
    private LocalFunctionExecutor classUnderTest;

    @Test
    public void testExecution() {
        classUnderTest.startFunction("C:\\Users\\Richard\\temp\\funcapp01\\target\\azure-functions\\funcapp01", 8080);
        classUnderTest.startFunction("C:\\Users\\Richard\\temp\\funcapp02\\target\\azure-functions\\funcapp01", 8081);
    }


}
package com.werner.powershell;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class PowershellConfig {

    @Bean("configMap")
    public Map<String, String> getPowershellConfigMap() {
        Map<String, String> configMap = new HashMap<>();
        configMap.put("maxWait", "20000");
        return configMap;
    }
}

package com.werner.bl.codegeneration.generators.projectlevel;

import com.werner.bl.codegeneration.helper.TemplateName;
import com.werner.bl.codegeneration.helper.TemplateResolver;
import com.werner.bl.codegeneration.model.FunctionAppClient;
import com.werner.bl.codegeneration.model.FunctionAppTrigger;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@AllArgsConstructor
public class LocalSettingsGenerator {

	private final static String PLACEHOLDER_CONNECTIONSTRINGS = "PLACEHOLDER_CONNECTIONSTRINGS";

	private final TemplateResolver templateResolver;

	public String generateCode(List<FunctionAppTrigger> triggers, List<FunctionAppClient> clients) {
		String result = templateResolver.resolveTemplate(TemplateName.LOCAL_SETTINGS);

		String property = "\t\t\"%s\": \"%s\",\n";

		for (FunctionAppTrigger trigger : triggers) {
			for (Map.Entry<String, String> entry : trigger.getTriggerParams().entrySet()) {
				String k = entry.getKey();
				String v = entry.getValue();
				if (k.startsWith(trigger.getTriggerName())) {
					String connectionStringKVPair = String.format(property, k, v);
					result = result.replace(PLACEHOLDER_CONNECTIONSTRINGS, connectionStringKVPair + PLACEHOLDER_CONNECTIONSTRINGS);
				}
			}
		}
//      TODO dont hardcode clienturl in code, use localsettings/appsettings
//		for (FunctionAppClient client : clients) {
//			for (Map.Entry<String, String> entry : client.getClientParams().entrySet()) {
//				String k = entry.getKey();
//				String v = entry.getValue();
//				if (k.startsWith(client.getClientName())) {
//					String connectionString = String.format(property, k, v);
//					result = result.replace(PLACEHOLDER_CONNECTIONSTRINGS, connectionString + PLACEHOLDER_CONNECTIONSTRINGS);
//				}
//			}
//		}

		return result
				.replace(",\n" + PLACEHOLDER_CONNECTIONSTRINGS, "")
				.replace(PLACEHOLDER_CONNECTIONSTRINGS, "");
	}
}

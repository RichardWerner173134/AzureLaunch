package com.werner.powershell;

import com.werner.bl.codegeneration.helper.TemplateResolver;
import com.werner.bl.resourcecreation.model.graph.node.ServicePrincipal;
import com.werner.log.PowershellResponse;
import com.werner.log.PowershellTaskLogger;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import static com.werner.bl.codegeneration.helper.TemplateName.SCRIPT_SERVICE_PRINCIPAL;

@Component
public class ServicePrincipalResolver extends AbstractPowershellCaller {

	private final String PLACEHOLDER_SERVICE_PRINCIPAL_NAME = "PLACEHOLDER_SERVICE_PRINCIPAL_NAME";

	private final String prefix = "Name AppId Tenant Secret: ";

	private final String delimiter = " ";

	private final TemplateResolver templateResolver;

	public ServicePrincipalResolver(PowershellTaskLogger logger, TemplateResolver templateResolver) {
		super(logger);
		this.templateResolver = templateResolver;
	}

	public ServicePrincipal getOrCreateServicePrincipal(String spName) {

		String cmd = templateResolver.resolveTemplate(SCRIPT_SERVICE_PRINCIPAL)
				.replaceAll(PLACEHOLDER_SERVICE_PRINCIPAL_NAME, spName);

		PowershellResponse powershellResponse = executeSingleCommandWithResponse(cmd);
		logger.addLogItem(powershellResponse, "GetOrCreate ServicePrincipal to deploy functions from - " + spName);
		String log = powershellResponse.getLog();

		int i = log.length() - 1;
		while (true) {
			String tail = log.substring(i);
			if (tail.startsWith(prefix)) {
				String valuesString = tail.replace(prefix, "");
				String[] values = valuesString.split(delimiter);
				return new ServicePrincipal(values[0], values[1], values[2], values[3]);
			}
			i--;
			if (i == 0) {
				throw new RuntimeException("Cannot extract values for ServicePrincipal");
			}
		}
	}
}

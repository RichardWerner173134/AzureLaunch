package com.werner.powershell;

import com.werner.bl.codegeneration.helper.TemplateResolver;
import com.werner.bl.exception.ServicePrincipalException;
import com.werner.bl.resourcecreation.model.graph.node.ServicePrincipal;
import com.werner.log.PowershellTask;
import com.werner.log.TaskLogger;
import org.springframework.stereotype.Component;

import static com.werner.bl.codegeneration.helper.TemplateName.SCRIPT_SERVICE_PRINCIPAL;

@Component
public class ServicePrincipalResolver extends AbstractPowershellCaller {

	private final String PLACEHOLDER_SERVICE_PRINCIPAL_NAME = "PLACEHOLDER_SERVICE_PRINCIPAL_NAME";

	private final String prefix = "Name AppId Tenant Secret: ";

	private final String delimiter = " ";

	private final TemplateResolver templateResolver;

	public ServicePrincipalResolver(TaskLogger logger, TemplateResolver templateResolver) {
		super(logger);
		this.templateResolver = templateResolver;
	}

	public ServicePrincipal getOrCreateServicePrincipal(String spName) throws ServicePrincipalException {

		String cmd = templateResolver.resolveTemplate(SCRIPT_SERVICE_PRINCIPAL)
				.replaceAll(PLACEHOLDER_SERVICE_PRINCIPAL_NAME, spName);

		PowershellTask powershellTask = executeSingleCommandWithResponse(cmd);
		logger.addLogItem(
				powershellTask, "GetOrCreate ServicePrincipal to authorize Maven to deploy functions from - " + spName);
		String log = powershellTask.getLog();

		int i = log.length() - 1;
		while (true) {
			String tail = log.substring(i);
			if (tail.startsWith(prefix)) {
				String valuesString = tail.replace(prefix, "");
				String[] values = valuesString.split(delimiter);

				String name = values[0].replaceAll("\n", "");
				String appId = values[1].replaceAll("\n", "");
				String tenant = values[2].replaceAll("\n", "");
				String secret = values[3].replaceAll("\n", "");
				return new ServicePrincipal(name, appId, tenant, secret);
			}
			i--;
			if (i <= 0) {
				throw new ServicePrincipalException("Something went wrong while extracting ServicePrincipal information");
			}
		}
	}
}

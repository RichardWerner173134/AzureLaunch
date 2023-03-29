package com.werner.powershell;

import com.werner.log.PowershellResponse;
import com.werner.log.PowershellTaskLogger;
import org.springframework.stereotype.Component;

@Component
public class ServiceBusResolver extends AbstractPowershellCaller {

    public ServiceBusResolver(PowershellTaskLogger logger) {
        super(logger);
    }

    public String resolveServiceBusConnectionString(String rg, String sbns) {
        StringBuilder sb = new StringBuilder();
        sb.append("(Get-AzServiceBusKey ");
        sb.append("-Name RootManageSharedAccessKey ");
        sb.append("-NamespaceName %s ");
        sb.append("-ResourceGroupName %s )");
        sb.append(".'PrimaryConnectionString'");

        String cmd = String.format(sb.toString(), sbns, rg);

        PowershellResponse powershellResponse = executeSingleCommandWithResponse(cmd);
        logger.addLogItem(powershellResponse, "Resolving Connectionstring of " + sbns);

        return powershellResponse.getLog().substring(0, powershellResponse.getLog().indexOf("\n"));
    }
}

package com.werner.powershell;

import com.werner.log.PowershellTask;
import com.werner.log.TaskLogger;
import org.springframework.stereotype.Component;

@Component
public class ServiceBusResolver extends AbstractPowershellCaller {

    public ServiceBusResolver(TaskLogger logger) {
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

        PowershellTask powershellTask = executeSingleCommandWithResponse(cmd);
        logger.addLogItem(powershellTask, "Resolving Connectionstring of " + sbns);

        return powershellTask.getLog().substring(0, powershellTask.getLog().indexOf("\n"));
    }
}

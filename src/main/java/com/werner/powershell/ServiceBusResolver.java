package com.werner.powershell;

import org.springframework.stereotype.Component;

@Component
public class ServiceBusResolver extends AbstractPowershellCaller {

    public String resolveServiceBusConnectionString(String rg, String sbns) {
        StringBuilder sb = new StringBuilder();
        sb.append("(Get-AzServiceBusKey ");
        sb.append("-Name RootManageSharedAccessKey ");
        sb.append("-NamespaceName %s ");
        sb.append("-ResourceGroupName %s )");
        sb.append(".'PrimaryConnectionString'");

        String cmd = String.format(sb.toString(), sbns, rg);


        return executeSingleCommandWithResponse(cmd);
    }
}

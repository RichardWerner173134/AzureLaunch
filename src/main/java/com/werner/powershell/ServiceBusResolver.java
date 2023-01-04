package com.werner.powershell;

import com.profesorfalken.jpowershell.PowerShellResponse;
import org.springframework.stereotype.Component;

@Component
public class ServiceBusResolver extends AbstractPowershellCaller {

    public String resolveServiceBusConnection(String rg, String sbns) {
        StringBuilder sb = new StringBuilder();
        sb.append("(Get-AzServiceBusKey ");
        sb.append("-Name RootManageSharedAccessKey ");
        sb.append("-NamespaceName %s ");
        sb.append("-ResourceGroupName %s )");
        sb.append(".\"PrimaryConnectionString\"; ");

        String cmd = String.format(sb.toString(), sbns, rg);


        PowerShellResponse powerShellResponse = executePowershellWithResponse(cmd);
        return powerShellResponse.getCommandOutput();
    }

    @Override
    public void handleResponse(PowerShellResponse powerShellResponse) throws Exception {
        // do nothing
    }
}

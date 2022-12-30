package com.werner.powershell;

import com.profesorfalken.jpowershell.PowerShellResponse;
import org.springframework.stereotype.Component;

@Component
public class ServiceBusResolver extends AbstractPowershellCaller {

    public String resolveServiceBusConnection() {
        StringBuilder sb = new StringBuilder();
        sb.append("(Get-AzServiceBusKey ");
        sb.append("-Name RootManageSharedAccessKey ");
        sb.append("-NamespaceName sbnsforsbtopforsubscription01 ");
        sb.append("-ResourceGroupName rg-testwerner002)");
        sb.append(".\"PrimaryConnectionString\"; ");

        PowerShellResponse powerShellResponse = executePowershellWithResponse(sb.toString());
        return powerShellResponse.getCommandOutput();
    }

    @Override
    public void handleResponse(PowerShellResponse powerShellResponse) throws Exception {
        // do nothing
    }
}

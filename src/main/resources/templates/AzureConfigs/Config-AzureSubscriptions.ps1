# Define general Azure Settings
$azure_subscription  = "d40c1b45-2512-4210-b7ae-84947aadf232"

$azure_default_resource_group_location = "eastus"

Write-Host "Switching to subscription '$azure_subscription'";
Select-AzSubscription -SubscriptionId $azure_subscription;
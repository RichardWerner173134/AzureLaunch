# .\create.ps1 -location germanywestcentral -rgName myFirstOwnRg
# Parameters definition
param(
    [Parameter(Mandatory=$True)]
    [string]
    $rgName
)

# Import configs and utils
. "$PSScriptRoot\..\AzureConfigs\Config-AzureSubscriptions.ps1"

$resourceGroup = $rgName

# Create new ResourceGroup to work with
New-AzResourceGroup -Name $resourceGroup -Location $azure_default_resource_group_location -Force
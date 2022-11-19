param(
    [Parameter(Mandatory=$True)]
    [string]
    $resourceGroup
)

# Import configs and utils
#. "$PSScriptRoot\..\AzureConfigs\Config-AzureSubscriptions.ps1"
#. "$PSScriptRoot\..\resourceGroup\create.ps1"

New-AzResourceGroupDeployment -ResourceGroupName $resourceGroup -TemplateFile .\serviceBusT.json -TemplateParameterFile .\serviceBusP.json
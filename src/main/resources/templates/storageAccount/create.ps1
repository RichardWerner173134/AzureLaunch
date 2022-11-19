# Import configs and utils
. "$PSScriptRoot\..\AzureConfigs\Config-AzureSubscriptions.ps1"
. "$PSScriptRoot\..\resourceGroup\create.ps1"

New-AzResourceGroupDeployment -ResourceGroupName $resourceGroup -TemplateFile .\storageAccountT.json -TemplateParameterFile .\storageAccountP.json
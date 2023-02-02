$scope = (\"/subscriptions/$((Get-AzSubscription).Id)\"); `
$oldSP = Get-AzADServicePrincipal -DisplayName PLACEHOLDER_SERVICE_PRINCIPAL_NAME; `
if($oldSP) { `
    Get-AzRoleAssignment -ObjectId $oldSP.Id -RoleDefinitionName Owner -Scope $scope | Remove-AzRoleAssignment; `
    Get-AzADApplication -DisplayName $oldSP.DisplayName | Remove-AzADApplication `
}; `
$newSP = New-AzADServicePrincipal -DisplayName PLACEHOLDER_SERVICE_PRINCIPAL_NAME; `
New-AzRoleAssignment -ObjectId $newSP.id -RoleDefinitionName Owner -Scope $scope; `
Write-Host Name AppId Tenant Secret: $newSP.DisplayName $newSP.appid (Get-AzTenant).Id $newSP.PasswordCredentials.SecretText
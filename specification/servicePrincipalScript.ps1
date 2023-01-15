$x = Get-AzAdServicePrincipal -SearchString testServicePrincipal; `
if($x) { `
    Remove-AzADServicePrincipal -ApplicationId $x.AppId `
}; `
$x = New-AzADServicePrincipal -DisplayName testServicePrincipal; `
New-AzRoleAssignment -ObjectId $x.id `
    -RoleDefinitionName Owner `
    -Scope /subscriptions/d40c1b45-2512-4210-b7ae-84947aadf232; `
Write-Host Please save following values in a file. You cannot access the secret after you exit this session.; `
Write-Host Name:    $x.DisplayName; `
Write-Host AppId:   $x.appid; `
Write-Host Secret:  $x.PasswordCredentials.SecretText; `
Write-Host Tenant:  (Get-AzTenant).Id

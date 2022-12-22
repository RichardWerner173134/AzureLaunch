# List of supported services

| Resourcename            | ResourceType                                         | Supported | Planned |
|-------------------------|------------------------------------------------------|---|---|
| Servicebus Namespace    | Microsoft.ServiceBus/namespaces                      |✅|✅|
| Servicebus Topic        | Microsoft.ServiceBus/namespaces/topics               |✅|✅|
| Servicebus Subscription | Microsoft.ServiceBus/namespaces/topics/Subscriptions |✅|✅|
| Servicebus Queue        |                                                      |❌|✅|
| Storage Account         | Microsoft.Storage/storageAccounts                    |❌|✅|
| Function App            | Microsoft.Web/sites                                  |❌|✅|
| Function                | Microsoft.Web/sites/functions                        |❌|✅|
| App Service Environment | Microsoft.Web/hostingEnvironments                    |❌|❌|
| Keyvault                | Microsoft.KeyVault/vaults                            |❌|✅|
| Keyvault Secret         | Microsoft.KeyVault/vaults/secrets                    |❌|✅|

## Abhängigkeiten
Folgende Ressourcen werden immer zusammen angelegt:

- sbns, sbtopic, sbsub
- funcapp, func, 
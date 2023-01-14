# List of supported services

| Resourcename            | ResourceType                                         | Supported | Planned |
|-------------------------|------------------------------------------------------|---|---|
| Servicebus Namespace    | Microsoft.ServiceBus/namespaces                      |✅|✅|
| Servicebus Topic        | Microsoft.ServiceBus/namespaces/topics               |✅|✅|
| Servicebus Subscription | Microsoft.ServiceBus/namespaces/topics/Subscriptions |✅|✅|
| Servicebus Queue        | Microsoft.ServiceBus/namespaces/queues               |✅|✅|
| Function App            | Microsoft.Web/sites                                  |✅|✅|
| Function                | Microsoft.Web/sites/functions                        |✅|✅|

## Resource Dependencies
Following resources are created together

- sbns, sbtopic, sbsub
- sbnd, sbqueue
- funcapp, func, storage account, appserviceplan
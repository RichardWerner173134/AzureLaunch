# Example Input 01
The provided example shows the input for generating following resources:

- Servicebus in PubSub
  - Microsoft.ServiceBus/namespaces
  - Microsoft.ServiceBus/namespaces/topics
  - Microsoft.ServiceBus/namespaces/topics/Subscriptions
- Azure Function HttpTrigger
  - Microsoft.Web/sites
  - Microsoft.Web/sites/functions

The Servicebus subscription can only exist in an Servicebus topic. A Servicebus topic can only exist in a Servicebus namespace. An Azure Function can only exist in an Azure Function App.
So these resources will be created in advance.

Additionally the input has to provide the kind of connection those two services have. The Azure Function writes data to the Servicebus subscription.
The subscription and resourceGroup are mandatory. If the resourceGroup doesnt exist it gets created on subscription level.
If the subscription doesnt exist the application cannot work intended.

The resources need to be provided as a directed graph. Every Node is a resource. Every Edge connects two nodes. 
The edges need to be directed to be able to generate code in an Azure Function.

```json
{
	"azAccount": {
		"subscription": "d40c1b45-2512-4210-b7ae-84947aadf232",
		"resourceGroup": "rgtest-rwerner"
	},
	"graph": {
		"nodes": [
			{
				"name": "subscription01",
				"type": "Microsoft.ServiceBus/namespaces/topics/Subscriptions"
			},
			{
				"name": "function01",
				"type": "Microsoft.Web/sites/functions"
			}
		],
		"edges": [
			{
				"node1": "function01",
				"node2": "subscription01",
				"type": "publish"
			}
		]
	}
}
```
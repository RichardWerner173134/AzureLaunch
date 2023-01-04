package com.werner.bl.codegeneration.model.enums;

import lombok.Getter;

@Getter
public enum TriggerParam {
	P_SERVICEBUS_NAMESPACE_NAME("SbNamespaceName"),
	P_SERVICEBUS_TOPIC_NAME("SbTopicName"),

	P_SERVICEBUS_SUBSCRIPTION_NAME("SbSubscriptionName"),
	P_SERVICEBUS_QUEUE_NAME("SbQueueName"),

	AS_CONNECTIONSTRING("ConnectionString")
	;

	private String value;

	TriggerParam(String value) {
		this.value = value;
	}
}

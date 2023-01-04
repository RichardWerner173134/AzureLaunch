    @FunctionName("PLACEHOLDER_FUNCTION_NAME")
    public void PLACEHOLDER_JAVA_FUNCTION_NAME(
        @ServiceBusTopicTrigger(
                    name = "message",
                    topicName = "PLACEHOLDER_TOPIC_NAME",
                    subscriptionName = "PLACEHOLDER_SUBSCRIPTION_NAME",
                    connection = "PLACEHOLDER_CONNECTIONSTRING"
        ) String message,
        final ExecutionContext context
    ) {
        context.getLogger().info(message);
    }
    @FunctionName("PLACEHOLDER_FUNCTION_NAME")
    public void PLACEHOLDER_JAVA_FUNCTION_NAME(
            @ServiceBusQueueTrigger(name = "msg",
                    queueName = "PLACEHOLDER_QUEUE_NAME",
                    connection = "PLACEHOLDER_CONNECTION_STRING") String message,
            final ExecutionContext context
    ) {
        context.getLogger().info(message);
    }
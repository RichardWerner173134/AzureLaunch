    @FunctionName("PLACEHOLDER_FUNCTION_NAME")
    public HttpResponseMessage PLACEHOLDER_JAVA_FUNCTION_NAME(
            @HttpTrigger(name = "req",
                    methods = {HttpMethod.POST},
                    authLevel = AuthorizationLevel.ANONYMOUS)
            HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {

        if (!request.getBody().isPresent()) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                    .body("No body provided")
                    .build();
        }

        return request.createResponseBuilder(HttpStatus.OK)
                        .header("Content-Type", "application/json")
                        .body("Message posted successfully")
                        .build();
    }
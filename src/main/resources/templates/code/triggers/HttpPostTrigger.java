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
        // return JSON from to the client
        // Generate document
        final String body = request.getBody().get();
        final String jsonDocument = "{\"id\":\"123456\", " + "\"description\": \"" + body + "\"}";
        return request.createResponseBuilder(HttpStatus.OK)
                        .header("Content-Type", "application/json")
                        .body(jsonDocument)
                        .build();
    }
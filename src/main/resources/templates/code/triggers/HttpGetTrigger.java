        @FunctionName("PLACEHOLDER_FUNCTION_NAME")
        public HttpResponseMessage PLACEHOLDER_JAVA_FUNCTION_NAME(
                @HttpTrigger(name = "req",
                        methods = {HttpMethod.GET},
                        authLevel = AuthorizationLevel.ANONYMOUS)
                HttpRequestMessage<Optional<String>> request,
                final ExecutionContext context) {
            return request.createResponseBuilder(HttpStatus.OK).body("Hello from generated function").build();
        }
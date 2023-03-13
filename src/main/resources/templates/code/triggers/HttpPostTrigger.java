    @FunctionName("PLACEHOLDER_FUNCTION_NAME")
    public HttpResponseMessage PLACEHOLDER_JAVA_FUNCTION_NAME(
            @HttpTrigger(name = "req",
                    methods = {HttpMethod.POST},
                    authLevel = AuthorizationLevel.ANONYMOUS)
            HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {

            String host = request.getUri().getHost() + ":" + request.getUri().getPort();

            context.getLogger().info(request.getHttpMethod().name() + ", " + host + ";\nbody: " + request.getBody().orElse("No body provided"));

            return request.createResponseBuilder(HttpStatus.OK)
                .header("Content-Type", "text/plain")
                .body("Message posted successfully to " + host)
                .build();
    }
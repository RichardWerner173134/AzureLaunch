        @FunctionName("PLACEHOLDER_FUNCTION_NAME")
        public HttpResponseMessage PLACEHOLDER_JAVA_FUNCTION_NAME(
                @HttpTrigger(name = "req",
                        methods = {HttpMethod.GET},
                        authLevel = AuthorizationLevel.ANONYMOUS)
                HttpRequestMessage<Optional<String>> request,
                final ExecutionContext context) {

                context.getLogger().info(request.getHttpMethod().name() + ", " + request.getUri().getHost() + ":" + request.getUri().getPort());

                return request
                        .createResponseBuilder(HttpStatus.OK)
                        .body("This message comes from " + request.getUri().getHost() + ":" + request.getUri().getPort())
                        .build();
        }
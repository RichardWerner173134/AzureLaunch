    @FunctionName("PLACEHOLDER_FUNCTION_NAME")
    public HttpResponseMessage PLACEHOLDER_JAVA_FUNCTION_NAME(
            @HttpTrigger(name = "req",
                    methods = {HttpMethod.GET},
                    authLevel = AuthorizationLevel.ANONYMOUS)
            HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {

        OkHttpClient client = new OkHttpClient();
        Request httpRequest = new Request.Builder()
                .url("PLACEHOLDER_URL")
                .build();

        Call call = client.newCall(httpRequest);

        try {
            Response response = call.execute();
            return request.createResponseBuilder(HttpStatus.OK).body("Response from called endpoint: " + response.body().string()).build();
        } catch (IOException e) {
            return request.createResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

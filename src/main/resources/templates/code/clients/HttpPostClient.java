    @FunctionName("PLACEHOLDER_FUNCTION_NAME")
    public HttpResponseMessage PLACEHOLDER_JAVA_FUNCTION_NAME(
            @HttpTrigger(name = "req",
                    methods = {HttpMethod.GET},
                    authLevel = AuthorizationLevel.ANONYMOUS)
            HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {

        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                "PLACEHOLDER_JSON");

        Request httpRequest = new Request.Builder()
                .url("PLACEHOLDER_URL")
                .post(body)
                .build();

        Call call = client.newCall(httpRequest);

        try {
            Response response = call.execute();
            return request.createResponseBuilder(HttpStatus.OK).body(response.body().string()).build();
        } catch (IOException e) {
            return request.createResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
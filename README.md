# OpenAPI Vert.x Sample

Sample application leveraging
[`vertx-web-openapi`](https://vertx-web-site.github.io/docs/vertx-web-openapi/java/) to validate
parameters passed to our Web HTTP endpoints.

## Getting Started

Clone the repository and start the web server:

```shell script
git clone https://github.com/mickaelpham/openapi-vertx-sample
cd openapi-vertx-sample
./gradlew run
```

Try a successful request, e.g.:

```shell script
curl --request GET \
  --url http://localhost:8080/accounts/1
```

It returns `HTTP 200 OK` with the `accountId` parameter displayed in the logs.

Then try a bad request, e.g.:

```shell script
curl --request GET \
  --url http://localhost:8080/accounts/foobar
```

It returns `HTTP 400 Bad Request` thanks to the validation in our [OpenAPI](http://spec.openapis.org/oas/v3.0.3) file:

```yaml
parameters:
  - name: accountId
    in: path
    description: Account ID to fetch
    required: true
    schema:
      type: integer
      minimum: 1
```

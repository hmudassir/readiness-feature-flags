# Readiness and feature flags

Two small rules for an API that can start before it is actually ready, and that can turn a feature off without removing the route.

**Readiness.** The process can accept HTTP before application startup tasks finish. `GET /actuator/health` stays `{"status":"DOWN"}` until a startup check reads the database and then marks the process ready. After that the same URL returns `{"status":"UP"}`. The body has no component details.

The startup check, the health response, and the order of `403`, `401`, and `503` are in [docs/architecture.md](docs/architecture.md).

**Disabled features.** Mail is off in this sample.

- `POST /api/newsletter/subscriptions` is anonymous. With a valid CSRF token it returns `503` and `"code":"FEATURE_DISABLED"`. Without the CSRF header it returns `403`.
- `POST /api/media/uploads` requires authentication. An anonymous caller receives `401`. A signed-in caller reaches the same guard and receives `503`.

## Run

Java 21.

```sh
./mvnw spring-boot:run
```

```sh
curl -sS http://127.0.0.1:8080/actuator/health
```

## Tests

```sh
./mvnw test
```

## What this is

A portfolio proof of concept. Turning mail on would mean changing `poc.mail.delivery` to `enabled` and adding a real sender. This sample does not send mail.

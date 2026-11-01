# Architecture

The process can accept HTTP before it is safe to call ready. A feature can stay on the route map and still be off. The two decisions are separate.

## Boundaries

```text
startup
  Flyway
  StartupCheck                 reads app_settings
  StartupReadiness.markReady   only after that read succeeds
        |
        v
GET /actuator/health
  StartupHealthIndicator       UP only after markReady
  body has status and no component details

request
  SecurityFilterChain          CSRF, then authentication
        |
        v
ApiController
        |
        v
FeatureFlags
  mail off  -> 503 FEATURE_DISABLED
  media off -> 503 FEATURE_DISABLED
```

Health does not become `UP` inside the web layer. The startup check marks ready only after it has read the expected row. A failed check stops startup and leaves the indicator `DOWN`.

## Request path

Anonymous newsletter signup is permitted. It still needs a CSRF token.

1. No CSRF header: `403`. The feature flag is not consulted.
2. CSRF present, no session: the newsletter route is allowed through. Mail is disabled, so the response is `503` with `"code":"FEATURE_DISABLED"`.
3. Media upload is authenticated. No session: `401`. A signed-in caller reaches the same flag and receives `503`.

The order is the point. Authentication and the feature flag answer different questions. A disabled feature is not reported as "sign in" and a missing session is not reported as "disabled".

## Failure

| Condition | Result |
| --- | --- |
| Startup check has not finished | `GET /actuator/health` is `503` and `{"status":"DOWN"}` |
| Startup check has finished | `200` and `{"status":"UP"}` |
| Newsletter without CSRF | `403` |
| Newsletter with CSRF while mail is off | `503` |
| Media upload with no session | `401` |
| Signed-in media upload while storage is off | `503` |

## What this sample leaves out

The flags are configuration values, `disabled` in this sample. Turning one on would mean changing that value and supplying a real sender or a real storage adapter. Health still waits for the startup check either way.

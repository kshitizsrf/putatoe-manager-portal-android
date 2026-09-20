# Putatoe Manager Portal (Android)

A native Android app for Putatoe area managers to log in, review and manage customer orders by status, and update their profile. It also contains a `MainActivity` "homepage" screen that lists product banners and a Fruits & Vegetables / Grocery catalog, presumably shared from a consumer-facing Putatoe app.

- **Package:** `com.practice.android.putatoe`
- **Min SDK:** 24 · **Target/Compile SDK:** 34
- **Language:** Java
- **Build system:** Gradle (Kotlin DSL), AGP 8.2.1

## Screens

| Activity | Purpose |
|---|---|
| `AdminPortalLoginActivity` (launcher) | Manager login (user ID + password) against the manager API; stores the returned auth token statically for later requests. |
| `OrdersManager` | Filters orders by date range and by status (Pending, Alloted to Service Provider, Ongoing, Completed, Cancelled, Final Cancelled) in a `RecyclerView` of order cards. |
| `MyProfile` | Displays and edits the manager's profile (name, state, district, user ID, username). |
| `MainActivity` | Home/catalog screen: image slider banners plus horizontally-scrolling product lists for "Fruits & Vegetables" and "Grocery". |

## Architecture

- Plain Android `Activity`-based UI, no navigation component, no ViewModel/MVVM — each activity owns its networking, parsing, and view logic directly.
- Networking via **OkHttp** (`OkHttpClient` + manual `Request`/`Response`), with responses parsed by hand using `org.json`.
- All network calls run on ad-hoc `new Thread(...)` instances and hop back to the UI thread with `runOnUiThread`, rather than a single async/executor pattern.
- Images loaded with **Picasso** (product images) and **Glide** (declared dependency) plus `ImageSlideshow` (denzcoskun) for the homepage banner carousel.
- Data models (`OrdersModel`, `ProductsModel`) are simple POJOs; `RecyclerOrdersAdapter` / `RecyclerProductsAdaptor` bind them to `cardview_orders.xml` / `vegetables_row.xml` row layouts.
- The manager auth token from login (`AdminPortalLoginActivity.AdminToken`) is read as a static field by `OrdersManager` and `MyProfile` to authenticate subsequent API calls — there is no dedicated session/auth manager.

## Key files

```
app/src/main/java/com/practice/android/putatoe/
├── AdminPortalLoginActivity.java   # Login screen + POST manager_login
├── OrdersManager.java              # Order list/filter screen + POST orders_manager_new
├── MyProfile.java                  # Profile view/edit screen + GET/POST manager_profile
├── MainActivity.java               # Homepage banners + product catalog
├── OrdersModel.java                # Order row data model
├── ProductsModel.java              # Product row data model
├── RecyclerOrdersAdapter.java      # RecyclerView adapter for order cards
└── RecyclerProductsAdaptor.java    # RecyclerView adapter for product cards
```

## Backend

The app talks to a Cloud Run–hosted REST API (`*.run.app`), split across `prod` and `test` hosts depending on endpoint (see `app/src/main/res/values/strings.xml`):

| Endpoint | Method | Used by |
|---|---|---|
| `manager_login` | POST | Login |
| `manager_profile` | GET | Fetch profile |
| `UpdateProfile_Manager` | POST | Save profile changes |
| `orders_manager_new` | POST | Fetch orders by status + date range |
| `getbannertrue` | POST | Homepage banners |
| `homepage_prodcuct_new_api/6` | GET | Homepage product catalog |

Auth is via an `Authtoken` / `authtoken` request header — either a static token embedded in `strings.xml`/source, or the token returned from `manager_login`, depending on the screen.

## Building & running

```bash
./gradlew assembleDebug
```

Open the project root in Android Studio (Hedgehog+ recommended for AGP 8.2.1 / SDK 34) and run the `app` configuration on a device or emulator with API 24+.

No `local.properties`/secrets file is required — all API URLs and tokens currently ship hardcoded in resources/source (see Known issues).

## Known issues / things to fix before shipping

- **Hardcoded secrets in source control:** `strings.xml` contains a plaintext manager login (`managerUserId`, `managerPasswd`) and several long-lived API auth tokens are hardcoded directly in `MainActivity.java` and `OrdersManager.java`. Since this is a public repository, these should be rotated and moved to a non-committed config (e.g. `local.properties`, a secrets manager, or fetched per-session from the backend) rather than shipped in the APK.
- Mixed use of `prod` vs `test` backend hosts across endpoints (e.g. orders/profile-update hit `putatoetest...` while login/profile-fetch hit `putatoeprod...`) looks unintentional and worth confirming.
- All four activities are `android:exported="true"`, including screens that assume a prior login (`OrdersManager`, `MyProfile`) — they can be launched directly by other apps, bypassing the login flow.
- No dependency injection, repository/service layer, or error surfacing to the user on request failures (exceptions are only logged via `printStackTrace()`).
- Only the default generated `ExampleUnitTest` / `ExampleInstrumentedTest` are present — no real test coverage.

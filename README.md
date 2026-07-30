# FinTrack Android

FinTrack is an Android expense and budget tracking app built with Kotlin and Jetpack Compose.  
It helps users track daily transactions, savings goals, and loans/debts with a dashboard-oriented UI.

## Features

- Dashboard with budget and balance insights
- Transaction ledger (income + expense tracking)
- Savings goals with progress tracking
- Loans & debts tracking
- User settings for theme, profile preset, currency, and budget preferences
- Local persistence using Room

## Tech Stack

- Kotlin
- Jetpack Compose (Material 3)
- Android Navigation Compose
- Room Database
- Coroutines + Flow
- Retrofit + OkHttp
- Firebase BOM (with optional AI/Auth/Firestore integrations)

## Prerequisites

- Android Studio (latest stable recommended)
- JDK 11
- Android SDK (compile/target SDK 36)

## Setup

1. Open the project in Android Studio.
2. (Optional) Create a `.env` file from `.env.example` and set values like `GEMINI_API_KEY` if needed.
3. Ensure `google-services.json` is available in `app/` if Firebase services are required.

## Build & Run

From the repository root:

```bash
./gradlew assembleDebug
./gradlew installDebug
```

Run unit tests:

```bash
./gradlew testDebugUnitTest
```

Run lint:

```bash
./gradlew lint
```

## Project Structure

- `app/src/main/java/com/example/ui/` - Compose UI screens, components, theme, and view models
- `app/src/main/java/com/example/data/` - Room entities, DAO interfaces, database, repository
- `app/src/main/res/` - Android resources

## Notes

- Release signing expects environment variables (`KEYSTORE_PATH`, `STORE_PASSWORD`, `KEY_PASSWORD`) or defaults to local paths defined in Gradle.

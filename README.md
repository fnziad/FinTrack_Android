# 📱 TakaKoi — Personal Finance & Budget Tracker (Android & iOS)

[![Build Android APK](https://github.com/fnziad/FinTrack_Android/actions/workflows/android.yml/badge.svg)](https://github.com/fnziad/FinTrack_Android/actions/workflows/android.yml)
[![iOS Framework Build](https://github.com/fnziad/FinTrack_Android/actions/workflows/ios.yml/badge.svg)](https://github.com/fnziad/FinTrack_Android/actions/workflows/ios.yml)

**TakaKoi** is a modern, high-performance cross-platform application built for intuitive expense tracking, budget management, and personal financial analytics on both **Android** and **iOS**. Built with **Kotlin Multiplatform (KMP)** and **Compose Multiplatform**, it features a premium editorial aesthetic with Material Design 3, providing glanceable insights into spending habits, daily spend pace, savings goals, and debt management.

---

## 📸 Screenshots

| Dashboard | Ledger | Savings |
|:-:|:-:|:-:|
| ![Dashboard](docs/screenshots/dashboard.png) | ![Ledger](docs/screenshots/ledger.png) | ![Savings](docs/screenshots/savings.png) |

| Loans & Debts | Settings |
|:-:|:-:|
| ![Loans](docs/screenshots/loans.png) | ![Settings](docs/screenshots/settings.png) |

---

## ✨ Key Features

- 📊 **Smart Dashboard**:
  - **Payday Hero Card**: Real-time countdown to your next salary date paired with remaining cash.
  - **Spend Pace Tracker**: Live analysis of your current vs. target daily spending pace.
  - **Cost Driver Analytics**: Identifies your highest spending category and single largest expense.
  - **Category Breakdown**: Interactive visual analytics with doughnut and bar charts.
- 📜 **Ledger**: Track income and expense transactions with instant search, category filtering, and date-sorted transaction history.
- 🎯 **Savings Goals**: Set custom savings wallets, track progress bars, and view accumulated vs. target amounts.
- 🤝 **Loans & Debt Management**: Keep tabs on money owed to you and debt repayments with settlement tracking.
- 🎨 **Custom Theme Presets**: Switch between light and dark modes with curated color palettes (Indigo, Emerald, Ocean, Teal, Rose).
- 🔤 **Space Grotesk Typography**: Geometric, tech-forward editorial typography system.
- 🍏📱 **Cross-Platform Core**: 100% shared business logic and Compose UI across Android and iOS.

---

## 🛠️ Tech Stack & Architecture

TakaKoi is structured as a **Kotlin Multiplatform (KMP)** project with shared UI and data layers across platforms:

```
TakaKoi/
├── shared/            # KMP shared module (UI, Logic, Data, Ktor, Room)
│   ├── commonMain/    # Compose Multiplatform UI, ViewModel, DAOs, Entities
│   ├── androidMain/   # Android platform-specific implementations (Room SQLite driver)
│   └── iosMain/       # iOS framework export & MainViewController wrapper
├── app/               # Android application host module (Jetpack Compose entry point)
└── iosApp/            # iOS application host module (SwiftUI wrapper for KMP view controller)
```

### Stack Breakdown

- **Languages**: Kotlin 2.1+, Swift 5.10+
- **UI Framework**: [Compose Multiplatform](https://www.jetbrains.com/lp/compose-multiplatform/) with Material Design 3 components.
- **Shared Logic & Architecture**: MVVM (Model-View-ViewModel) powered by StateFlow and Kotlin Coroutines.
- **Database**: [Room KMP 2.7+](https://developer.android.com/kotlin/multiplatform/room) with SQLite Bundled driver for offline storage on both Android and iOS.
- **Networking**: [Ktor 3.0+](https://ktor.io/) HTTP client (OkHttp engine for Android, Darwin engine for iOS).
- **Date & Time**: `kotlinx-datetime` for cross-platform date manipulation.
- **Build System**: Gradle 9.3.1 with Android Gradle Plugin (AGP) 9.1.1, Java 21 LTS, and Xcode 15+.

---

## 🚀 Getting Started

### Prerequisites

- **JDK**: OpenJDK 21 installed.
- **Android SDK**: API Level 36 target, API Level 24 minimum.
- **Xcode**: Xcode 15.0+ (required for iOS compilation and simulator execution).

### Environment Setup

Set your `JAVA_HOME` to JDK 21 before building:

```bash
export JAVA_HOME="$(brew --prefix openjdk@21)/libexec/openjdk.jdk/Contents/Home"
```

---

### Build & Run Android App

1. **Clone the repository**:
   ```bash
   git clone https://github.com/fnziad/TakaKoi.git
   cd TakaKoi
   ```

2. **Build Debug APK**:
   ```bash
   ./gradlew assembleDebug
   ```

3. **Install & Run on Emulator / Device**:
   ```bash
   ./gradlew :app:installDebug
   ```
   *Or launch using `android-cli`:*
   ```bash
   android run --apks=app/build/outputs/apk/debug/app-debug.apk --device=emulator-5554
   ```

---

### Build & Run iOS App

1. **Link Shared Framework**:
   ```bash
   ./gradlew :shared:linkDebugFrameworkIosSimulatorArm64
   ```

2. **Open iOS Project**:
   Open `iosApp/iosApp.xcodeproj` in **Xcode**.

3. **Run on Simulator**:
   Select an iOS Simulator target (e.g. iPhone 15 Pro) and press `Cmd + R` to run.

---

## ⚙️ CI/CD Pipelines

Automated **GitHub Actions** workflows enforce continuous integration for both platforms:

- **Android Build** (`.github/workflows/android.yml`):
  - Environment: `ubuntu-latest` with JDK 21 (Temurin) and Gradle wrapper validation.
  - Auto-generates debug keystore if missing.
  - Compiles the Debug APK (`./gradlew assembleDebug`) and uploads the `app-debug.apk` artifact.

- **iOS Build** (`.github/workflows/ios.yml`):
  - Environment: `macos-latest` with JDK 21 (Temurin), `JAVA_HOME` explicitly pinned to prevent system JDK 26 override.
  - Compiles KMP release frameworks for iOS Simulator (`iosSimulatorArm64`) and iOS Device (`iosArm64`).
  - Uploads two separate artifacts: `shared-ios-framework-simulator` and `shared-ios-framework-device`.

---

## 🗺️ Roadmap

- [x] **iOS Support** — Kotlin Multiplatform (KMP) + Compose Multiplatform migration.
- [ ] Push notifications for payday reminders.
- [ ] Cloud sync and backup via Firebase.
- [ ] Recurring transaction automation.
- [ ] Export transactions to CSV / PDF reports.

---

## 🌿 Branch Guide

| Branch | Purpose | Status |
|---|---|---|
| `main` | Primary integration branch — Android + iOS KMP | ✅ Active |
| `feature/ios-kmp` | Kotlin Multiplatform & Compose Multiplatform feature development | 🔀 Merged into `main` |
| `stable-build` | Stable snapshot — mirrors `main` at last verified release | 📌 Pinned |

### Branch Workflow

```
feature/ios-kmp  →  main  →  (tag: v1.0.0-kmp)
                      ↓
                 stable-build (stable snapshot)
```

- Feature work happens on `feature/*` branches.
- `main` is the integration target — CI must pass before merge.
- `stable-build` is reset to `main` after each verified release.

---

## 📄 License

This project is licensed under the MIT License — see the repository files for details.

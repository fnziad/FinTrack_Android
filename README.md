# 📱 TakaKoi — Personal Finance & Budget Tracker

**TakaKoi** is a modern, high-performance Android application built for intuitive expense tracking, budget management, and personal financial analytics. Designed with a premium editorial aesthetic using Jetpack Compose and Material 3, it offers glanceable insights into spending habits, savings targets, and debt management.

---

## ✨ Features

- 📊 **Smart Dashboard**:
  - **Payday Hero Card**: Real-time countdown to your next salary date paired with remaining cash.
  - **Spend Pace Tracker**: Live analysis of your current vs. target daily spending pace.
  - **Cost Driver Analytics**: Identifies your highest spending category and single largest expense.
  - **Category Breakdown**: Interactive visual analytics of sub-category expenditures.
- 📜 **Ledger**: Track income and expense transactions with quick category filtering and transaction history.
- 🎯 **Savings Goals**: Set custom monthly savings goals, track progress bars, and view projected end-of-month savings.
- 🤝 **Loans & Debt Management**: Keep tabs on money owed to you and debt repayments with settlement tracking.
- 🎨 **Custom Theme Presets**: Switch between light and dark modes with curated color palettes (Indigo, Emerald, Ocean, Teal, Rose).
- 🔤 **Space Grotesk Typography**: Geometric, tech-forward editorial typography system.

---

## 🛠️ Tech Stack & Architecture

- **UI Framework**: [Jetpack Compose](https://developer.android.com/jetpack/compose) with Material Design 3 components.
- **Design System**: Space Grotesk custom font family, warm off-white surface palette (`#F5F4F2`), deep ink hero cards (`#0A0A0F`).
- **Database**: [Room Database](https://developer.android.com/training/data-storage/room) for persistent local offline storage.
- **Architecture Pattern**: MVVM (Model-View-ViewModel) powered by StateFlow and Kotlin Coroutines.
- **Build System**: Gradle 9.3.1 with Android Gradle Plugin (AGP) 9.1.1 and Java 21 LTS.

---

## 🚀 Getting Started

### Prerequisites

- **JDK**: OpenJDK 21 installed.
- **Android SDK**: API Level 36 target, API Level 24 minimum.

### Environment Setup

Set your `JAVA_HOME` to JDK 21 before building:

```bash
export JAVA_HOME="$(brew --prefix openjdk@21)/libexec/openjdk.jdk/Contents/Home"
```

### Build & Run Locally

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
   android run --apks=app/build/outputs/apk/debug/app-debug.apk --device=emulator-5554
   ```

---

## ⚙️ CI/CD Pipeline

The project includes an automated **GitHub Actions** workflow (`.github/workflows/android.yml`) that:
- Configures JDK 21 (Temurin distribution) and Gradle wrapper.
- Auto-generates debug keystores if missing.
- Builds the Debug APK and uploads the artifact for every push or PR to `main`.

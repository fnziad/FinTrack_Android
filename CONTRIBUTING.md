# Contributing to TakaKoi 📱

Thank you for your interest in contributing to **TakaKoi**! We welcome contributions to help improve the personal finance and budget tracking experience across Android and iOS.

---

## 🌿 Branching Strategy & Workflow

TakaKoi uses a structured Git workflow to maintain stability while allowing active feature development:

```
feature/my-feature ──┐
                     ├──> develop (Preview / Active Work) ──> main (Stable / Tagged Release)
fix/bug-fix ─────────┘
```

### Branch Roles

- **`main`**: The production-ready branch. Code here is always stable, fully tested, and tagged with version numbers (e.g. `v1.0.0`). Direct pushes to `main` are restricted; code enters `main` only via pull requests from `develop`.
- **`develop`**: The active integration branch. Nightly preview APKs and frameworks are built from here. Developers branch off `develop` and merge back into `develop`.
- **`feature/<name>`**: Short-lived feature branches created from `develop` for specific features (e.g. `feature/recurring-payments`).
- **`fix/<name>`**: Short-lived bug fix branches created from `develop` for resolving specific issues (e.g. `fix/payday-calculator`).

---

## 🚀 Local Development Setup

### 1. Prerequisites

- **JDK 21**: Install OpenJDK 21 (e.g. `brew install openjdk@21`).
- **Android SDK**: API 36 target, API 24 minimum.
- **Xcode 15+**: Required for building and running the iOS app container.

### 2. Environment Configuration

Ensure your `JAVA_HOME` environment variable points to JDK 21:

```bash
export JAVA_HOME="$(brew --prefix openjdk@21)/libexec/openjdk.jdk/Contents/Home"
```

> ⚠️ **Important**: macOS system defaults (Java 26+) break the Android SDK `jlink` module processing. Always ensure JDK 21 is set in your shell before running Gradle commands.

---

## 🛠️ Building & Testing Locally

Before opening a pull request, ensure both Android and iOS targets compile without errors.

### Android Debug Build

```bash
JAVA_HOME="$(brew --prefix openjdk@21)/libexec/openjdk.jdk/Contents/Home" \
  ./gradlew assembleDebug --no-configuration-cache
```

### iOS Framework Build

```bash
JAVA_HOME="$(brew --prefix openjdk@21)/libexec/openjdk.jdk/Contents/Home" \
  ./gradlew :shared:linkDebugFrameworkIosSimulatorArm64 --no-configuration-cache
```

---

## 📐 Code Style & Conventions

- **Kotlin**: Follow the official [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html).
- **Compose Multiplatform**:
  - Keep UI components decoupled and stateful logic inside `ExpenseViewModel`.
  - Use Material Design 3 tokens and Space Grotesk typography defined in `shared/src/commonMain/kotlin/com/example/shared/ui/theme/`.
  - Prefer auto-mirrored icons for directional icons (`Icons.AutoMirrored.Filled.*`).
- **Swift / iOS**: Follow standard Swift conventions in `iosApp/`.

---

## 📥 Pull Request (PR) Guidelines

1. **Create a branch** off `develop`:
   ```bash
   git checkout develop
   git pull origin develop
   git checkout -b feature/my-new-feature
   ```
2. **Commit your changes** with clear, semantic commit messages (e.g., `feat: add PDF report generator`, `fix: correct currency formatting in Ledger`).
3. **Run local verification** (`./gradlew assembleDebug` and `:shared:linkDebugFrameworkIosSimulatorArm64`).
4. **Push to GitHub** and open a PR against the `develop` branch.
5. Ensure all **GitHub Actions CI checks** (`Build Android APK` and `iOS Framework Build`) pass cleanly.

---

## 📄 License

By contributing to TakaKoi, you agree that your contributions will be licensed under the project's [MIT License](LICENSE).

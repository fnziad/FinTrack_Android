# Agent Instructions and Constraints

## Build and CI Configuration Boundaries

The CI/CD pipeline and Gradle setup are highly sensitive and currently passing. We recently had to fix the build pipeline due to several mismatched versions. DO NOT revert or change the following configurations under any circumstances:

1. **Gradle Wrapper (`gradlew`, `gradle/wrapper/*`)**: 
   - The wrapper is upgraded to Gradle 9.3.1 because AGP 9.1.1 requires a minimum of Gradle 9.3.1. 
   - We are using an official wrapper JAR extracted from the Gradle 9.3.1 distribution. 
   - DO NOT replace `gradle-wrapper.jar` or modify `gradle-wrapper.properties`. 
   - The `gradlew` script has execute permissions (`chmod +x`), do not alter its permissions.

2. **GitHub Actions Workflow (`.github/workflows/android.yml`)**:
   - The build uses `./gradlew assembleDebug` (not the bare `gradle` command).
   - The JDK version is explicitly set to `21` because AGP 9.1.1 requires JDK 21+.
   - `validate-wrappers: false` is set in the `setup-gradle` action.
   - DO NOT modify this workflow file.

3. **Dependencies (`build.gradle.kts` / `libs.versions.toml`)**:
   - DO NOT downgrade or upgrade AGP, Gradle, or Kotlin versions.
   - Only modify these files if you are explicitly adding a new library dependency that is strictly required for the feature you are building.

Your job is exclusively to work on the app's features and code (e.g., `app/src/main/...`) without breaking the build environment.

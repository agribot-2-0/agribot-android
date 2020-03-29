# Agribot Android Application

## Defined gradle wrapper commands
* `./gradlew clean` - Removes build directory with all contents
* `./gradlew dependencyUpdates` - Check all dependencies and lists all possible version updates
* `./gradlew ktlint` - Checks Kotlin files formatting and lists found KTLint style issues
* `./gradlew ktlintFormat` - Reformats code to match KTLint style requirements
* `./gradlew spotlessCheck` - Performs static code analysis with Spotless
* `./gradlew spotlessApply` - Fixes code issues found with Spotless
* `./gradlew publishReleaseBundle` - Builds and pushes Android AAB to `internal` track on Google Play

## Core libraries
* [Flipper](https://github.com/facebook/flipper) - Advanced debugging tool for mobile applications
* [Koin](https://github.com/InsertKoinIO/koin) - Dependency injection framework for Kotlin & Android
* [MaterialDialogs](https://github.com/afollestad/material-dialogs) - Easy display and management of dialog windows
* [Timber](https://github.com/JakeWharton/timber) - Logging tool, built on top of Android Logger

## Jetpack Components
* [Android Lifecycles](https://developer.android.com/topic/libraries/architecture/lifecycle)
* [Data Binding Library](https://developer.android.com/topic/libraries/data-binding)
* [LiveData](https://developer.android.com/topic/libraries/architecture/livedata)
* [Navigation Component](https://developer.android.com/guide/navigation)
* [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)

### Architecture 
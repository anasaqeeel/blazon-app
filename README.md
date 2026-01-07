# Blazon Android App

A premium men's salon mobile application built with Kotlin and Jetpack Compose.

## Features

- **Premium Dark Theme**: Black background with gold accents for a luxury feel
- **MVVM Architecture**: Clean separation of concerns
- **Navigation Compose**: Modern navigation with bottom navigation bar
- **Material 3**: Latest Material Design components

## Screens

1. **SplashScreen**: App launch screen
2. **BranchSelectionScreen**: Select salon branch
3. **HomeScreen**: Main dashboard with quick actions
4. **ServicesScreen**: Browse services by category
5. **MembershipScreen**: Create membership with selected services
6. **VisitTrackingScreen**: QR code and Wi-Fi verification (UI only)
7. **ProfileScreen**: User profile and settings

## Tech Stack

- Kotlin
- Jetpack Compose
- Material 3
- Navigation Compose
- MVVM Architecture
- Coroutines & Flow

## Setup Instructions

1. Open the project in Android Studio (Hedgehog or later)
2. Sync Gradle files
3. Ensure you have:
   - Android SDK 34
   - JDK 17
   - Kotlin 1.9.20+
4. Run the app on an emulator or physical device (API 24+)

## Project Structure

```
app/src/main/java/com/blazon/app/
├── data/
│   ├── model/          # Data models
│   └── repository/     # Mock data repository
├── navigation/         # Navigation setup
├── theme/              # Theme configuration
├── ui/
│   ├── components/     # Reusable UI components
│   └── screens/        # Screen composables
├── viewmodel/          # ViewModels
└── MainActivity.kt     # Main entry point
```

## Notes

- All data is currently mocked (no backend integration)
- QR scanning is UI-only (no actual camera integration)
- Wi-Fi connection is simulated
- Branch selection is stored in memory (not persisted)

## Building

```bash
./gradlew assembleDebug
```

## Running

```bash
./gradlew installDebug
```

Or use Android Studio's Run button.


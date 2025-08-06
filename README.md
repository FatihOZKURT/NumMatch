# 📱 NumMatch

NumMatch is an Android number matching game built with Jetpack Compose, where players try to match all the hidden number pairs on a grid before time runs out. It's a fun and brain-challenging game designed for all ages.

🎮 Gameplay
- Players enter their name and select a difficulty level.
- Each difficulty(Easy and Hard) determines the number of cards(16 or 24).
- Flip two cards to reveal numbers.
- If the numbers match, they remain open; otherwise, they flip back.
- The game ends when all matches are found or time runs out.
- Scores are calculated based on performance and stored locally.

🌟 Features
- 🧠 Memory-based number matching
- 🌗 Dark mode support (toggleable)
- ⏱️ Timer visibility toggle in settings
- 📊 Score saving system using Room Database
- ⚙️ Settings screen with options to clear scores
- 🔠 Multilingual support (English & Turkish)
- 🧭 Smooth navigation using Navigation Component
- 📦 MVVM architecture with Hilt for dependency injection

🔧 Tech Stack
- Language: **Kotlin**
- UI: **Jetpack Compose**
- Architecture: **MVVM**
- Dependency Injection: **Hilt**
- Navigation: **Navigation-Compose**)
- Local Storage: **Room**
- Persistence: **DataStore (for theme and timer preferences)**
- Localization: **string resources with English & Turkish support**

## 📷 Screenshots

<div align="center">
  <img src="https://github.com/FatihOZKURT/NumMatch/blob/master/screenshots/GameScreen.png" width="250" />
  &nbsp; &nbsp; 
  <img src="https://github.com/FatihOZKURT/NumMatch/blob/master/screenshots/ScoreScreen.png" width="250" />
  &nbsp; &nbsp; 
  <img src="https://github.com/FatihOZKURT/NumMatch/blob/master/screenshots/SettingsScreen.png" width="250" />
</div>

## 📂 Package Structure
 ```bash
com.example.nummatch
│
├── datasource        # Data sources
├── di                # Dependency injection
├── model             # Data models
├── presentation                
│   ├── navigation    # Navigation setup
│   ├── screen        # UI screens
│   └── theme         # App theme and styles
├── repo              # Repository implementations
├── room              # Room database
└── util              # Utility classes
```


## ⚙️ Setup

1. Clone the repository:
 ```bash
   git clone https://github.com/FatihOZKURT/NumMatch.git
   cd NumMatch
 ```
2. Open the project in Android Studio Hedgehog or newer.
3. Make sure you have the following installed:
  - Kotlin 1.9+
  - Compose Compiler compatible with BOM
  - Android SDK 24–36
  - Java 17
4. Build and run the project:
  - Connect an emulator or device.
  - Click Run or use:
```bash
   ./gradlew installDebug
```

# SCR Case Book — Android 16 (API 36, Baklava) App

An offline-first, tablet-optimized medical and homoeopathic case-taking application built specifically for **Android 16 (API Level 36 / Codename "Baklava")**.

This app digitizes the **Standardised Case Record (SCR)** system with full digital pen/stylus handwriting support on lined paper, 26 clinical sections, structured follow-up logs, and interoperable JSON backups.

---

## 🌟 Dual-Engine Architecture

This project provides **both** modern Android architectures in a unified project, allowing you to run or toggle between them at any time:

### 1. Pure Jetpack Compose Native Engine
* **100% Native Kotlin & Jetpack Compose**: Built with Material 3, modern state management, and edge-to-edge layout.
* **Low-Latency Stylus Canvas**: Custom Compose `pointerInput` engine with `PointerType.Stylus` palm rejection (rest your palm directly on the tablet screen without leaving stray marks).
* **Paper Note Simulation**: Ruled paper notebook lines, custom 5-column grid for Section 3 (*No.*, *Location*, *Sensation & Pathology*, *Modalities*, *Accompaniments*), pen color palette (black, blue, red), pen widths (thin, medium, thick), eraser, undo, and multi-page expansion per section.
* **Room SQLite Persistence**: Offline-first, reactive database architecture (`PatientEntity`, `FollowUpEntity`, `SectionPageEntity`) with Kotlin Flow.

### 2. High-Performance Android 16 WebView Container
* **Bi-directional Native Bridge (`AndroidWebBridge`)**: Injects native Android 16 capabilities into the high-fidelity web canvas engine.
* **Storage Access Framework (SAF)**: Directly triggers native Android 16 `CreateDocument` and `OpenDocument` pickers for backup JSON files.
* **Haptics**: Hardware vibration feedback for stylus and UI interactions.
* **Seamless Mode Switcher**: Quick one-tap switch button between the Native Compose interface and the Canvas Web container.

---

## 🚀 Android 16 (API 36 / Baklava) Features & Compliance

1. **Target SDK 36**:
   - `compileSdk = 36`
   - `targetSdk = 36`
   - `minSdk = 26` (Android 8.0 to Android 16)
2. **Enforced Edge-to-Edge**:
   - Uses `enableEdgeToEdge()` and window insets padding (`systemBarsPadding()`), conforming to Android 15/16 mandatory edge-to-edge system bar standards.
3. **Predictive Back Navigation**:
   - Configured with `android:enableOnBackInvokedCallback="true"` and Compose `BackHandler` for smooth, gesture-driven back transitions.
4. **Scoped Storage / SAF**:
   - Zero legacy storage permissions. Utilizes `ActivityResultContracts.CreateDocument` and `OpenDocument` for safe document import/export.
5. **Adaptive & Themed App Icons**:
   - Includes standard adaptive icons, circular icons, and `ic_launcher_monochrome` for Android 13–16 Material You dynamic theming.

---

## 📋 The 26 Standard Clinical Sections

1. **Case particulars** (S. No, Physician, Observer, Reg No, Date, Diagnosis, Age, Sex, Education, Occupation, Status, Religion, Diet, Family details, Address, Tel)
2. **Case summary**
3. **A.I Chief complaint(s)** (Tabular: *No., Location, Sensation & pathology, Modalities, Accompaniments*)
4. **A.II Associated complaints (concomitants)**
5. **III.A.1 Physical characteristics** (Appearance, build, gait, deformities, skin, hair, nails, eyes, teeth, thermal reactions)
6. **III.A.2 Digestion** (Acidity, appetite, cravings, aversions, thirst)
7. **III.A.3 Eliminations** (Stool, urine, discharges)
8. **III.A.4 Menstrual function** (FMP, LMP, menses, flow, leucorrhoea)
9. **III.A.5 Sexual function**
10. **III.A.6 Patient’s / mother’s obstetric history**
11. **III.A.7 Developmental landmarks & problems**
12. **III.A.8 Diet & daily routine**
13. **III.B Mental state** (Emotional, intellectual, activity/inactivity, nature & cause)
14. **Life-space investigation** (Society, family, interpersonal relations)
15. **III.C Reactions – physical factors** (Time, temperature, motion, meteorological)
16. **Dreams: analysis**
17. **III.D Fever totality** (Onset, chill, heat, sweat, apyrexia)
18. **III.E Original unmodified picture**
19. **III.F Chronological sequence**
20. **Past history (personal / family) & check-list**
21. **Provisional clinical & differential diagnosis**
22. **Investigations** (Reports & dates)
23. **Miasmatic expression**
24. **Repertorial technique & totality**
25. **Remedy-correspondences & selection**
26. **Planning & programming of Rx**

Plus structured **Follow-up log** with date, remedy/potency (Rx) tracking, and multi-page ink notes.

---

## 📂 Project Structure

```
SCR_Case_Book_Android/
├── app/
│   ├── build.gradle.kts                # compileSdk 36, targetSdk 36, Compose & Room
│   ├── proguard-rules.pro
│   └── src/main/
│       ├── AndroidManifest.xml         # Android 16 permissions, FileProvider, Baklava flags
│       ├── assets/www/                 # Enhanced web canvas engine & bridge
│       │   ├── index.html
│       │   ├── manifest.webmanifest
│       │   ├── sw.js
│       │   ├── icon-192.png
│       │   └── icon-512.png
│       ├── java/com/scrcasebook/app/
│       │   ├── MainActivity.kt         # Edge-to-Edge, dual-mode switcher, SAF launchers
│       │   ├── ScrApplication.kt       # Application class & Room DB singleton
│       │   ├── bridge/
│       │   │   └── AndroidWebBridge.kt # JavascriptInterface bridge
│       │   ├── data/
│       │   │   ├── db/
│       │   │   │   ├── Entities.kt     # Room entities (Patient, FollowUp, SectionPage)
│       │   │   │   ├── PatientDao.kt
│       │   │   │   ├── SectionPageDao.kt
│       │   │   │   └── ScrDatabase.kt
│       │   │   └── model/
│       │   │       └── ScrModels.kt    # SCR clinical section definitions & Stroke models
│       │   ├── ui/
│       │   │   ├── components/
│       │   │   │   ├── StylusDrawingCanvas.kt  # Compose Canvas with palm rejection
│       │   │   │   └── ChiefComplaintHeader.kt # Section 3 column table
│       │   │   ├── screens/
│       │   │   │   ├── HomeScreen.kt
│       │   │   │   ├── PatientDetailScreen.kt
│       │   │   │   ├── StylusSheetScreen.kt
│       │   │   │   └── WebViewScreen.kt
│       │   │   └── theme/
│       │   │       ├── Color.kt
│       │   │       ├── Theme.kt
│       │   │       └── Type.kt
│       │   └── util/
│       │       └── BackupManager.kt    # JSON backup export & import
│       └── res/
│           ├── drawable/               # Adaptive launcher & monochrome icons
│           ├── mipmap-anydpi-v26/
│           ├── values/                 # strings.xml, colors.xml, themes.xml
│           ├── values-night/
│           └── xml/file_paths.xml      # FileProvider paths for sharing
├── gradle/
│   ├── libs.versions.toml              # Version catalog (AGP 8.8.0, Kotlin 2.1.0, Compose BOM)
│   └── wrapper/
│       └── gradle-wrapper.properties   # Gradle 8.11.1
├── build.gradle.kts                    # Root build script
├── settings.gradle.kts
├── gradle.properties
├── gradlew
└── gradlew.bat
```

---

## 🛠️ How to Open & Build

1. Open **Android Studio** (Koala Feature Drop, Ladybug, Meerkat, or newer recommended for Android 16 / API 36).
2. Select **Open** and browse to:
   `c:\Users\Dell\Downloads\SCR_Case_Book_App-1\SCR_Case_Book_Android`
3. Allow Gradle to sync dependencies.
4. Connect an Android tablet, phone, or launch an Android 16 (API 36) Emulator.
5. Click **Run (`Shift + F10`)**.

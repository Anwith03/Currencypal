# CurrencyPal 💱

**CurrencyPal** is an Android application designed for quick, accurate, and offline-capable currency conversion. It fetches real-time exchange rates from a public API and allows users to seamlessly convert between various global currencies.

---

## ✨ Features

- **Real-Time Exchange Rates**: Live rate updates via the `open.er-api.com` service.
- **Multi-Currency Support**: Supports major global currencies including USD, EUR, GBP, INR, JPY, AUD, CAD, CHF, CNY, SEK, and NZD.
- **Quick Currency Swap**: Easily swap the source ("From") and target ("To") currencies with one tap.
- **Offline Mode**: Automatically caches exchange rates in local storage (`SharedPreferences`) so conversions continue working even without an internet connection.
- **State Persistence**: Remembers your previously selected currency pair across app launches.
- **Responsive & Accessible UI**: Material Design 3 styled components with edge-to-edge support and layout constraints for multi-device compatibility (phones, foldables, tablets).

---

## 📸 Screenshots

| USD to INR Conversion | INR to USD Conversion |
|:---------------------:|:---------------------:|
| ![USD to INR](screenshots/usd_to_inr.jpg) | ![INR to USD](screenshots/inr_to_usd.jpg) |

---

## 🛠️ Tech Stack & Requirements

- **Language**: Kotlin
- **Min SDK**: API 24 (Android 7.0 Nougat)
- **Target SDK**: API 37
- **UI Architecture**: XML Layouts using `ConstraintLayout` and `Material Components`
- **Networking**: `java.net.URL` & `org.json.JSONObject`
- **Persistence**: `SharedPreferences`
- **Build System**: Gradle with Kotlin DSL (`build.gradle.kts`)

---

## 📁 Project Structure

```text
CurrencyPal/
├── app/
│   ├── build.gradle.kts                 # App-level build configuration & dependencies
│   └── src/
│       ├── main/
│       │   ├── java/com/example/currencypal/
│       │   │   └── MainActivity.kt      # Main activity handling UI logic, API fetching & caching
│       │   ├── res/
│       │   │   ├── layout/
│       │   │   │   └── activity_main.xml # Responsive layout with ConstraintLayout
│       │   │   ├── values/
│       │   │   │   ├── colors.xml       # Palette definitions
│       │   │   │   ├── strings.xml      # UI strings & currency list array
│       │   │   │   └── themes.xml       # Application theme configuration
│       │   │   └── values-night/
│       │   │       └── themes.xml       # Dark mode theme settings
│       │   └── AndroidManifest.xml      # App permissions & launch activity setup
│       ├── androidTest/                 # Android instrumentation tests
│       └── test/                        # Local JVM unit tests
├── gradle/
│   └── libs.versions.toml               # Dependency version catalog
├── screenshots/                         # App screenshots & UI previews
├── build.gradle.kts                     # Root-level Gradle build script
├── settings.gradle.kts                  # Gradle settings & plugin repositories
└── README.md                            # Project documentation
```

---

## 🚀 Getting Started

### Prerequisites
- Android Studio Ladybug (or newer)
- JDK 11 or higher
- Android SDK 37 (or an Android Device / Emulator running API 24+)

### Building and Running

1. **Clone the repository**:
   ```bash
   git clone https://github.com/your-username/CurrencyPal.git
   cd CurrencyPal
   ```

2. **Open in Android Studio**:
   Open Android Studio, select **Open**, and navigate to the `CurrencyPal` root folder.

3. **Sync Gradle**:
   Let Android Studio download dependencies and sync the project structure.

4. **Run the App**:
   Select your target device or emulator and press **Run** (`Shift + F10` or click `▶`).

---

## 📱 How to Use

1. **Enter Amount**: Type the numeric value you want to convert in the amount field.
2. **Select Currencies**: Pick the source currency in the **From** dropdown and target currency in the **To** dropdown.
3. **Swap Currencies**: Tap the 🔄 sync button between the dropdowns to quickly switch the two currencies.
4. **Convert**: Press the **Convert** button to calculate the target value using live rates.
5. **Reset**: Press **Reset** to clear the input field and calculated result.

---


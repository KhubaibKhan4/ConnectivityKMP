[![Maven Central](https://img.shields.io/maven-central/v/io.github.khubaibkhan4/ConnectivityKMP.svg?label=Maven%20Central)](https://search.maven.org/artifact/io.github.khubaibkhan4/connectivity-kmp)
![GitHub License](https://img.shields.io/github/license/KhubaibKhan4/ConnectivityKMP)
![GitHub Issues](https://img.shields.io/github/issues/KhubaibKhan4/ConnectivityKMP)
![GitHub Pull Requests](https://img.shields.io/github/issues-pr/KhubaibKhan4/ConnectivityKMP)
![GitHub Last Commit](https://img.shields.io/github/last-commit/KhubaibKhan4/ConnectivityKMP)
![GitHub Stars](https://img.shields.io/github/stars/KhubaibKhan4/ConnectivityKMP?style=social)

![Supported Platforms](https://img.shields.io/badge/platform-Android-green.svg)
![Supported Platforms](https://img.shields.io/badge/platform-iOS-blue.svg)
![Supported Platforms](https://img.shields.io/badge/platform-JVM-red.svg)
![Supported Platforms](https://img.shields.io/badge/platform-WASMJS-blue.svg)

<img src="https://img.shields.io/liberapay/patrons/KhubaibKhanDev.svg?logo=liberapay">

# 📡 ConnectivityKMP

**ConnectivityKMP** is a **Kotlin Multiplatform library** that provides a unified way to check **network connectivity status** across platforms with a **Compose-first API**.

It supports:

- ✅ Android
- ✅ iOS
- ✅ JVM/Desktop
- ✅ Web (Wasm/JS)

---

## ✨ Features

- 📱 **Android** – Uses `ConnectivityManager` callbacks to detect real-time connectivity changes.
- 🍏 **iOS** – Uses Apple’s **`nw_path_monitor`** for monitoring connectivity.
- 💻 **JVM/Desktop** – Fallback mechanism via socket reachability checks.
- 🌐 **Web (Wasm/JS)** – Detects online/offline using browser events (`navigator.onLine`).
- 🧩 **Composable API** – Exposes a simple `rememberNetworkStatus()` you can use anywhere in Compose.
- ⚡ Lightweight and dependency-free.

---

## 🚀 Getting Started

Add the dependency from Maven Central:

```kotlin
kotlin {
    sourceSets {
        commonMain.dependencies {
           implementation("io.github.khubaibkhan4:connectivity-kmp:0.0.1")
        }
    }
}
```

```kotlin
@Composable
fun App() {

   // get the Network Status
   val isNetworkAvailable by rememberNetworkStatus()


   Box(
      modifier = Modifier.fillMaxSize().background(Color.White),
      contentAlignment = Alignment.Center
   ) {
      if (isNetworkAvailable){
         BasicText("Connected")
      } else {
         BasicText("Disconnected")
      }
   }
}
```

### Run Sample App

 - Desktop JVM: `./gradlew :sample:composeApp:run`
 - Android: `open project in Android Studio and run the sample app`
 - iOS: `open 'sample/iosApp/iosApp.xcodeproj' in Xcode and run the sample app`
 - Wasm: `./gradlew :sample:composeApp:wasmJsBrowserRun`

### Publish to MavenLocal

1) Run `./gradlew :shared:publishToMavenLocal`
2) Open `~/.m2/repository/my/connectivity/kmp/`

### Publish to MavenCentral

1) Create an account and a namespace on Sonatype:  
   https://central.sonatype.org/register/central-portal/#create-an-account
2) Add developer id, name, email and the project url to  
   `./shared/build.gradle.kts`
3) Generate a GPG key:  
   https://getstream.io/blog/publishing-libraries-to-mavencentral-2021/#generating-a-gpg-key-pair
   ```
   gpg --full-gen-key
   gpg --keyserver keyserver.ubuntu.com --send-keys XXXXXXXX
   gpg --export-secret-key XXXXXXXX > XXXXXXXX.gpg
   ```
4) Add these lines to `gradle.properties`:
   ```
   signing.keyId=XXXXXXXX
   signing.password=[key password]
   signing.secretKeyRingFile=../XXXXXXXX.gpg
   mavenCentralUsername=[generated username]
   mavenCentralPassword=[generated password]
   ```
5) Run `./gradlew :shared:publishAndReleaseToMavenCentral --no-configuration-cache`

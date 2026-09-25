# 📱 Network Assistant

**Network Assistant** is an Android application developed using
**Kotlin** as a Mobile Application Development (MAD) project. The
application helps users check their network condition, signal strength,
network speed, ping, and identify a potentially better network location.

## 👨‍💻 Developed By

**Manish Singh**

## 📖 Project Overview

Network Assistant is designed to provide users with useful information
about their current network connection in a simple and
easy-to-understand interface.

The application can display the current network status and signal
strength and can perform network tests such as download speed, upload
speed, and ping. It can also provide a suggested location where a better
signal may be available.

## ✨ Features

### 📶 Current Network Information

-   Displays the current network connection.
-   Shows signal strength.
-   Displays signal status such as Strong, Medium, or Weak.

### 🚀 Network Speed Test

The application can measure: - Download speed in Mbps - Upload speed in
Mbps - Ping/latency in milliseconds

### 📍 Better Network Location

The application can display a suggested location with a better signal.

Example:

``` text
Better network found!

Move towards:
37.42200, -122.08400

Best Signal: 4 / 4
```

### 🧭 Start Guidance

The **Start Guidance** button can be used to provide guidance toward the
suggested network location.

### 🎨 User Interface

The application uses a clean, dark-themed interface with: - Signal
information - Network status - Progress indicators - Action buttons -
Easy-to-read text

## 🛠️ Technologies Used

-   **Kotlin**
-   **Android Studio**
-   **Android SDK**
-   **XML**
-   **AndroidX**
-   **Material Design**
-   `ConnectivityManager`
-   `TelephonyManager`
-   Android Location APIs
-   `HttpURLConnection`

## 📂 Project Structure

``` text
app/
└── src/
    └── main/
        ├── java/
        │   └── com.example.a24012011225_mad_project/
        │       ├── MainActivity.kt
        │       ├── Network_test.kt
        │       └── ...
        │
        ├── res/
        │   ├── layout/
        │   │   ├── activity_main.xml
        │   │   ├── activity_network_test.xml
        │   │   └── ...
        │   ├── drawable/
        │   ├── mipmap/
        │   └── values/
        │
        └── AndroidManifest.xml
```

## 🌐 Network Testing

The `Network_test.kt` activity performs three main tests.

### Download Speed

The application downloads test data and calculates the speed based on
the amount of data received and the time taken.

``` text
Download Speed =
(total bytes × 8) / time in seconds / 1,000,000
```

The result is displayed in **Mbps**.

### Upload Speed

The application sends test data to a server and calculates the upload
speed.

``` text
Upload Speed =
(total bytes × 8) / time in seconds / 1,000,000
```

### Ping

The application measures the time required to reach a network host and
displays the result in milliseconds.

> Note: Android's `InetAddress.isReachable()` may not always behave like
> a traditional ICMP ping. The result can depend on the device and
> network.

## 🔐 Required Permissions

Depending on the implemented features, the application may require:

``` xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
```

Location permissions should only be requested when location-based
features are being used.

## ▶️ How to Run

1.  Install **Android Studio**.
2.  Open the project in Android Studio.
3.  Allow Gradle to synchronize.
4.  Connect an Android device or use an emulator.
5.  Grant the required permissions.
6.  Click **Run ▶**.
7.  Open the Network Assistant screen.
8.  Use the available network testing and guidance features.

## 📊 Example Output

``` text
Network Assistant

Current Network: Unknown
Current Signal: 4 / 4
Status: Strong Signal

Better network found!

Move towards:
37.42200, -122.08400

Best Signal: 4 / 4

[ Start Guidance ]
[ Back ]
```

Network test example:

``` text
Download: 25.40 Mbps
Upload: 8.72 Mbps
Ping: 42 ms

Network test completed
```

## ⚠️ Important Notes

-   Network speed depends on the Internet connection, device, server,
    and network conditions.
-   Results may vary between Wi-Fi and mobile data.
-   Signal strength and Internet speed are different measurements.
-   Location-based features require appropriate location permissions.
-   Sample coordinates should be replaced with dynamically detected
    coordinates in a production version.
-   The loading indicator should be hidden after the corresponding
    network/location operation is completed.

## 🚀 Future Enhancements

Future versions can include:

-   Real-time network monitoring
-   Automatic 4G/5G/Wi-Fi detection
-   Network operator name
-   Google Maps integration
-   Nearby signal-strength scanning
-   Signal and speed graphs
-   Network test history
-   Automatic best-location detection
-   Turn-by-turn navigation
-   Notifications for poor network quality
-   Improved ping measurement
-   Light and dark theme support

## 🎓 Academic Information

  Detail                 Information
  ---------------------- --------------------------------------
  Project Name           Network Assistant
  Project Type           Mobile Application Development (MAD)
  Platform               Android
  Programming Language   Kotlin
  UI                     XML / Material Design
  IDE                    Android Studio
  Developer              **Manish Singh**

## 👨‍💻 Developer

### Manish Singh

Developed as an academic **Mobile Application Development (MAD)**
project using Android Studio and Kotlin.

------------------------------------------------------------------------

**© 2026 Manish Singh --- Network Assistant**

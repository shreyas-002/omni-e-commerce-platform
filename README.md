# Omni E-Commerce Platform

A native, modern e-commerce Android application built with **Kotlin**, **Jetpack Compose**, and **Material 3**. Omni delivers a high-fashion shopping experience featuring 4K studio product photography, bi-directional location auto-fill, an Amazon/Myntra-style cart with inline recommendations, a multi-step checkout pipeline, live order tracking, and local database persistence.

---

## Key Features & System Modules

### 📱 Declarative Jetpack Compose UI
* **100% Declarative Layouts**: Built entirely in Jetpack Compose replacing legacy XML views.
* **Adaptive Grid Layout**: Dynamically adjusts catalog column counts (2 columns on mobile, 3 on foldables, 4 on tablets).
* **System Bar Protection**: Built-in status and navigation bar insets (`statusBarsPadding()`, `navigationBarsPadding()`) preventing overlaps.

### 🔒 User Registration & Authentication
* **Dual Auth Modes**: Toggle between **Sign In** and **Sign Up / Register**.
* **Comprehensive Registration**: Captures Full Name, Email, Password, Phone Number (`+91`), and Delivery Street Address.
* **Input Validation**: Client-side regex checking for email formats and password strength.
* **Quick Demo Access**: 1-tap demo account selector chips for rapid testing.

### 📍 Smart Bi-Directional Location Engine
* **PIN Code → City & State**: Entering a 6-digit Indian PIN code (e.g. `560001` -> Bengaluru, Karnataka; `110001` -> New Delhi, Delhi) automatically populates City and State.
* **City → PIN Code**: Typing a City name automatically suggests the matching PIN code.

### 🖼️ 4K Studio Product Photography
* **Asynchronous Image Caching**: Integrated **Coil (`coil-compose`)** library for network image loading, disk caching, and cross-fade animations across all product cards, cart items, and receipts.

### 🛍️ Amazon / Myntra-Style Full Cart Page
* **Direct Cart Tab**: Accessible directly from the bottom navigation bar.
* **Itemized Cart List**: Product photos, quantity modifiers (`-`, count, `+`), trash removal, and delivery estimates (*"Delivery by 3 business days"*).
* **Inline "Customers Also Added" Carousel**: In-cart horizontal carousel allowing users to browse and add recommended items with one tap without leaving the cart screen.
* **Sticky Bottom Bar**: Pinned total payable amount + *"Proceed to Checkout →"* action bar.

### 💳 Multi-Step Checkout Pipeline
* **Step 1: Order Summary**: Review items, quantities, shipping (FREE), and total.
* **Step 2: Shipping & Contact Form**: Address, Phone Number, and PIN code.
* **Step 3: Payment Method Selection**: UPI (*Google Pay / PhonePe*), Credit/Debit Card, Cash on Delivery (COD), or Net Banking.
* **Step 4: Confirmation Receipt**: Generates a real Order ID (`#OMNI-XXXXX`), address receipt, and estimated delivery date.

### 🚚 Profile & Live Order Tracking
* **Profile Management**: Displays member name, email, phone number, saved delivery location, and VIP Gold status.
* **Live Order Tracking**: Active order history cards featuring a 4-stage status tracker (*Confirmed* → *Shipped* → *Out for Delivery* → *Delivered*).

### 💾 Local Persistence Repository (`OmniRepository`)
* SharedPreferences-backed data layer saving user profiles, default delivery locations, active cart items, and order history across app restarts.

---

## 🛠️ Technology Stack

| Component | Technology |
| :--- | :--- |
| **Language** | Kotlin (100% Modern Kotlin) |
| **UI Framework** | Jetpack Compose, Material Design 3 (Material3) |
| **Architecture** | MVVM (Model-View-ViewModel) with Unidirectional Data Flow (UDF) |
| **State Management** | StateFlow, MutableStateFlow, Coroutines |
| **Image Loading** | Coil (`io.coil-kt:coil-compose`) |
| **Persistence** | SharedPreferences with JSON Serialization (`OmniRepository`) |
| **Build System** | Gradle (Kotlin DSL - `build.gradle.kts`) |

---

## 📂 Project Architecture

```
Omniecommerceplatform/
├── app/src/main/
│   ├── java/com/example/omnie_commerceplatform/
│   │   ├── data/
│   │   │   ├── OmniData.kt          # Mock product catalog & categories
│   │   │   ├── OmniRepository.kt    # Persistent database layer
│   │   │   └── OrderModels.kt       # User, Address, Order & PIN lookup models
│   │   ├── ui/
│   │   │   ├── components/
│   │   │   │   ├── CartBottomSheet.kt
│   │   │   │   └── OmniLogo.kt
│   │   │   ├── screens/
│   │   │   │   ├── CheckoutScreen.kt
│   │   │   │   ├── HomeScreen.kt
│   │   │   │   ├── SignInScreen.kt
│   │   │   │   └── SplashScreen.kt
│   │   │   ├── theme/
│   │   │   └── OmniViewModel.kt
│   │   └── MainActivity.kt
│   └── res/
│       ├── drawable/                 # 3D Logo & Product Vector Assets
│       └── values/
```

---

## 🚀 Getting Started

### Prerequisites
* **Android Studio** (2026.1.2 or higher)
* **Android SDK** API Level 24+ (Android 7.0 Nougat or newer)

### Installation
1. **Clone the repository**:
   ```bash
   git clone https://github.com/shreyas-002/omni-e-commerce-platform.git
   ```
2. **Open in Android Studio**:
   Open the project folder in Android Studio and let Gradle sync.
3. **Run the App**:
   Select an Android Emulator or connected physical device and press **Run** (`Shift + F10`).

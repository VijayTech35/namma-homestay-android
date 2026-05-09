# 🏡 Namma HomeStay

**Rural Stays, Real Experiences** — A production-grade Android application connecting travelers with authentic rural homestay hosts in Karnataka, India.

## ✨ Features

### 👤 Authentication
- **Phone OTP** — Firebase Phone Auth with reCAPTCHA verification
- **Google Sign-In** — One-tap sign-in with Google account
- **Session Persistence** — Secure session via EncryptedSharedPreferences
- **Role Selection** — Guest, Host, or Admin on first sign-up

### 🏠 Guest Features
- **Browse Listings** — Scrollable grid of verified homestays with shimmer loading
- **Search** — Real-time search by name, location, or description (300ms debounce)
- **Filters** — Quick chips (All / Available / Verified / Low Price) + advanced filter dialog
- **Favorites ❤️** — Save wishlist items, toggle from detail screen
- **Share ↗️** — Share homestay details via any app
- **Detail View** — Full info, today's menu, nearby places, Google Maps integration
- **Inquiries** — Send booking inquiries directly to hosts

### 🏪 Host Features
- **Dashboard** — Quick actions: Inquiries, Menu, Guide Places, Settings
- **Manage Listings** — Add/Edit/Delete your homestays with image upload
- **Inquiry Management** — View guest inquiries, one-tap call or WhatsApp
- **Daily Menu** — Add menus manually or **AI-generate** via Gemini API 🤖
- **Guide Places** — Add nearby attractions with map coordinates

### 🛡️ Admin Features
- **Panel** — View all listings with verify/reject/delete controls
- **Chip Filters** — All / Verified / Pending with pull-to-refresh

### ⚙️ Settings
- **Edit Profile** — Update name and phone number
- **Dark Mode** 🌙 — Toggle dark/light theme, persisted across sessions
- **Logout** — Secure session cleanup

## 🚀 Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Kotlin 1.9.22 |
| Architecture | MVVM + Repository Pattern, Single-Activity with Navigation Component |
| UI | Material Design 3, ViewBinding, ConstraintLayout, RecyclerView |
| Backend | Firebase Auth, Firestore, Cloud Storage |
| AI | Gemini Pro API (menu generation) |
| Images | Coil (loading), custom compression |
| Auth | Firebase Phone Auth, Google Sign-In |
| Security | EncryptedSharedPreferences |
| Animations | Shimmer Facebook, Lottie-ready |
| Build | Gradle KTS, AGP 8.x |

## 📱 Screenshots

*(Add screenshots here after building and running the app)*

## 🛠️ Setup

### Prerequisites
- Android Studio Hedgehog (2023.1.1) or later
- JDK 17
- Android SDK Platform 34 + Build-Tools 34.0.0
- A Firebase project (see below)

### 1. Clone
```bash
git clone https://github.com/VijayTech35/namma-homestay-android.git
cd namma-homestay-android
```

### 2. Firebase Setup
1. Go to [Firebase Console](https://console.firebase.google.com)
2. Create a project (or use existing)
3. Register Android app with package name `com.nammahomestay`
4. Download `google-services.json` and place in `app/`
5. Enable: **Phone Auth**, **Google Sign-In**, **Email/Password** (for admin)
6. Create **Firestore Database** (test mode) + **Cloud Storage**

### 3. Configure Keys

**Google Web Client ID** — `app/src/main/java/com/nammahomestay/utils/Constants.kt:20`
```kotlin
const val GOOGLE_WEB_CLIENT_ID = "YOUR_WEB_CLIENT_ID.apps.googleusercontent.com"
```
Find this in Firebase Console → Authentication → Sign-in method → Google → Web SDK configuration.

**Gemini API Key** — Already set in `Constants.kt:14`, replace with your own if needed.

### 4. Build & Run
```bash
# Clean build
./gradlew clean

# Debug APK
./gradlew assembleDebug

# Install on connected device
./gradlew installDebug
```

## 🔥 Firestore Structure

### Collections
| Collection | Description |
|-----------|-------------|
| `users` | User profiles (uid, phone, name, role) |
| `homestays` | Listings with full details, photos, coordinates |
| `inquiries` | Guest-to-host booking inquiries |
| `daily_menu` | Daily food menus (per homestay) |
| `guide_places` | Nearby tourist attractions |
| `favorites` | User wishlist (userId → homestayId) |

### Security Rules
See [AGENTS.md](AGENTS.md#3-firebase-security-rules) for recommended Firestore and Storage rules.

## 👑 Admin Access
No admin registration UI. Create via Firebase Console:
1. Authentication → Add user (email/password)
2. Firestore → `users` collection → Add document with UID and `role: "admin"`

## 🧪 Testing

| Scenario | How to Test |
|----------|-------------|
| Auth flow | Open app → enter phone → OTP → select role → dashboard |
| Google Sign-In | Tap "Sign in with Google" → choose account → role selection |
| Guest browsing | Browse listings → filter → tap card → view details → favorite/share |
| Host flow | Add listing with image → manage menu (AI generate) → guide places |
| Admin panel | Login as admin → verify/reject listings |
| Dark mode | Settings → toggle Dark Mode |
| Search | Type in search bar → real-time results |
| Offline | Enable airplane mode → app shows cached data gracefully |

## 🏗️ Architecture

```
NammaHomeStay/
├── app/
│   ├── src/main/
│   │   ├── java/com/nammahomestay/
│   │   │   ├── adapter/        # RecyclerView adapters
│   │   │   ├── data/
│   │   │   │   ├── model/      # Data classes (HomeStay, User, etc.)
│   │   │   │   └── repository/ # Firebase operations
│   │   │   ├── ui/
│   │   │   │   ├── admin/      # Admin panel
│   │   │   │   ├── auth/       # Login, OTP, Role selection, Splash
│   │   │   │   ├── guest/      # Browse, Detail, Filter, Inquiry
│   │   │   │   ├── host/       # Dashboard, Add/Edit, Menu, Places
│   │   │   │   └── settings/   # Profile, Dark mode, Logout
│   │   │   ├── utils/          # Constants, Session, Validation, etc.
│   │   │   ├── MainActivity.kt # Single activity host
│   │   │   ├── NammaHomeStayApp.kt # Application class
│   │   │   └── SplashActivity.kt # Branded splash with shimmer
│   │   ├── res/                # Layouts, drawables, values, themes
│   │   └── AndroidManifest.xml
│   └── google-services.json    # Firebase config (replace with yours)
├── build.gradle.kts
├── settings.gradle.kts
└── gradle/
```

## 📄 License

MIT License — feel free to use and modify.

## 🤝 Contributing

Pull requests are welcome. For major changes, please open an issue first to discuss what you'd like to change.

---

Built with ❤️ for rural tourism in Karnataka

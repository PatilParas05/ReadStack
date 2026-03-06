<img width="1474" height="704" alt="readstack banner" src="https://github.com/user-attachments/assets/e0aedf11-ec2c-444e-b73e-d73e9f12e46b" />

---
# 📚 ReadStack

> ⚠️ **This project is built purely for learning purposes.** It is a hands-on Android application developed to practice and explore modern Android development concepts, architecture patterns, and Jetpack libraries.

---

## 📖 About the Project

**ReadStack** is an Android app that allows users to **search for books** using the Google Books API, **save their favorite books** to a personal library, **view detailed book information**, and **share favorite quotes** as images. The project serves as a practical playground for learning and implementing modern Android development best practices.

---

## 🚀 Features

- 🔍 **Book Search** — Search books by title, author, or keyword using the Google Books API
- 📄 **Paginated Search Results** — Load more results dynamically with pagination support
- 📚 **Personal Library** — Add and manage your own reading list stored locally
- 🗑️ **Swipe to Delete** — Remove books from your library with a swipe gesture
- 📘 **Book Detail Screen** — View full details including cover, author, page count, and description
- 🌐 **Open Book Preview** — Launch book previews in a custom Chrome tab (in-app browser)
- 💬 **Quote Sharing** — Write a favorite quote on a book card and share it as an image
- 📡 **Offline Support** — Cached search results and library available without internet
- 🔌 **Network Monitoring** — Real-time connectivity detection with offline banner UI
- 🔄 **Background Sync** — Periodic library sync via WorkManager (every 6 days, on Wi-Fi + charging)
- 🔔 **Snackbar Tips** — Contextual hints shown to the user (e.g., swipe-to-delete tip on first book)

---

## 🏗️ Architecture & Project Structure

The project follows **Clean Architecture** with a clear separation of concerns:

```
com.paraspatil.readstack
│
├── data/
│   ├── local/           # Room database: BookDao, BookEntity, SearchResultEntity
│   ├── remote/          # Retrofit API: GoogleBookApi, DTOs, Mappers
│   ├── repository/      # BookRepositoryImpl
│   └── util/            # NetworkMonitor
│
├── domain/
│   ├── model/           # Book domain model
│   ├── repository/      # BookRepository interface
│   └── util/            # NetworkResult sealed class
│
├── ui/
│   ├── library/         # LibraryScreen, LibraryViewModel, BookCard, QuoteShareCard
│   ├── details/         # BookDetailScreen, BookDetailViewModel
│   └── theme/           # Material3 theme, colors, typography
│
├── navigation/          # AppNavigation, AppScreens (type-safe routes)
├── di/                  # Hilt modules: AppModule, RepositoryModule, WorkManagerModule, DatabaseModule, NetworkModule
├── workers/             # SyncWorker (WorkManager)
└── ReadStackApp.kt      # Hilt Application class
```

---

## 🛠️ Tech Stack & Libraries Used

| Category | Technology |
|---|---|
| **Language** | Kotlin |
| **UI Framework** | Jetpack Compose + Material 3 |
| **Architecture** | Clean Architecture + MVVM |
| **Dependency Injection** | Hilt (Dagger) |
| **Local Database** | Room (v2.6.1) |
| **Networking** | Retrofit 2 + Kotlinx Serialization |
| **Image Loading** | Coil (v2.4.0) |
| **Background Work** | WorkManager (v2.9.0) |
| **Navigation** | Jetpack Navigation Compose (v2.8.5) with type-safe routes |
| **Async/Concurrency** | Kotlin Coroutines + Flow |
| **In-App Browser** | AndroidX Browser (Custom Tabs) |
| **JSON Parsing** | Gson + Kotlinx Serialization JSON |
| **Icons** | Material Icons Extended |
| **Build System** | Gradle Kotlin DSL + Version Catalog |

---

## 🧠 What I Learned / Concepts Practiced

This project was built to explore and practice the following concepts:

- ✅ **Jetpack Compose** — Building declarative UI with state hoisting, `LaunchedEffect`, `collectAsState`, and animated composables
- ✅ **MVVM + Clean Architecture** — Separating UI, domain, and data layers with clear contracts via interfaces
- ✅ **Hilt Dependency Injection** — Constructor injection, `@HiltViewModel`, `@HiltWorker`, and Hilt modules
- ✅ **Room Database** — Defining entities, DAO with `Flow`, `@Upsert`, offline-first data access
- ✅ **Retrofit + Kotlin Serialization** — Type-safe API calls with serialization converter
- ✅ **Kotlin Coroutines & Flow** — `StateFlow`, `callbackFlow`, `combine`, `map`, `stateIn`, coroutine scopes
- ✅ **Offline-First Architecture** — Caching search results in Room, serving local data when offline
- ✅ **WorkManager** — Periodic background sync with constraints (network + charging)
- ✅ **Type-Safe Navigation** — Using `@Serializable` sealed interface routes with Navigation Compose
- ✅ **NetworkMonitor** — Real-time connectivity tracking using `ConnectivityManager.NetworkCallback`
- ✅ **Canvas & Graphics** — Capturing a composable as a bitmap using `GraphicsLayer` to share as image
- ✅ **SwipeToDismiss** — Implementing swipe-to-delete with `SwipeToDismissBox`
- ✅ **Custom Chrome Tabs** — Opening URLs inside the app using `AndroidX Browser`
- ✅ **Pagination** — Manual pagination with `startIndex` and `maxResults` for API calls



---

## ⚙️ Requirements

- **Android Studio** Hedgehog or newer
- **Min SDK**: 26 (Android 8.0)
- **Target SDK**: 36
- **Kotlin**: Latest stable
- Internet permission required (for Google Books API)

---

## 🚦 Getting Started

1. **Clone the repository**
   ```bash
   git clone https://github.com/PatilParas05/ReadStack.git
   ```
2. **Open** in Android Studio
3. **Sync** Gradle dependencies
4. **Run** on an emulator or physical device (Android 8.0+)

> No API key required — uses the public Google Books API endpoint.

---

## 📁 Key Files

| File | Description |
|---|---|
| `ReadStackApp.kt` | Hilt Application class + WorkManager setup |
| `MainActivity.kt` | Entry point, sets up Compose and navigation |
| `AppNavigation.kt` | Navigation graph with type-safe routes |
| `LibraryScreen.kt` | Main UI: library + search tabs, quote sharing |
| `LibraryViewModel.kt` | State management, search, pagination, sync |
| `BookRepositoryImpl.kt` | Data layer: API + Room + NetworkMonitor |
| `BookDao.kt` | Room DAO: library and search result queries |
| `SyncWorker.kt` | Background WorkManager sync worker |
| `NetworkMonitor.kt` | Real-time network connectivity observer |

---

## 🤝 Contributing
Contributions are welcome! Since this is a learning project, feel free to open issues or pull requests to suggest improvements, fix bugs, or add new features. 

---

## ⚠️ Disclaimer

This project is created **solely for educational and learning purposes**. It is not intended for production use. The app uses the free [Google Books API](https://developers.google.com/books) and does not store or transmit any personal user data.

---

## 👨‍💻 Author

**Paras Patil** — [@PatilParas05](https://github.com/PatilParas05)

---

*Happy Reading & Happy Learning! 📚*

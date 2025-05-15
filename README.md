# 🎬 FilmWorld

FilmWorld is an Android application that allows users to browse, search, and explore popular TV shows using a clean, modern UI and structured architecture. The app is built using Kotlin, MVVM architecture, and Jetpack components.

## ✨ Features

- 📺 Browse most popular TV shows
- 🔍 Search TV shows in real time
- 📝 View detailed information about each show (description, rating, genre, etc.)
- ❤️ Add shows to watchlist
- 🌐 Fetch data from a remote API
- ⚡ Smooth user experience with ViewPager, RecyclerView, and LiveData

## 🧠 Architecture

The app follows **MVVM (Model-View-ViewModel)** architecture and uses best practices for separation of concerns.

### Key Components

- **LiveData & ViewModel** – For lifecycle-aware data handling
- **Navigation Component** – For safe and intuitive fragment navigation
- **Retrofit2** – For API communication
- **RxJava** – For handling background tasks and threading
- **ViewPager2** – For image sliders and navigation
- **RecyclerView** – For listing TV shows efficiently
- **DataBinding** – For cleaner and reactive UIs

## 📸 Screenshots

> (Insert images of your app here if available)

## 🚀 Getting Started

### Prerequisites

- Android Studio Hedgehog (or newer)
- Gradle 8.0+
- Kotlin 1.9+
- Min SDK: 21

### Installation

```bash
git clone https://github.com/avivhanoon/FilmWorld.git
├── adapters/
├── models/
├── network/
├── repositories/
├── ui/
│   ├── fragments/
│   └── viewmodels/
├── utils/
├── MainActivity.kt
└── ...

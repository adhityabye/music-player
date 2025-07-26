# Music Player App

A minimal Android music player built with Kotlin and Jetpack Compose, showcasing MVVM and manual DI.

## 📁 Project Structure

```text
app/
└── src/
    └── main/
        └── java/
            └── by/
                └── tutorials/
                    └── musicapp/
                        ├── MainActivity.kt  + MainScreen.kt         # Our main screen just here
                        ├── data/
                        │   ├── model/
                        │   │   ├── Song.kt           # Track data model
                        │   │   └── SongResponse.kt   # API response wrapper
                        │   ├── remote/
                        │   │   └── MusicApiService.kt # Retrofit interface
                        │   └── repository/
                        │       ├── MusicRepository.kt     # Repository interface
                        │       └── MusicRepositoryImpl.kt # Implementation
                        ├── di/
                        │   └── ServiceLocator.kt      # Manual DI helper
                        ├── playback/
                        │   └── PlaybackManager.kt     # MediaPlayer wrapper
                        ├── viewmodel/
                        │   ├── SongViewModel.kt        # UI state holder
                        │   └── SongViewModelFactory.kt # ViewModel factory
                        └── ui/
                            ├── components/
                               ├── SearchBar.kt         # Reusable Composable
                               ├── MusicList.kt         # Reusable Composable
                               └── PlaybackControls.kt  # Reusable Composable

```

## 💀 Features So Far

1. Display songs via the iTunes API
2. List display of tracks with title, and artist
3. Playback controls: play, pause, previous, next, and seeking slider
4. MVVM architecture with StateFlow for UI state
5. Manual DI via a ServiceLocator

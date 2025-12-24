# Murmur - Twitter/X Clone Android App

A full-featured social media application built with Jetpack Compose, following Clean Architecture and MVVM design patterns.

## Features

### Core Functionality
- ✅ User Authentication (Email/Password via Firebase Auth)
- ✅ Post murmurs (similar to tweets) with 280 character limit
- ✅ Timeline with infinite scroll pagination (10 murmurs per page)
- ✅ Like/Unlike murmurs with real-time count updates
- ✅ Follow/Unfollow users
- ✅ User profiles with stats (murmurs, followers, following)
- ✅ Delete own murmurs
- ✅ Offline-first with local caching (Room database)

### Technical Features
- 🏗️ **Clean Architecture** - Domain, Data, and Presentation layers
- 🎨 **MVVM Pattern** - ViewModels with StateFlow
- 🧩 **Jetpack Compose** - Modern declarative UI
- 🔥 **Firebase Backend** - Authentication and Firestore database
- 💾 **Room Database** - Local caching for offline support
- 🌐 **Retrofit** - REST API support (optional)
- 🚀 **Kotlin Coroutines** - Async operations
- 🧭 **Navigation Compose** - Screen navigation with animations
- 🎭 **Material 3** - Modern Material Design

## Architecture

### Project Structure
```
app/
├── data/
│   ├── local/
│   │   ├── dao/          # Room DAOs
│   │   ├── entity/       # Database entities
│   │   └── MurmurDatabase.kt
│   ├── remote/
│   │   ├── dto/          # Data transfer objects
│   │   └── FirebaseService.kt
│   ├── repository/       # Repository implementations
│   └── mapper/           # Data mappers
├── domain/
│   ├── model/            # Domain models
│   ├── repository/       # Repository interfaces
│   └── usecase/          # Business logic use cases
├── presentation/
│   ├── auth/             # Authentication screens
│   ├── timeline/         # Timeline screen & ViewModel
│   ├── profile/          # User profile screen & ViewModel
│   ├── post/             # Post murmur screen
│   ├── components/       # Reusable UI components
│   └── navigation/       # Navigation setup
├── di/                   # Dependency injection
└── ui/theme/             # Material Theme customization
```

### Tech Stack

| Category | Technology |
|----------|-----------|
| Language | Kotlin |
| UI Framework | Jetpack Compose |
| Architecture | Clean Architecture + MVVM |
| State Management | StateFlow, LiveData |
| Navigation | Navigation Compose |
| Backend/BaaS | Firebase (Auth + Firestore) |
| Local Database | Room |
| Network | Retrofit + OkHttp |
| Async | Kotlin Coroutines |
| Image Loading | Coil |
| Serialization | Gson |

## Setup Instructions

### Prerequisites
- Android Studio Hedgehog or later
- JDK 11 or later
- Android SDK 26+
- Firebase account

### Firebase Setup

1. **Create a Firebase Project**
   - Go to [Firebase Console](https://console.firebase.google.com/)
   - Create a new project or use existing one
   - Enable Google Analytics (optional)

2. **Add Android App to Firebase**
   - Click "Add app" and select Android
   - Package name: `com.example.mymurmurapp`
   - Download `google-services.json`
   - Replace `/app/google-services.json` with your downloaded file

3. **Enable Firebase Services**
   
   **Authentication:**
   - Go to Firebase Console → Authentication
   - Click "Get Started"
   - Enable "Email/Password" sign-in method
   
   **Firestore Database:**
   - Go to Firebase Console → Firestore Database
   - Click "Create Database"
   - Start in **test mode** for development
   - Choose a Cloud Firestore location
   
4. **Firestore Security Rules** (for production)
   ```javascript
   rules_version = '2';
   service cloud.firestore {
     match /databases/{database}/documents {
       // Users collection
       match /users/{userId} {
         allow read: if true;
         allow write: if request.auth != null && request.auth.uid == userId;
       }
       
       // Murmurs collection
       match /murmurs/{murmurId} {
         allow read: if true;
         allow create: if request.auth != null;
         allow update, delete: if request.auth != null && 
                                 request.auth.uid == resource.data.userId;
       }
       
       // Likes collection
       match /likes/{likeId} {
         allow read: if true;
         allow write: if request.auth != null;
       }
       
       // Follows collection
       match /follows/{followId} {
         allow read: if true;
         allow write: if request.auth != null;
       }
     }
   }
   ```

### Build & Run

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd MyMurmurApp
   ```

2. **Open in Android Studio**
   - File → Open → Select project directory
   - Wait for Gradle sync to complete

3. **Run the app**
   - Connect an Android device or start an emulator
   - Click "Run" or press Shift+F10
   - Select your device and click OK

## Usage

### First-Time Setup

1. **Sign Up**
   - Launch the app
   - Click "Sign Up"
   - Enter email, username, display name, and password
   - Click "Sign Up" button

2. **Post Your First Murmur**
   - Click the floating "+" button
   - Type your message (max 280 characters)
   - Click "Post"

3. **Explore**
   - View timeline of all murmurs
   - Like murmurs by tapping the heart icon
   - Click on user avatars to view profiles
   - Follow users from their profile page

### Features Guide

#### Timeline
- Scroll to see murmurs from all users
- Pull down to refresh (implicit via lazy loading)
- Automatic pagination - scroll to bottom loads more
- Click on user names/avatars to visit profiles
- Tap heart icon to like/unlike

#### Post Murmur
- Tap floating "+" button on timeline
- Maximum 280 characters
- Real-time character counter
- Murmurs appear instantly in your timeline

#### User Profile
- View user information and statistics
- See all murmurs by that user
- Follow/Unfollow button (for other users)
- Delete button on your own murmurs

## Database Schema

### Firestore Collections

**users**
```json
{
  "id": "string",
  "username": "string",
  "displayName": "string",
  "email": "string",
  "bio": "string",
  "profileImageUrl": "string",
  "followersCount": "number",
  "followingCount": "number",
  "createdAt": "timestamp"
}
```

**murmurs**
```json
{
  "id": "string",
  "userId": "string",
  "username": "string",
  "userDisplayName": "string",
  "userProfileImageUrl": "string",
  "content": "string",
  "likesCount": "number",
  "createdAt": "timestamp",
  "updatedAt": "timestamp"
}
```

**likes**
```json
{
  "id": "string",
  "userId": "string",
  "murmurId": "string",
  "createdAt": "timestamp"
}
```

**follows**
```json
{
  "followerId": "string",
  "followingId": "string",
  "createdAt": "timestamp"
}
```

### Room Database (Local Cache)

Mirrors the Firestore schema with additional fields:
- `isLikedByCurrentUser` - for optimistic UI updates

## API Endpoints (Future REST API)

If implementing a custom REST API backend:

```
POST   /api/auth/signup
POST   /api/auth/signin
POST   /api/auth/signout

GET    /api/murmurs?page=0&size=10
POST   /api/me/murmurs
DELETE /api/me/murmurs/:id
GET    /api/murmurs/:id

POST   /api/murmurs/:id/like
DELETE /api/murmurs/:id/like

GET    /api/users/:id
GET    /api/users/:id/murmurs
POST   /api/users/:id/follow
DELETE /api/users/:id/follow
```

## Development

### Adding New Features

1. **Domain Layer** - Add models, repository interfaces, and use cases
2. **Data Layer** - Implement repositories, DAOs, and DTOs
3. **Presentation Layer** - Create ViewModels and Compose screens
4. **Navigation** - Add routes and navigation logic

### Testing

Run tests with:
```bash
./gradlew test           # Unit tests
./gradlew connectedAndroidTest  # Instrumented tests
```

## Troubleshooting

### Common Issues

**Build Error: "google-services.json not found"**
- Solution: Download google-services.json from Firebase Console and place in `/app` directory

**Firebase Auth Error: "Network error"**
- Solution: Ensure internet permission is in AndroidManifest.xml
- Check Firebase project is properly configured

**Room Database Migration Error**
- Solution: Uninstall app and reinstall (clears database)
- Or increment database version in MurmurDatabase.kt

**Compose Error: "Cannot resolve symbol"**
- Solution: File → Invalidate Caches / Restart

## Future Enhancements

- [ ] Image upload for murmurs and profile pictures
- [ ] Comments on murmurs
- [ ] Retweet/Share functionality
- [ ] Direct messaging
- [ ] Notifications
- [ ] Search users and murmurs
- [ ] Hashtags support
- [ ] Trending topics
- [ ] User mentions (@username)
- [ ] Dark/Light theme toggle
- [ ] Multi-language support

## License

This project is for educational purposes.

## Contact

For questions or issues, please create an issue in the repository.


# 🎮 PokéDex Pro

A modern Android application that serves as a mini Pokédex, built with Jetpack Compose and following MVVM architecture principles. This app demonstrates Android development practices, clean architecture, and modern UI/UX design.

## 📱 Features

### 🏠 Home Screen
- **Pokémon List**: Displays the first 151 Pokémon from the PokéAPI (complete original Pokedex)
- **Search Functionality**: Real-time search with case-insensitive filtering
- **Pokémon Images**: High-quality sprites with loading states and error handling
- **Responsive Design**: Optimized for different screen orientations
- **Smooth Scrolling**: LazyColumn with proper keys for optimal performance

### 🔍 Detail Screen
- **Pokémon Information**: Name, ID, and comprehensive details
- **High-Quality Images**: Front sprites with fallback handling
- **Base Stats**: Visual representation with progress bars
- **Types & Abilities**: Combined display in organized cards
- **Platform Design**: Follows Material Design 3 principles

## 📸 Screenshots

### 🏠 Landing Screen (Pokemon List)
![Pokemon List Screen](screenshots/pokemon_list_screen.png)
*The main landing screen showing the Pokemon list with search functionality, featuring a red header bar and dark blue background. Displays Pokemon cards with images, names, and Pokedex numbers.*

### 🔍 Details Screen (Pokemon Information)
![Pokemon Detail Screen](screenshots/pokemon_detail_screen.png)
*The Pokemon details screen showing comprehensive information including stats, types, abilities, and high-quality images. Features the same red header with Pokemon-specific theming.*



## 🏗️ Architecture

### **MVVM Pattern**
- **Model**: Data classes and repository layer
- **View**: Jetpack Compose UI components
- **ViewModel**: State management and business logic

### **Dependency Injection**
- **Koin**: Lightweight DI framework
- **Singleton Pattern**: REST client implementation
- **Interface Segregation**: ViewModels accept interfaces

### **SOLID Principles**
- **Single Responsibility**: Each class has one purpose
- **Open/Closed**: Extensible without modification
- **Liskov Substitution**: Interfaces can be swapped
- **Interface Segregation**: Focused interfaces
- **Dependency Inversion**: High-level modules don't depend on low-level

## 🛠️ Technical Stack

### **Core Technologies**
- **Kotlin**: Primary programming language
- **Jetpack Compose**: Modern UI toolkit
- **Coroutines**: Asynchronous programming
- **StateFlow**: Reactive state management

### **Networking**
- **Retrofit**: HTTP client for API calls
- **OkHttp**: HTTP client with logging
- **Gson**: JSON serialization/deserialization

### **Image Loading**
- **Coil**: Image loading library
- **Shimmer Effects**: Loading placeholders
- **Fallback URLs**: Graceful error handling

### **Testing**
- **JUnit**: Unit testing framework
- **Mockito**: Mocking framework
- **Coroutines Test**: Asynchronous testing

## 🚀 Getting Started

### **Prerequisites**
- Android Studio Hedgehog or later
- JDK 17
- Android SDK 34
- Minimum API Level 24

### **Installation**
1. **Clone the repository**:
   ```bash
   git clone https://github.com/dennisluke11/pokemonApp.git
   ```
2. **Navigate to project directory**:
   ```bash
   cd pokemonApp
   ```
3. **Open in Android Studio**: File → Open → Select the `pokemonApp` folder
4. **Sync Gradle files**: Click "Sync Now" when prompted
5. **Run the app**: Click the green play button or press Shift+F10

## 🔧 Key Components

### **Data Layer**
- **API Service**: Retrofit interface for PokéAPI integration
- **Repository**: Data access layer with caching
- **Models**: Data classes for API responses

### **UI Layer**
- **Screens**: List and detail screens with Compose
- **Components**: Reusable UI components
- **Navigation**: Compose Navigation setup

### **Business Logic**
- **ViewModels**: State management and business logic
- **State Management**: StateFlow for reactive UI updates
- **Error Handling**: User-friendly error messages

## 🌐 API Integration

### **PokéAPI v2**
- **Base URL**: `https://pokeapi.co/api/v2/`
- **Endpoints**: Pokémon list and details
- **Data**: Comprehensive Pokémon information

## 🎨 Design Features

### **Theme & Styling**
- **Custom Colors**: Pokémon-inspired color scheme
- **Material Design 3**: Modern design principles
- **Responsive Layout**: Adaptive to different screen sizes

### **User Experience**
- **Loading States**: Shimmer effects and progress indicators
- **Error Handling**: Graceful fallbacks and user feedback
- **Performance**: Efficient image loading and state management

## 🧪 Testing

### **Test Coverage**
- **Repository Tests**: Data fetching and error handling
- **ViewModel Tests**: State management and business logic
- **Coverage**: Happy path and error scenarios

### **Test Execution**
```bash
./gradlew test
```

## 📁 Project Structure

```
app/src/main/java/com/example/pokemonapp/
├── data/           # Data layer (API, models, repository)
├── di/             # Dependency injection
├── ui/             # UI components and screens
└── MainActivity.kt # App entry point
```

## 🔮 Future Enhancements

- Offline support with local database
- Pagination for larger Pokemon lists
- Favorite Pokemon functionality
- Advanced filtering and sorting options
- Pokemon evolution chains
- Move sets and learnable moves

## 🆕 Recent Improvements

### ✅ **Critical Fixes Applied**
- **Pokemon Limit**: Increased from 100 to 151 Pokemon (complete original Pokedex)
- **Crash Prevention**: Fixed Koin initialization issues
- **Null Safety**: Removed all force unwrapping (!!) operators
- **UI States**: Converted to sealed classes for better type safety
- **Scrolling**: Fixed LazyColumn performance with proper keys

### 🎨 **UI/UX Enhancements**
- **Custom App Icon**: Beautiful Pokeball design
- **Authentic Colors**: Pokemon Red/Blue color scheme
- **Better Navigation**: Simplified and more reliable
- **Error Handling**: Improved user feedback and recovery
- **Performance**: Optimized image loading and state management
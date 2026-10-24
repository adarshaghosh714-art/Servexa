# Servexa — Service Booking Android App

Servexa is an Android application designed to connect customers with service providers through a single platform. Customers can explore service options and manage service requests, while service providers can manage their profiles and handle customer requests.

Built with **Kotlin, Jetpack Compose, Firebase, and MVVM architecture**, Servexa focuses on a modern user interface and a streamlined service-management experience.

## ✨ Features

- **User Authentication** — Firebase Authentication for user sign-in and registration.
- **Dual Account Roles** — Customer and Service Provider account modes.
- **Profile Management** — Manage personal information, account role, and service selections.
- **Service Management** — Support for service categories such as plumbing, electrical work, carpentry, painting, AC servicing, and appliance repair.
- **Job Management** — Customer requests and service-provider job workflows.
- **In-App Messaging** — Conversation and chat functionality.
- **Light and Dark Mode** — Choose Light, Dark, or System default appearance.
- **Modern UI** — Built with Jetpack Compose and Material 3.
- **Cloud Data Storage** — Firebase Cloud Firestore integration.

## 🛠️ Tech Stack

| Technology | Purpose |
|---|---|
| Kotlin | Primary programming language |
| Jetpack Compose | Declarative Android UI |
| Material 3 | UI components and theming |
| Navigation Compose | Navigation between screens |
| Firebase Authentication | User authentication |
| Cloud Firestore | Cloud database |
| DataStore Preferences | Persistent theme preferences |
| MVVM | Application architecture |
| Kotlin Coroutines & Flow | Asynchronous operations and reactive state |
| Git & GitHub | Version control and project hosting |

## 🏗️ Architecture

Servexa follows the **Model–View–ViewModel (MVVM)** architectural pattern.

- **Model:** Represents application data, such as users, jobs, conversations, and chat messages.
- **View:** Compose screens display application data and collect user interactions.
- **ViewModel:** Manages UI state and coordinates application operations.
- **Repository:** Handles data access and communication with Firebase.
- **Navigation:** Manages transitions between application screens.

This separation helps keep the UI, application logic, and data operations organized and maintainable.

## 📱 Screens and Modules

The application includes modules for:

- Authentication and registration
- Customer dashboard
- Service-provider dashboard
- User profile and account switching
- Service selection
- Job management and customer requests
- Conversations and chat
- Appearance preferences

## 🚀 Getting Started

### Prerequisites

- Android Studio
- Android SDK compatible with the project configuration
- JDK compatible with the project's Gradle configuration
- A Firebase project
- An Android device or emulator

### Installation

1. Clone the repository:

   ```bash
   git clone https://github.com/YOUR_USERNAME/YOUR_REPOSITORY.git
   ```

2. Open the cloned project in Android Studio.

3. Configure Firebase:
   - Create or select a project in the Firebase Console.
   - Register your Android application using its exact application ID.
   - Download `google-services.json`.
   - Place the file in the app module directory, typically `app/google-services.json`.
   - Enable Firebase Authentication using the authentication provider configured by the app.
   - Set up Cloud Firestore.

4. Sync the project with Gradle.

5. Build and run the application on an emulator or Android device.

> **Note:** Replace the example GitHub repository URL with your actual repository URL. Do not commit private keys, service-account credentials, or other secrets to GitHub.

## 🎨 Theme Support

Servexa supports three appearance preferences:

- **System default:** Follows the device's system appearance.
- **Light:** Uses the light color scheme.
- **Dark:** Uses the dark color scheme.

Theme preferences are stored locally using DataStore Preferences.

## 🔐 Security

- Firebase Authentication is used for account authentication.
- Firestore Security Rules should restrict data access according to authenticated users, account roles, and resource ownership.
- Firebase configuration and security rules should be reviewed before production deployment.
- Never publish private credentials or service-account keys.

## 📦 APK Download

Download the Android APK from the repository's **GitHub Releases** section.

**Latest release:** `v1.0.0`

If no release is published yet, visit the repository's Releases page after publishing the APK.

## 🛣️ Future Improvements

Potential future enhancements include:

- Push notifications for new service requests and messages
- Improved job tracking and status updates
- Ratings and reviews for service providers
- Advanced service search and filtering
- Additional UI and accessibility improvements
- Expanded testing and production security hardening

## 👨‍💻 Developer

**Adarsh Ghosh**

B.Tech Computer Science and Engineering — Artificial Intelligence and Machine Learning

Interested in Android development, Kotlin, modern mobile UI, and Firebase-powered applications.

## 📄 License

Choose a license before allowing others to reuse or distribute this project. If you intend to publish it as an open-source project, consider adding a `LICENSE` file.

---

*Servexa — Bringing customers and service providers closer together.*

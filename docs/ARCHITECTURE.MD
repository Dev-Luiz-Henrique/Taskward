# Taskward Architecture

Taskward is a gamified task management application for Android. Below is an overview of the architecture and technology stack used to build the application. The system follows a multi-layer architecture designed to be scalable, maintainable, and easy to test.

## 1. Architecture Overview

Taskward follows a layered architecture with a clear separation of concerns:

- **Presentation Layer**: Handles user interaction (UI).
- **Service Layer**: Contains business logic (e.g., task and reward management).
- **Controller Layer**: Manages communication between the UI and the business logic.
- **Data Layer**: Responsible for data persistence and database operations.
- **Exception Layer**: Handles error management through custom exceptions.

This separation allows for easy maintenance, testing, and potential future scalability.

## 2. Technologies and Tools

### Android SDK & Android Studio
- **IDE**: Android Studio
- **SDK Version**: Android SDK 34 (API 26 minimum, API 34 target)
- **Gradle Plugin Version**: 8.7.2
- **Programming Language**: Java

### Libraries and Dependencies
- **AndroidX Libraries**:
  - AppCompat, Material, Activity, ConstraintLayout, Navigation Fragment/UI
- **Testing Libraries**:
  - JUnit, Mockito, Espresso
- **JSQParser**: For handling SQL queries programmatically

### Database
- **SQLite**: Direct interaction using `SQLiteOpenHelper` for data storage, without using the Room library.
- **DatabaseHelper**: Handles database creation and management.
- **DatabaseContract**: Defines the database schema (tables and columns).

### APK Signing
- APK is signed using `keystore.properties` for production releases.

## 3. Project Layer Structure

The project is structured into different layers:

### Controller Layer
- Manages application logic and communication between the model and the view (UI).
  - `RewardController.java`
  - `TaskController.java`
  - `TaskEventController.java`
  - `UserController.java`

### Data Layer
- Handles data persistence and database interactions.
  - `DatabaseContract.java`: Defines database schema.
  - `DatabaseHelper.java`: Uses `SQLiteOpenHelper` for database management.

- Sub-packages:
  - **enums**: Contains constants like task status and frequency.
  - **models**: Represents data entities (`Reward.java`, `Task.java`, `TaskEvent.java`, `User.java`).
  - **repositories**: CRUD operations for database access.

### Exception Layer
- Manages custom error handling.
  - **codes**: Specific error codes (e.g., `DatabaseErrorCode.java`).
  - **custom**: Custom exceptions for database operations.
  - **handlers**: Error handling classes (e.g., `DatabaseErrorHandler.java`).

### Service Layer
- Contains core application logic.
  - `PointService.java`: Manages points and rewards logic.

### Presentation Layer (UI Layer)
- Deals with UI elements, activities, and fragments.
  - `MainActivity.java`
  - **Fragments**: e.g., `CreateProfileFragment.java`, `CreateRewardsFragment.java`.

- Sub-packages:
  - **adapters**: Manages UI components like `RewardsAdapter.java`, `TaskAdapter.java`.
  - **fragments**: Represent different screens in the app.

### Utility Layer
- Provides helper functions for various tasks.
  - `CacheManager.java`: Manages data caching.
  - `DateUtils.java`: Date handling functions.
  - `Logger.java`: For logging.
  - `NavigationHelper.java`: Handles navigation between activities/fragments and setup the BottomNavigationMenu and the Toolbar.
  - `SQLScriptUtils.java`: Manages SQL scripts.
  - `TaskScheduler.java`: Manages scheduled tasks.
  - `UIUtils.java`: UI-related helper methods.

## 4. Design Pattern

Taskward follows the **MVC (Model-View-Controller)** design pattern:

- **Model**: Classes in the `models` package represent data and business logic (e.g., `Task.java`, `User.java`).
- **View**: UI elements (e.g., `MainActivity.java`, `CreateProfileFragment.java`).
- **Controller**: Classes in the `controllers` package (e.g., `TaskController.java`, `UserController.java`), which manage the interaction between the Model and View.

This approach ensures a clean separation 

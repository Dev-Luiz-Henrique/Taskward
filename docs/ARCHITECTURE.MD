# Taskward Architecture

Taskward is a gamified task management application for Android. Below is an overview of the architecture and technology stack used to build the application. The system follows a layered architecture inspired by the **Model-View-Controller (MVC)** design pattern, with additional layers for scalability, maintainability, and testability. The project incorporates key design patterns such as **Singleton**, **Factory**, and **Observer** to enhance the robustness and flexibility of the application.

---

## 1. Architecture Overview

Taskward's architecture is a **layered architecture inspired by MVC**, tailored for modern Android development. It combines the core principles of MVC with additional layers like **Service**, **Repository**, and **Utility** to ensure clear separation of concerns, modularity, and scalability. Design patterns such as **Singleton**, **Factory**, and **Observer** are used throughout the system to further enhance the design.

### Layers Overview:
- **Presentation Layer (View)**: Handles user interaction (UI).
- **Service Layer**: Encapsulates business logic and reusable operations (e.g., points management).
- **Controller Layer**: Acts as a mediator between the Presentation and Service/Data layers.
- **Data Layer (Model)**: Manages data persistence and database operations, abstracted by repositories.
- **Exception Layer**: Centralizes error handling through custom exceptions.
- **Utility Layer**: Provides reusable helpers for tasks like navigation, logging, and scheduling.

This architecture ensures **maintainability**, **testability**, and **scalability**, while incorporating modern Android best practices.

---

## 2. Key Design Patterns

### **Singleton**
- **DatabaseHelper**: Ensures only one instance of the database manager is active using a singleton pattern.  
  - This helps prevent connection conflicts and redundant resource usage.
- **ExceptionHandler**: Centralizes error handling across the application and ensures consistent behavior through a single shared instance.

### **Factory**
- Custom exceptions, such as `DatabaseOperationException` and `NavigationHelperException`, implement **Factory Methods** to simplify and standardize exception creation.

### **Observer**
- **NavigationHelper**: Uses **LiveData** to observe and update points dynamically across the app's UI components.
- **MainActivity**: Observes changes in user points via `NavigationHelper` and updates the UI (e.g., the points displayed on the toolbar).

---

## 3. Technologies and Tools

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
- **SQLite**: Direct interaction using `SQLiteOpenHelper` for data storage.
- **DatabaseHelper**: Implements the **Singleton Pattern** and uses `executeSQLFromResource` to handle SQL scripts (e.g., database creation and initialization).

### APK Signing
- APK is signed using `keystore.properties` for production releases.

---

## 4. Project Layer Structure

### **Presentation Layer (UI Layer)**
- Manages user interaction and displays data.
  - **Activities**: e.g., `MainActivity.java`
  - **Fragments**: e.g., `CreateProfileFragment.java`, `CreateRewardsFragment.java`
- **Sub-packages**:
  - **adapters**: Handles UI components like `RewardsAdapter.java`, `TaskAdapter.java`.
  - **fragments**: Represents different screens in the app.

### **Service Layer**
- Encapsulates business logic and reusable operations.
  - e.g., `PointService.java`: Manages points and rewards logic.

### **Controller Layer**
- Bridges the Presentation and Service layers, delegating tasks and managing communication.
  - Examples: `RewardController.java`, `TaskController.java`, `UserController.java`.

### **Data Layer**
- Responsible for data persistence and database interactions.
  - **DatabaseHelper.java**: A **Singleton** class managing SQLite database operations.
  - **DatabaseContract.java**: Defines the database schema.
- **Sub-packages**:
  - **enums**: Constants like task status and frequency.
  - **models**: Represents data entities (`Reward.java`, `Task.java`, `TaskEvent.java`, `User.java`).
  - **repositories**: Centralizes CRUD operations.

### **Exception Layer**
- Centralizes error handling with:
  - **codes**: e.g., `DatabaseErrorCode.java`.
  - **custom exceptions**: e.g., `DatabaseOperationException.java`, implementing **Factory Methods**.
  - **handlers**: e.g., `ExceptionHandler.java`, a **Singleton** that processes and displays errors.

### **Utility Layer**
- Provides helper functions for various tasks.
  - `CacheManager.java`: Manages data caching.
  - `DateUtils.java`: Handles date formatting and operations.
  - `Logger.java`: Simplifies logging.
  - `NavigationHelper.java`: Implements navigation logic, observing user points using **LiveData**.
  - `SQLScriptUtils.java`: Manages SQL scripts for database setup.
  - `TaskScheduler.java`: Handles scheduled tasks.


## 5. Design Pattern and Layered Architecture

Taskward follows a **layered architecture inspired by Model-View-Controller (MVC)**, enhanced with additional layers for better modularity and scalability:

- **Model**: Handles data representation and business logic, abstracted via repositories.
- **View**: Manages user interaction through UI components (Activities, Fragments, Adapters).
- **Controller**: Mediates between the View and Service layers, delegating tasks and managing communication.

### Modern Extensions:
- **Service Layer**: Encapsulates reusable business logic, such as reward calculations and task management.
- **Repository**: Provides a clean abstraction for data access, ensuring decoupled database operations.
- **Singleton**: Used in classes like `DatabaseHelper` and `ExceptionHandler` to ensure consistent, centralized operations.
- **Factory**: Standardizes exception creation in `DatabaseOperationException` and `NavigationHelperException`.
- **Observer**: Implements live updates through `LiveData` in classes like `NavigationHelper` and `MainActivity`.

This hybrid approach ensures clean separation of concerns while aligning with best practices for modern Android development.

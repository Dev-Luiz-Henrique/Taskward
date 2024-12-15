# Taskward: The Gamified Task Management App

Taskward is an innovative Android application designed to help you manage your tasks in a fun and motivating way, using gamification elements. The app allows you to organize daily, weekly, monthly, yearly, or one-time tasks while earning points and unlocking rewards.

## 1. Features

### Task Creation with Frequency and Interval
You can add tasks with predefined repetition frequencies and intervals to control their recurrence:

- **Frequency Options**:
  - Daily
  - Weekly
  - Monthly
  - Annually

- **Interval**: Defines how often the task will repeat within the selected frequency. For example:
  - Daily with an interval of 1 means it repeats every day.
  - Weekly with an interval of 2 means it repeats every two weeks.
  - Monthly with an interval of 3 means it repeats every three months.

This provides flexibility to plan your activities based on your goals.

### Points and Gamification
Completing tasks earns you points. The more tasks you complete, the more points you gain, motivating you to keep progressing. Points serve as a measure of your performance over time.

### Customizable Rewards
You can set up your own rewards to motivate yourself. Examples of rewards include:
- Buying a new gadget (e.g., PS5).
- Enjoying a favorite treat (e.g., chocolate).
- Any other reward that suits your goals.

As you accumulate points, you can redeem them for your personalized rewards.

### Profile and Statistics
Taskward provides a profile dashboard where you can view your:
- Task completion history.
- Accumulated points.
- Reward milestones.

This helps you track your progress, stay motivated, and adjust your goals.

### Local Storage and Privacy
Taskward uses SQLite for local data storage, ensuring that all your data is stored securely on your device. There is no need for a constant internet connection, making it ideal for offline use.

## 2. How It Works

### 1. Create Tasks
Add new tasks with defined frequency (e.g., daily, weekly) and intervals (e.g., every 2 days, every 3 weeks).

### 2. Earn Points
Complete tasks to earn points based on their difficulty and repetition frequency.

### 3. Redeem Rewards
Set your rewards and link them to your tasks. Once you accumulate the required number of points, you can redeem your reward.

### 4. Track Progress
Use the profile dashboard to view completed tasks, accumulated points, and the rewards you've unlocked.

## 3. Benefits

- **Increased Productivity**: Gamification makes completing tasks more fun and motivating.
- **Complete Customization**: Create tasks, rewards, and goals tailored to your needs.
- **Privacy**: Your data remains safe and secure on your device.
- **Detailed Statistics**: Track your progress and adjust goals to improve performance.

## 4. Technologies and Tools

### Android SDK & Android Studio
- **IDE**: Android Studio
- **SDK Version**: Android SDK 34 (API 26 minimum, API 34 target)
- **Programming Language**: Java

### Database
- **SQLite**: Used directly for data storage without Room.

### Libraries
- **AndroidX Libraries**: AppCompat, Material, Navigation, and more.
- **Testing Libraries**: JUnit, Mockito, Espresso.

## 5. Version and Dependencies

- **JUnit**: 4.13.2
- **Material**: 1.12.0
- **AppCompat**: 1.7.0
- **Navigation Fragment**: 2.8.4

Taskward is built to be a simple yet effective tool for managing tasks while keeping users motivated with a personalized gamification experience.

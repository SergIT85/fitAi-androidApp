# User Flows for FitAI

This document describes the primary user journeys for the FitAI MVP.

---

## 1. First Launch & Onboarding Flow

**Goal:** A new user opens the app, creates an account, and completes the initial setup.

**Path:**

1.  **App Start**
    - **[Splash Screen]** is shown.
    - System checks for an active session token.
    - **Decision:**
        - **If** token exists and is valid -> **Go to step 9 (Dashboard).**
        - **If** no token -> **Continue to step 2.**

2.  **Welcome**
    - **[Welcome Screen]** is shown.
    - *Content:* App logo, brief value proposition.
    - *Action:* User taps the "Get Started" button.
    - *Next Screen:* **[Login Screen]**

3.  **Authentication**
    - **[Login Screen]** is shown.
    - *Content:* "Sign in with Google" button.
    - *Action:* User taps "Sign in with Google".
    - The Google Sign-In flow is initiated.
    - **Decision:**
        - **If** successful -> System gets user profile, creates a user document in Firestore -> **Continue to step 4.**
        - **If** failed/cancelled -> Show an error message (e.g., a Toast or Snackbar) on the **[Login Screen]**.

4.  **Onboarding Step 1: Goals**
    - **[Onboarding - Goals Screen]** is shown.
    - *Content:* "What is your primary goal?" (e.g., Lose Weight, Build Muscle, Maintain Fitness).
    - *Action:* User selects one goal and taps "Next".
    - *Data Saved:* The selected goal is stored locally (e.g., in a ViewModel, to be saved at the end).

5.  **Onboarding Step 2: Parameters**
    - **[Onboarding - Parameters Screen]** is shown.
    - *Content:* Fields for Gender, Age, Height, Weight.
    - *Action:* User fills in the details and taps "Next".
    - *Data Saved:* Parameters are stored locally.

6.  **Onboarding Step 3: Finish**
    - **[Onboarding - Finish Screen]** is shown.
    - *Content:* "You're all set!" summary message.
    - *Action:* User taps "Go to Dashboard".
    - *System Action:* All onboarding data is saved to DataStore to mark onboarding as complete.
    - *Next Screen:* **[Dashboard Screen]**

7.  **(END OF ONBOARDING FLOW)**

---

8.  **App Start (Returning User)**
    - **[Splash Screen]** is shown.
    - System checks for an active session token.
    - Token is found and is valid.
    - *Next Screen:* **[Dashboard Screen]**

9.  **Main App Screen**
    - **[Dashboard Screen]** is shown.
    - **(END OF RETURNING USER FLOW)**


## 2. Logging a Workout

**Goal:** The user selects a pre-defined workout, completes all exercises and sets, and saves the completed session.

**Path:**

1.  **Start from Dashboard**
    - The user is on the **[Dashboard Screen]**.
    - *Action:* User taps the "Start a Workout" button.
    - *Next Screen:* **[Workout List Screen]**

2.  **Select a Workout**
    - **[Workout List Screen]** is shown.
    - *Content:* A list of available workout plans (e.g., "Full Body Strength - Day 1", "Leg Day").
    - *Action:* User taps on "Full Body Strength - Day 1".
    - *Next Screen:* **[Workout Detail Screen]**

3.  **Review the Workout**
    - **[Workout Detail Screen]** is shown.
    - *Content:* Shows a summary of the workout: list of exercises (e.g., Squats, Push-ups, Plank), number of sets/reps for each.
    - *Action:* User taps the "Start Workout" button.
    - *Next Screen:* **[Exercise Screen]** for the first exercise (Squats).

4.  **Perform an Exercise**
    - **[Exercise Screen - Squats]** is shown.
    - *Content:*
        - Exercise name: "Squats"
        - Target: "Set 1 of 3, 10 repetitions"
        - Video/animation of the correct technique.
        - Button to "Start Pose Detection".
        - Input fields for "Reps" and "Weight".
    - *Action (Logging a set):* User performs the set, then enters "10" reps and "20" kg. They tap the "Log Set" button.
    - *System Action:*
        - The screen updates to show "Set 1: 10 reps @ 20kg" as completed.
        - A rest timer for 60 seconds automatically starts.
        - The screen now shows the target for the next set: "Set 2 of 3, 10 repetitions".

5.  **Perform Next Set / Automatic Transition**
    - The user performs and logs all 3 sets of Squats.
    - After the final set of Squats is logged, the app waits for the rest period to finish.
    - *System Action:* The app **automatically navigates** to the next exercise in the plan.
    - *Next Screen:* **[Exercise Screen - Push-ups]**. (The flow returns to Step 4 for the new exercise).

6.  **Workout Completion**
    - After the user logs the final set of the final exercise (Plank).
    - A "Finish Workout" button appears.
    - *Action:* User taps "Finish Workout".
    - *Next Screen:* **[Workout Summary Screen]**

7.  **View Summary**
    - **[Workout Summary Screen]** is shown.
    - *Content:* "Great job!", total time, total volume (weight lifted), list of exercises and sets completed.
    - *Action:* User taps "Done".
    - *Next Screen:* **[Dashboard Screen]**.

8.  **(END OF WORKOUT LOGGING FLOW)**


## 3. Logging a Meal

**Goal:** The user can search for a food item and add it to a specific meal (e.g., Lunch) in their daily diary.

**Path:**

1.  **Start from Dashboard**
    - The user is on the **[Dashboard Screen]**.
    - *Action:* User taps the "Add Food to Lunch" button.
    - *Next Screen:* **[Meal Diary Screen - Lunch]**

2.  **View Meal Diary & Initiate Search**
    - The **[Meal Diary Screen - Lunch]** is shown.
    - *Content:* A list of foods already added to today's lunch (initially empty). An "Add Food" button.
    - *Action:* User taps the "Add Food" button.
    - *Next Screen:* **[Search Food Screen]**

3.  **Search and Add a Food Item**
    - The **[Search Food Screen]** is shown.
    - *Content:* A search bar.
    - *Action:* User types "Chicken Breast" and taps on the correct search result.
    - *System Action:* A bottom sheet appears, asking for the quantity (e.g., in grams).

4.  **Enter Quantity and Save**
    - *Action:* The user enters "150" (for 150g) into the bottom sheet and taps "Save Food".
    - *System Action:*
        - The food item is immediately saved to the database for today's lunch.
        - The bottom sheet closes.
        - The user remains on the **[Search Food Screen]**, ready to search for another food if they wish.

5.  **Return to Diary**
    - *Action:* After adding all desired foods, the user taps the system "Back" button.
    - *Next Screen:* **[Meal Diary Screen - Lunch]**.
    - *Content:* The screen now shows "Chicken Breast, 150g" in the list. The user gets immediate confirmation that their food was saved correctly.

6.  **(END OF MEAL LOGGING FLOW)**






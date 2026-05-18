# 1. Core Architecture

*   **Pattern**: Clean Architecture with an MVI presentation layer (State, Intent, Effect).
*   **Structure**: Multi-module, feature-based.
*   **Dependency Rule**: Dependencies will always point inwards: `Feature` -> `Domain` -> `Data`. The `:core` modules will provide shared utilities to all layers.

# 2. Module Structure & Dependencies

*   `:app`: The main application module. Depends on all `:feature` modules and `:core:navigation`.
*   `:feature:onboarding`: Contains all UI and ViewModel logic for the onboarding flow. Depends on `:core:domain`, `:core:ui`, `:core:common`.
*   `:feature:workout`: Contains workout logging flow. Depends on `:core:domain`, `:core:ui`, `:core:common`.
*   `:feature:dashboard`: Contains dashboard logging flow. Depends on `:core:domain`, `:core:ui`, `:core:common`.
*   `:feature:nutrition`: Contains all UI and ViewModel logic for the nutrition flow. Depends on `:core:domain`, `:core:ui`, `:core:common`.
*   `:feature:settings`: Contains settings logging flow. Depends on `:core:domain`, `:core:ui`, `:core:common`.
*   `:feature:ai-coach`: Contains all UI and ViewModel logic for the ai-coach flow. Depends on `:core:domain`, `:core:ui`, `:core:common`.
*   `:data:repository`: Implementation of domain repositories. Depends on `:core:domain`, `:data:model`, `:core:network`, `:core:database`, `:core:datastore`.
*   `:data:model`: Implementation of models amd mappers. 
*   `:core:domain`: Contains domain models and UseCases (e.g., `LogWorkoutUseCase`). Has NO Android dependencies. The most independent module is injected via App module.
*   `:core:network`: Provides Retrofit, OkHttp, and API definitions.
*   `:core:common`: Provides extensions, utility classes, and the `BaseViewModel`.
*   `:core:ui`: Provides shared composables, Material 3 theme, and typography.
*   `:core:database`: Provides Room database configuration and base DAOs.
*   `:core:datastore`: Provides DataStore instances for key-value or proto storage.
*   `:core:navigation`: Provides navigation destinations and the global Navigation Graph.
*   `:core:testing`: Provides test utilities, fakes, and custom test rules.

# 3. Database Schema

### `users`
id (String)
name (String)
email (String)


### `workouts`
id (Long, PK)
user_id (String, FK)
date (Long)


### `workout_exercises`
id (Long, PK)
workout_id (Long, FK)
exercise_name (String)


### `exercise_sets`
id (Long, PK)
workout_exercise_id (Long, FK)
reps (Int)
weight_kg (Double)


### `cached_foods`
id (String, PK)          — Unique identifier from the external food API.
name (String)             — Name of the food item.
brand (String, Optional)  — Brand or manufacturer name.
calories_per_100g (Double)— Energy content per 100 grams.
protein_per_100g (Double) — Protein content per 100 grams.
carbs_per_100g (Double)   — Carbohydrate content per 100 grams.
fat_per_100g (Double)     — Fat content per 100 grams.


### `food_diary_entries`
id (Long, PK)             — Unique identifier for the log entry.
user_id (String, FK)      — References users.id.
food_id (String, FK)      — References cached_foods.id.
date (Long)               — Date of consumption (Timestamp).
meal_type (String)        — Categorization of the meal (e.g., Breakfast, Lunch, Dinner, Snack).
grams (Double)            — Weight of the consumed portion in grams.


# 4. API Specification

### Wger API
*   **Endpoint**: `/api/v2/exerciseinfo/`
*   **Usage**: Fetch exercise details, including description and video links.
*   **Trigger**: When the user views the exercise detail screen.

### Open Food Facts API
*   **Endpoint**: `https://world.openfoodfacts.org/api/v0/product/{barcode}.json`
*   **Usage**: Fetch nutrition data for a given barcode.
*   **Trigger**: After the user scans a barcode.

### GigaChat API
*   **Usage**: Provide AI-based coaching tips and meal plan suggestions.
*   **Trigger**: On-demand from the AI Coach screen.

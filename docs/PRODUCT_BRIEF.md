markdown
# Product Brief: [FitAI]

## 🎯 review
This mobile App for people who want to do fitness and monitor their nutrition, solving the problem of not wanting to visit fitness centers and work with a nutritionist

## 🚀 Goals
1. Implement Core-functionality that allows the app work without accessing the network & AI  .
2. Implement local AI functionality.
3. Implement with network request.
4. Upgrade the app to KMP

## 🛑 Non-Goals
- This app does not provide paid access to AI

## 🛠 Main features
1. Authorizations: OAuth2 / Google Sign-In. (Chose)
2. Visual control and correct execution of exercises (With AI with network request)
3. Introduction of functionality for nutrition (food) with network request.

## 📊 Success metrics
- Functionally working app
- Lack of ANR on budget devices.
- Positive feedback from QA.

## 📝 Technical Notes
- Architecture: Clean Architecture + MVI. Multimodularity + Scalability
- Planned Stack: Kotlin, Compose, Coroutines, Hilt, Retrofit2 + OkHttp3 + Kotlin Serialization converter, Room, DataStore Preferences, .
- Planned Api: Wger API, Open Food Facts, Firebase Firestore, Firebase Auth (Yandex?), GigaChat.
- AI: ML Kit Pose Detection (local), ML Kit Barcode Scanner(local), GigaChat (Api).
- AI (Claud) - Firebase Cloud Functions
- Test: MockK, Turbine, kotlinx-coroutines-test, Compose UI Test

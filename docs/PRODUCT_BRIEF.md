markdown
# Product Brief: [FitAI]

## 🎯 Review
This mobile App for people who want to do fitness and monitor their nutrition, solving the problem of not wanting to visit fitness centers and work with a nutritionist
A typical user: Alex, a 30-year-old male manager, works 8 hours a day. He wants to lose weight and increase his vitality. He doesn't feel comfortable or is embarrassed to work out in the gym. He tried a training app but failed because he didn't know how to do the exercises correctly 
FitAI will give him confidence through real-time equipment control and a simple nutrition plan, so he won’t need to visit a nutritionist or work out in a gym.
## 🚀 Goals
1. The user successfully completes and logs their first workout within 15 minutes..
2. The second goal is to allow the user to track their daily calories without manual input.
3. The third goal is to reduce the risk of injury in beginners by providing instant feedback on technique for three basic exercises
## 🛠 Main features
1. Authorizations: Users can sign in to the app using their Google or Yandex account.
2. Visual control and correct: Users can turn on their camera, and the app will provide real-time feedback on their form and highlight whether their joints are moving correctly
3. Food Diary: The user can search for a food item in the database by name and add it to their daily diet by entering their weight.

## 📊 Success metrics
- Retention D7: 50% of users come back to the app within 7 days.
- The workout logger rate: Goal is that 50% of users who start the workout successfully complete and save it.
- Feature Adoption (Nutrition): What percentage of active users have logged at least one meal in the past month?. Goal is 50%.
 - Play Store Rating:  Achieve an average Play Store rating of 4.5 stars after 1,000 installations

## 📝 Technical Notes
- Architecture: Clean Architecture + MVI. Multimodularity + Scalability
- Planned Stack: Kotlin, Compose, Coroutines, Hilt, Retrofit2 + OkHttp3 + Kotlin Serialization converter, Room, DataStore Preferences, .
- Planned Api: Wger API, Open Food Facts, Firebase Firestore, Firebase Auth (Yandex?), GigaChat.
- AI: ML Kit Pose Detection (local), ML Kit Barcode Scanner(local), GigaChat (Api).
- Cloud for communication with the outside AI - Firebase Cloud Functions
- Test: MockK, Turbine, kotlinx-coroutines-test, Compose UI Test


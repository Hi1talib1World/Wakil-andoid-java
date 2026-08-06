# Implementation Plan - Firestore Integration & Persistent Search Bar

This plan combines migrating house posts to Cloud Firestore for collaboration and adding a persistent, prominent search bar for filtering hotel/post titles.

## Proposed Changes

### [Dependencies]

#### [MODIFY] [build.gradle (:app)](file:///C:/Users/hicha/Documents/GitHub/Wakil-andoid-java/app/build.gradle)
- Add Firebase Firestore dependency.
  - `implementation 'com.google.firebase:firebase-firestore'`

### [UI & Layout]

#### [MODIFY] [content_main.xml](file:///C:/Users/hicha/Documents/GitHub/Wakil-andoid-java/app/src/main/res/layout/content_main.xml)
- Add a persistent `MaterialCardView` containing a `SearchView` at the top of the layout (above the filters). This will make searching titles more accessible than the toolbar menu icon.

#### [MODIFY] [strings.xml](file:///C:/Users/hicha/Documents/GitHub/Wakil-andoid-java/app/src/main/res/values/strings.xml)
- Add a hint string for the new search bar, e.g., `search_hint`.

### [Data Layer - Firestore]

#### [MODIFY] [HotelRepository.kt](file:///C:/Users/hicha/Documents/GitHub/Wakil-andoid-java/app/src/main/java/com/denzo/wakil/domain/repository/HotelRepository.kt)
- Add methods to fetch posts from Firestore and save new posts to Firestore.

#### [MODIFY] [HotelRepositoryImpl.kt](file:///C:/Users/hicha/Documents/GitHub/Wakil-andoid-java/app/src/main/java/com/denzo/wakil/data/repository/HotelRepositoryImpl.kt)
- Implement Firestore logic in `HotelRepositoryImpl`.

### [Activity Logic]

#### [MODIFY] [MainActivity.java](file:///C:/Users/hicha/Documents/GitHub/Wakil-andoid-java/app/src/main/java/com/denzo/wakil/ui/home/MainActivity.java)
- Wire up the new layout `SearchView` to the `HotelsAdapter` filter.
- Update `MainActivity` to use `FirebaseAuth` for user identity and logout.

#### [MODIFY] [AddPostActivity.java](file:///C:/Users/hicha/Documents/GitHub/Wakil-andoid-java/app/src/main/java/com/denzo/wakil/ui/home/AddPostActivity.java)
- Update to save new posts directly to Firestore.

#### [MODIFY] [MainViewModel.kt](file:///C:/Users/hicha/Documents/GitHub/Wakil-andoid-java/app/src/main/java/com/denzo/wakil/viewmodel/MainViewModel.kt)
- Update to fetch data from Firestore asynchronously.

## Verification Plan

### Automated Tests
- Build project: `./gradlew :app:assembleDebug`.

### Manual Verification
- **Search**: Type a hotel name in the new search bar and verify the list filters in real-time.
- **Post Creation**: Add a post and verify it appears on the home screen and in the Firebase Console.
- **Multi-user**: Verify posts created by one user are visible to others.

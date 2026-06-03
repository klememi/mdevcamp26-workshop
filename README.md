# Bridge the Gap

### How to Write KMP Devs Actually Like

##### Livesport KMP workshop @mDevCamp 26

> Bridge the Gap: How to Write KMP Devs Actually Like is a practical exploration of Kotlin Multiplatform tailored specifically for native iOS and Android developers. It takes participants through the process of architecting a shared KMP core to power two provided platform-specific applications, highlighting essential best practices for state management, architecture, and Swift interop. The main lesson from this workshop is that sharing business logic doesn't require compromising the native experience. If you want to unify your codebase without frustrating your platform teams, learn with us how to build shared code that native devs actually enjoy using.

#### Tasks

##### Session 1 - Deconstruction & Extraction
- [ ] Add new KMP module
  - In Android Studio, File -> New -> New Module -> Kotlin Multiplatform Shared Module
  - For module name type `shared`, for package name type `eu.livesport.mdevcamp26.shared`
  - uncheck test modules
  - wait for Gradle sync after adding
- [ ] Add the new module as a dependency for Android app
  - build.gradle.kts in android module, dependencies scope, add `implementation(project(":shared"))`
- [ ] Replace content of `build.gradle.kts` in shared module
  - use this predefined content
  ```
  import org.jetbrains.kotlin.gradle.dsl.JvmTarget
  
  plugins {
      alias(libs.plugins.kotlin.multiplatform)
      alias(libs.plugins.kotlin.serialization)
      alias(libs.plugins.android.multiplatform.library)
  }
  
  kotlin {
      android {
          namespace = "eu.livesport.mdevcamp26.shared"
          compileSdk = 36
          minSdk = 24
  
          compilations.all {
              compileTaskProvider.configure {
                  compilerOptions {
                      jvmTarget.set(JvmTarget.JVM_17)
                  }
              }
          }
      }
  
      iosArm64()
      iosSimulatorArm64()
  
      val xcfName = "Shared"
  
      iosArm64 {
          binaries.framework {
              baseName = xcfName
          }
      }
  
      iosSimulatorArm64 {
          binaries.framework {
              baseName = xcfName
          }
      }
  
      sourceSets {
          commonMain.dependencies {
              implementation(libs.koin.core)
              implementation(libs.koin.core.viewmodel)
              implementation(libs.kotlinx.coroutines.core)
              implementation(libs.kotlinx.datetime)
              implementation(libs.kotlinx.serialization.json)
              implementation(libs.ktor.client.content.negotiation)
              implementation(libs.ktor.client.core)
              implementation(libs.ktor.client.logging)
              implementation(libs.ktor.serialization.kotlinx.json)
              implementation(libs.lifecycle.viewmodel)
          }
      }
  }
  ```
  - Dependencies and versions are defined in `libs.versions.toml`
- [ ] In Xcode add new build phase for building KMP
  - above compile sources step, add new run script phase with following content:
  ```
  cd "$SRCROOT/.."
  ./gradlew :shared:embedAndSignAppleFrameworkForXcode
  ```
  - from now on, KMP framework will build and embed automatically into iOS app. You should be able to access this framework using `import Shared`
- [ ] Move domain models from androidApp module into shared/commonMain module
  - fix imports for androidApp module
  - rebuild iOS app with shared framework and replace Swift domain models with shared ones (don't forget to import Shared)
  - you can use annotation `@HiddenFromObjC` in Kotlin shared module to hide objects for iOS

##### Session 2 - The Heavy Lifting
- [ ] Move rest of the objects to be shared to KMP
  - Mappers
  - DTOs
  - HttpClientFactory
  - DataSource
  - Repositories
  - ViewModels with UIStates
  - use refactor in Android Studio
- [ ] Create `SharedModule` and move all content from AppModule there
- [ ] In `createHttpClient` function extract engineFactory to new `expect` function and implement `actual` functions for both platforms - use `OkHttp` for `androidMain` and `Darwin` for `iosMain`
  - Don't forget to add dependencies for corresponding sourceSet in `build.gradle.kts`
- [ ] Remove unnecessary iOS objects - viewModels, domain models, WCApi (they will be replaced with equivalent objects from shared module)
  - You might need to remove/comment out some parts of views for the iOS app to build
- [ ] Write `startKoin` function in `iosMain` which will startKoin with sharedModule dependencies for iOS app
##### Session 3 - The Bridge & Polish
- [ ] Add SKIE dependency for Shared module
  - add `skie = "0.10.12"` into versions section, add `skie = { id = "co.touchlab.skie", version.ref = "skie" }` into plugins section
  - gradle sync
  - add `alias(libs.plugins.skie)` to `build.gradle.kts` in Shared module
  - add these lines to `build.gradle.kts` in Shared module
  ```
  skie {
      features {
          enableSwiftUIObservingPreview = true
      }
  }
  ```
  - gradle sync
- [ ] Start koin when iOS app starts
- [ ] Declare and initialize shared viewModels in SwiftUI Views
- [ ] Implement UI using data from ViewModels and included main view functions in iOS
  - Use `Observing` View from SKIE
- [ ] Fix searchBar View in iOS

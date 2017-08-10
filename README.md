# UI Atoms for Android

MVVM + Dependency Injection + Unit Testing + Line of Business Controls

# Project Level Gradle

    allprojects {
      repositories {
        maven { url "https://jitpack.io" }
      }
    }

# Application level Gradle

    dependencies {
       compile 'com.github.neurospeech:ui-atoms-android:v1.0.11'
    }

# Initialization

    public class AndroidApplication extends Application{
    
      @Override
      public void onCreate(){
        AtomNavigator.register(this,
          0,
          BR.viewModel);
      }
    
    }

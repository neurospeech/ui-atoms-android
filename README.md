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
       compile 'com.github.neurospeech:ui-atoms-android:v1.0.12'
    }

# Sample View Model

```java
  public class TaskEditorViewModel extends AtomViewModel{

    public final ObservableField<String> error = new ObervableField<>();

    // ...    

  }

  public class TaskListActivity extends AtomActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      TaskEditorViewModel viewModel = new TaskEditorViewModel();

      // Bind View model to View
      AtomViewBinding.from(this)
              .attach(viewModel)
              .inflate(R.layout.activity_task_list);
    }

  }



  public class TaskFragment extends AtomFragment {

    @Override
    protected View onCreate(Bundle savedInstanceState) {
      

      TaskEditorViewModel viewModel = new TaskEditorViewModel();

      return AtomViewBinding.from(this)
              .attach(viewModel)
              .inflate(R.layout.activity_task_list);
    }

  }  
```  

# Simple and Small Dependency Injection

```java

  public class AppLogger extends AtomLogger {
    public void log(Throwable ex) {
        ex.printStackTrace();
    }

    public void log(String message){
        Log.d("Logger",message);
    }
  }

  // override existing logger
  DI.put(AtomLogger.class, () -> new AppLogger());

  // resolve and use
  DI.resolve(AtomLogger.class)
    .log("Custom message");

  // register a class with singleton instance

  DI.register(SingletonService.class);

  // register a class for service interface 
  // first parameter is an interface
  // second parameter is an implementation
  DI.register(SingletonServiceInterface.class, SingletonServiceImplementation.class);

  // register a class with scoped instance
  DI.registerScoped(SingletonServiceInterface.class, SingletonServiceImplementation.class);

  // register a transient class which will always return a new instance when resolved
  DI.registerTransient(SingletonServiceInterface.class, SingletonServiceImplementation.class);
  
```

# Display progress in promise resolution

```java

   public class AppProgressIndicator extends ProgressIndicator {

      public void start() {
        // display progress
      }

      public void stop() {
        // hide progress
      }

   }

   // put

   DI.put(ProgressIndicator.class, () -> new AppProgressIndicator());

```

# Handler

```java

    // Handler is available in DI by default

    DI.resolve(Handler.class)
      .postDelayed( 
          () -> {} , 100 );

```

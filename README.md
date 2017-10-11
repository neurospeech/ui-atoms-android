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


# Control AtomListView

* items - Binds AtomListView to ObservableList or AtomList
* layout - Item Layout (Must be bindable, root element must be layout)
* viewModel - Passes viewModel to each item so child item can refer parent
* itemHeader - [optional] Delegate that gives header to be displayed on top of item
* selectedItems - [optional] ObservableList or AtomList which will be used to store selected items
* headerLayout - [optional] Header Item to be displayed before any item
* footerLayout - [optional] Footer Item to be displayed after all items
* allowMultipleSelection - [optional] If set, you can select multiple items

```xml

        <com.neurospeech.uiatoms.AtomListView
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            app:items="@{viewModel.tasks}"
            app:layout="@{R.layout.item_task}"
            app:viewModel="@{viewModel}"
            app:itemHeader="@{ item -> viewModel.getHeader(item) }"
            app:selectedItems="@{ viewModel.selectedTasks }"
            app:headerLayout="@{ R.layout.list_header_task }"
            app:footerLayout="@{ R.layout.list_footer_task }"
            app:allowMultipleSelection="@{true}"
            tools:listitem="@layout/item_task"></com.neurospeech.uiatoms.AtomListView>

```

## Sample Item Layout

```xml

  <layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:atom="http://schemas.android.com/apk/res-auto">

    <data>
        <variable
            name="viewModel"
            type="com.neurospeech.uiatomsdemo.viewmodels.TaskEditorViewModel"/>

        <variable
            name="model"
            type="com.neurospeech.uiatomsdemo.models.Task"/>

        <!-- Optional ItemModel stores header and selected -->
        <variable
            name="itemModel"
            type="com.neurospeech.uiatoms.AtomListView.ItemModel"/>

        <import type="android.view.View"/>
        <import type="com.neurospeech.uiatomsdemo.R"/>


    </data>

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical">

        <!-- Optional Header will be displayed only on first group of headered items -->

        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="@{ itemModel.header }"
            android:visibility="@{ itemModel.header == null ? View.GONE : View.VISIBLE }"
            />

        <LinearLayout
            android:background="@{ itemModel.selected ? @drawable/list_selected_item_background : @drawable/list_item_background }"
            android:orientation="horizontal"
            android:layout_width="match_parent"
            android:layout_height="wrap_content">

            <Switch
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:checked="@={ itemModel.selected }"
                />

            <TextView
                android:layout_width="0dp"
                android:layout_height="wrap_content"
                android:layout_weight="1"
                android:text="@{model.label}" />

            <Button
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="Delete"
                app:clickCommand="@{viewModel.removeCommand}"
                app:commandParameter="@{model}"
                />


        </LinearLayout>
    </LinearLayout>
</layout>

```

# Control AtomView

Dynamic layout loader, you can pass layout ID in `layout` attribute.

```xml
  <com.neurospeech.uiatoms.AtomView
    app:layout="@{ viewModel.getLayoutId() }"
    app:viewModel="@{ viewModel }

    />

```
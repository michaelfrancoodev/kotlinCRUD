# Student CRUD App in Kotlin with Jetpack Compose - Step-by-Step Guide

 **step-by-step guide** to build a simple **CRUD app in Kotlin with Jetpack Compose**, using **MVVM + Room + Coroutines/Flow**. This is a **full minimal example** for a **Student Management App**.

---

## Step 1: Set Up Your Project

1. Open **Android Studio** → **New Project** → **Empty Compose Activity**.
2. Make sure **Kotlin** and **Jetpack Compose** are selected.
3. Open `build.gradle.kts (app)` and add dependencies:

```kotlin
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

android {
    namespace = "com.example.studentcrudapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.studentcrudapp"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.0"
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.2")
    implementation("androidx.activity:activity-compose:1.9.0")
    implementation("androidx.compose.ui:ui:1.6.0")
    implementation("androidx.compose.material3:material3:1.2.0")
    implementation("androidx.compose.ui:ui-tooling-preview:1.6.0")
    
    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.2")
    
    // Room
    implementation("androidx.room:room-runtime:2.6.0")
    kapt("androidx.room:room-compiler:2.6.0")
    implementation("androidx.room:room-ktx:2.6.0")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
}
```

---

## Step 2: Define the Data Layer

### 2.1 Create `Student.kt` (Entity)

```kotlin
package com.example.studentcrudapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "students")
data class Student(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val course: String
)
```

### 2.2 Create `StudentDao.kt` (DAO)

```kotlin
package com.example.studentcrudapp.data.dao

import androidx.room.*
import com.example.studentcrudapp.data.entity.Student
import kotlinx.coroutines.flow.Flow

@Dao
interface StudentDao {
    @Query("SELECT * FROM students")
    fun getAllStudents(): Flow<List<Student>>

    @Insert
    suspend fun insertStudent(student: Student)

    @Update
    suspend fun updateStudent(student: Student)

    @Delete
    suspend fun deleteStudent(student: Student)
}
```

### 2.3 Create `AppDatabase.kt` (Room Database)

```kotlin
package com.example.studentcrudapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.studentcrudapp.data.dao.StudentDao
import com.example.studentcrudapp.data.entity.Student

@Database(entities = [Student::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun studentDao(): StudentDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "student_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
```

---

## Step 3: Repository Layer

```kotlin
package com.example.studentcrudapp.data.repository

import com.example.studentcrudapp.data.dao.StudentDao
import com.example.studentcrudapp.data.entity.Student
import kotlinx.coroutines.flow.Flow

class StudentRepository(private val dao: StudentDao) {
    val allStudents: Flow<List<Student>> = dao.getAllStudents()

    suspend fun insert(student: Student) = dao.insertStudent(student)
    suspend fun update(student: Student) = dao.updateStudent(student)
    suspend fun delete(student: Student) = dao.deleteStudent(student)
}
```

---

## Step 4: ViewModel Layer

```kotlin
package com.example.studentcrudapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studentcrudapp.data.entity.Student
import com.example.studentcrudapp.data.repository.StudentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class StudentViewModel(private val repository: StudentRepository) : ViewModel() {

    private val _students = MutableStateFlow<List<Student>>(emptyList())
    val students: StateFlow<List<Student>> = _students

    init {
        viewModelScope.launch {
            repository.allStudents.collect {
                _students.value = it
            }
        }
    }

    fun addStudent(student: Student) = viewModelScope.launch {
        repository.insert(student)
    }

    fun updateStudent(student: Student) = viewModelScope.launch {
        repository.update(student)
    }

    fun deleteStudent(student: Student) = viewModelScope.launch {
        repository.delete(student)
    }
}
```

---

## Step 5: Build UI with Jetpack Compose

```kotlin
package com.example.studentcrudapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.studentcrudapp.data.entity.Student
import com.example.studentcrudapp.viewmodel.StudentViewModel

@Composable
fun StudentListScreen(viewModel: StudentViewModel) {
    val students by viewModel.students.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        LazyColumn {
            items(students) { student ->
                Text("${student.name} - ${student.course}")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        var name by remember { mutableStateOf("") }
        var course by remember { mutableStateOf("") }
        TextField(value = name, onValueChange = { name = it }, label = { Text("Name") })
        TextField(value = course, onValueChange = { course = it }, label = { Text("Course") })
        Button(onClick = {
            viewModel.addStudent(Student(name = name, course = course))
            name = ""
            course = ""
        }) {
            Text("Add Student")
        }
    }
}
```

---

## Step 6: Connect Everything in MainActivity

```kotlin
package com.example.studentcrudapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studentcrudapp.data.database.AppDatabase
import com.example.studentcrudapp.data.repository.StudentRepository
import com.example.studentcrudapp.ui.screens.StudentListScreen
import com.example.studentcrudapp.viewmodel.StudentViewModel
import com.example.studentcrudapp.viewmodel.StudentViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dao = AppDatabase.getDatabase(this).studentDao()
        val repository = StudentRepository(dao)
        val factory = StudentViewModelFactory(repository)

        setContent {
            val studentViewModel: StudentViewModel = viewModel(factory = factory)
            StudentListScreen(studentViewModel)
        }
    }
}
```

---

## Step 7: ViewModel Factory

```kotlin
package com.example.studentcrudapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.studentcrudapp.data.repository.StudentRepository

class StudentViewModelFactory(private val repository: StudentRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StudentViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StudentViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
```

---

✅ **Now you have a full, functional CRUD app**:

* Add new students ✅
* View students list ✅
* Update or Delete students (can be added with UI buttons) ✅
* Uses **MVVM + Room + Jetpack Compose + Coroutines** ✅



# Student CRUD App - Folder/Package Structure

```
com.example.studentcrudapp
│
├── data
│   ├── entity
│   │   └── Student.kt          # Defines the data model (Room Entity)
│   ├── dao
│   │   └── StudentDao.kt       # Data Access Object (CRUD queries)
│   └── database
│       └── AppDatabase.kt      # Room Database setup
│
├── data.repository
│   └── StudentRepository.kt    # Provides clean API for data operations
│
├── viewmodel
│   ├── StudentViewModel.kt     # Handles UI state & business logic
│   └── StudentViewModelFactory.kt  # Factory to provide repository to ViewModel
│
└── ui
    ├── components              # Reusable UI components (buttons, text fields, cards)
    └── screens
        └── StudentListScreen.kt  # Composable screen displaying students
```

### Explanation of Packages:

1. **`data.entity`**

   * Contains Kotlin `data class` mapping to database tables (Room Entity).

2. **`data.dao`**

   * Interfaces defining CRUD operations using Room annotations.

3. **`data.database`**

   * Room database setup providing access to DAOs.

4. **`data.repository`**

   * Acts as a single source of truth for data operations.

5. **`viewmodel`**

   * Handles UI state and communicates with the repository.

6. **`ui.screens`**

   * Compose UI screens displaying data and handling user interactions.

7. **`ui.components`**

   * Optional folder for reusable Compose UI elements.




# Student CRUD App - MVVM + Room Architecture Flow

 Here’s a clear **visual flow diagram** for your **Student CRUD App** showing **how data moves** in the **MVVM + Room architecture**:

```
┌───────────────────┐
│     UI Layer       │
│ (Jetpack Compose) │
│ StudentListScreen │
└─────────┬─────────┘
          │ User actions (Add, Update, Delete)
          ▼
┌───────────────────┐
│   ViewModel Layer │
│ StudentViewModel  │
└─────────┬─────────┘
          │ Calls repository functions & exposes state via Flow/State
          ▼
┌───────────────────┐
│ Repository Layer  │
│ StudentRepository │
└─────────┬─────────┘
          │ Performs data operations via DAO
          ▼
┌───────────────────┐
│ Data Access Layer │
│     StudentDao    │
└─────────┬─────────┘
          │ Executes SQL queries (Room)
          ▼
┌───────────────────┐
│   Database Layer  │
│   AppDatabase     │
└───────────────────┘
```

### **Flow Explanation:**

1. **UI Layer**

   * Users interact with Compose screens (`StudentListScreen`).
   * Inputs like adding a student trigger **events** handled by the ViewModel.

2. **ViewModel Layer**

   * Receives UI events.
   * Calls repository methods to modify data.
   * Exposes state (list of students) to UI using `StateFlow` or `LiveData`.

3. **Repository Layer**

   * Acts as a **single source of truth**.
   * Decides how data is fetched or stored (from database, network, or cache).

4. **Data Access Layer (DAO)**

   * Executes database operations (`insert`, `update`, `delete`, `query`).
   * Returns data as **Flow** for reactive UI updates.

5. **Database Layer**

   * Room database persists the data.
   * Ensures data is stored locally on the device.

---

✅ This diagram helps you visualize the **direction of data flow**:

* **Top → Bottom:** Commands from UI to database
* **Bottom → Top:** Updated data from database to UI automatically via Flow


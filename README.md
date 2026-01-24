```md
# Building a CRUD Mobile App with Kotlin and Jetpack Compose

Building a **CRUD (Create, Read, Update, Delete)** mobile app with **Kotlin and Jetpack Compose** is the recommended modern approach for Android development.  
Best practice involves using a **single-activity MVVM architecture** with **Jetpack Room** for local data persistence.

---

## Core Technologies

- **Kotlin**  
  The official and preferred language for Android development, known for its conciseness, null-safety, and modern features.

- **Jetpack Compose**  
  Android’s modern, declarative UI toolkit that simplifies and accelerates UI development with less code.

- **MVVM (Model–View–ViewModel)**  
  A robust architectural pattern that separates UI logic from business logic, improving maintainability and testability.

- **Jetpack Room**  
  An abstraction layer over SQLite that simplifies database interactions through a type-safe API and compile-time SQL validation.

- **Coroutines / Flow**  
  Kotlin’s mechanism for managing background tasks and handling asynchronous data streams, essential for database operations.

- **Hilt / Dagger (Optional)**  
  A dependency injection framework that helps manage dependencies and maintain a clean architecture.

---

## Step-by-Step Implementation Overview

A typical CRUD application built with these technologies follows a **layered architecture**.

---

## 1. Set Up Your Project

1. Start a new Android Studio project and select the **Empty Activity** template  
   - Defaults to **Kotlin** and **Jetpack Compose**.

2. Add the required dependencies in `build.gradle.kts`:
   - Jetpack Compose
   - Room
   - ViewModel
   - Coroutines (and Flow)

---

## 2. Define the Data Layer

### 2.1 Entity
- Create a Kotlin **data class** representing your data model.
- Annotate it with `@Entity` to define a database table.

### 2.2 DAO (Data Access Object)
- Create an interface annotated with `@Dao`.
- Define CRUD operations using:
  - `@Insert`
  - `@Query`
  - `@Update`
  - `@Delete`

### 2.3 Database
- Create an abstract class extending `RoomDatabase`.
- Link **Entities** and **DAOs**.
- Use a `companion object` to provide a **singleton** database instance via `Room.databaseBuilder`.

---

## 3. Implement the Domain / Repository Layer

- Create a **Repository** class that abstracts data sources (Room DAO or remote APIs).
- This layer:
  - Handles business and data logic
  - Exposes data as `Flow` streams for asynchronous observation

---

## 4. Create the ViewModel

- Acts as the bridge between **UI** and **data layers**.
- Responsibilities:
  - Holds UI state
  - Executes business logic
- Uses **Coroutines** to perform background operations.
- Exposes data via:
  - `StateFlow` (recommended)
  - or `LiveData`

---

## 5. Build the UI with Jetpack Compose

### 5.1 Composable Functions
- Design the UI using `@Composable` functions.

### 5.2 UI Components
- Common composables include:
  - `TextField`
  - `Button`
  - `LazyColumn`
  - `Card`, `Scaffold`, etc.

### 5.3 State Observation
- The UI observes state from the **ViewModel**.
- Automatically updates when data changes.
- Follows **unidirectional data flow**.

### 5.4 Best Practice
- **Do not place business logic in composables**.
- Composables should only:
  - Read state
  - Display UI
  - Forward user events to the ViewModel

---

## Summary

Using **Kotlin + Jetpack Compose + MVVM + Room + Coroutines** provides a modern, scalable, and maintainable foundation for Android CRUD applications.  
This architecture ensures clean separation of concerns, responsive UI updates, and efficient data handling.
```


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
    id("org.jetbrains.kotlin.plugin.compose")
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

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    // Core Android
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.9.0")

    // Compose BOM
    val composeBom = platform("androidx.compose:compose-bom:2024.02.00")
    implementation(composeBom)
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")

    // Material Components (for themes compatibility)
    implementation("com.google.android.material:material:1.11.0")

    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")

    // Room Database
    implementation("androidx.room:room-runtime:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // Splash Screen
    implementation("androidx.core:core-splashscreen:1.0.1")

    // Icons
    implementation("androidx.compose.material:material-icons-extended:1.6.0")
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
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class StudentViewModel(private val repository: StudentRepository) : ViewModel() {

    // Original list from database
    private val _students = MutableStateFlow<List<Student>>(emptyList())

    // Search query state
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    // Filtered students based on search query
    val students: StateFlow<List<Student>> = _students.combine(_searchQuery) { studentList, query ->
        if (query.isBlank()) {
            // If search is empty, return all students
            studentList
        } else {
            // Filter students by name or course (case-insensitive)
            studentList.filter { student ->
                student.name.contains(query, ignoreCase = true) ||
                        student.course.contains(query, ignoreCase = true)
            }
        }
    }.stateFlow(viewModelScope, emptyList())

    init {
        // Collect all students from database
        viewModelScope.launch {
            repository.allStudents.collect {
                _students.value = it
            }
        }
    }

    /**
     * Update search query
     * Automatically triggers filtering in students StateFlow
     */
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    /**
     * Clear search query and show all students
     */
    fun clearSearch() {
        _searchQuery.value = ""
    }

    /**
     * Add a new student to database
     */
    fun addStudent(student: Student) = viewModelScope.launch {
        repository.insert(student)
    }

    /**
     * Update existing student in database
     */
    fun updateStudent(student: Student) = viewModelScope.launch {
        repository.update(student)
    }

    /**
     * Delete student from database
     */
    fun deleteStudent(student: Student) = viewModelScope.launch {
        repository.delete(student)
    }
}

// Extension function to convert Flow to StateFlow with initial value
private fun <T> kotlinx.coroutines.flow.Flow<T>.stateFlow(
    scope: kotlinx.coroutines.CoroutineScope,
    initialValue: T
): StateFlow<T> {
    val stateFlow = MutableStateFlow(initialValue)
    scope.launch {
        collect { stateFlow.value = it }
    }
    return stateFlow
}
```

---

## Step 5: Build UI with Jetpack Compose

```kotlin
package com.example.studentcrudapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.studentcrudapp.data.entity.Student
import com.example.studentcrudapp.viewmodel.StudentViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentListScreen(viewModel: StudentViewModel) {
    val students by viewModel.students.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    var showAddDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var selectedStudent by remember { mutableStateOf<Student?>(null) }

    // Focus manager for keyboard handling
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "Student Manager",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "${students.size} students",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showAddDialog = true },
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("Add Student") },
                containerColor = MaterialTheme.colorScheme.primary
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Search Bar Section
            SearchBar(
                query = searchQuery,
                onQueryChange = { viewModel.updateSearchQuery(it) },
                onClearClick = {
                    viewModel.clearSearch()
                    focusManager.clearFocus()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            )

            // Students List Section
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                if (students.isEmpty()) {
                    // Empty state - shows when no students match search or database is empty
                    EmptyState(
                        isSearching = searchQuery.isNotBlank(),
                        onClearSearch = {
                            viewModel.clearSearch()
                            focusManager.clearFocus()
                        }
                    )
                } else {
                    // Show filtered students list
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(students, key = { it.id }) { student ->
                            StudentCard(
                                student = student,
                                searchQuery = searchQuery, // Pass search query to highlight matches
                                onEdit = {
                                    selectedStudent = student
                                    showEditDialog = true
                                },
                                onDelete = {
                                    selectedStudent = student
                                    showDeleteDialog = true
                                }
                            )
                        }
                        item {
                            Spacer(modifier = Modifier.height(80.dp))
                        }
                    }
                }
            }
        }

        // Add Student Dialog
        if (showAddDialog) {
            StudentDialog(
                title = "Add New Student",
                student = null,
                onDismiss = { showAddDialog = false },
                onSave = { name, course ->
                    val newStudent = Student(name = name, course = course)
                    viewModel.addStudent(newStudent)
                    showAddDialog = false
                }
            )
        }

        // Edit Student Dialog
        if (showEditDialog && selectedStudent != null) {
            StudentDialog(
                title = "Edit Student",
                student = selectedStudent,
                onDismiss = {
                    showEditDialog = false
                    selectedStudent = null
                },
                onSave = { name, course ->
                    viewModel.updateStudent(
                        selectedStudent!!.copy(
                            name = name,
                            course = course
                        )
                    )
                    showEditDialog = false
                    selectedStudent = null
                }
            )
        }

        // Delete Confirmation Dialog
        if (showDeleteDialog && selectedStudent != null) {
            AlertDialog(
                onDismissRequest = {
                    showDeleteDialog = false
                    selectedStudent = null
                },
                icon = {
                    Icon(
                        Icons.Default.Warning,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error
                    )
                },
                title = { Text("Delete Student?") },
                text = {
                    Text("Are you sure you want to delete ${selectedStudent?.name}? This action cannot be undone.")
                },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.deleteStudent(selectedStudent!!)
                            showDeleteDialog = false
                            selectedStudent = null
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text("Delete")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showDeleteDialog = false
                            selectedStudent = null
                        }
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

/**
 * Search Bar Component
 * Allows users to filter students by name or course
 */
@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onClearClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier,
        placeholder = { Text("Search by name or course...") },
        leadingIcon = {
            Icon(
                Icons.Default.Search,
                contentDescription = "Search",
                tint = MaterialTheme.colorScheme.primary
            )
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = onClearClick) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Clear search",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(28.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
        ),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                // Hide keyboard when search button is pressed
                focusManager.clearFocus()
            }
        )
    )
}

/**
 * Empty State Component
 * Shows different messages based on whether user is searching or database is empty
 */
@Composable
fun EmptyState(
    isSearching: Boolean,
    onClearSearch: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            if (isSearching) Icons.Default.SearchOff else Icons.Default.School,
            contentDescription = null,
            modifier = Modifier.size(120.dp),
            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            if (isSearching) "No Results Found" else "No Students Yet",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            if (isSearching) "Try different search terms" else "Click the + button below to add your first student",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )

        // Show clear search button when searching
        if (isSearching) {
            Spacer(modifier = Modifier.height(16.dp))
            TextButton(onClick = onClearSearch) {
                Icon(Icons.Default.Close, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text("Clear Search")
            }
        }
    }
}

/**
 * Student Card Component
 * Displays individual student information
 */
@Composable
fun StudentCard(
    student: Student,
    searchQuery: String,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = student.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "ID: ${student.id}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    }
                }

                Row {
                    IconButton(
                        onClick = onEdit,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Edit",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    Icons.Default.Book,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Course: ",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                Text(
                    text = student.course,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

/**
 * Student Dialog Component
 * Used for both adding and editing students
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentDialog(
    title: String,
    student: Student?,
    onDismiss: () -> Unit,
    onSave: (String, String) -> Unit
) {
    var name by remember { mutableStateOf(student?.name ?: "") }
    var course by remember { mutableStateOf(student?.course ?: "") }

    val isValid = name.isNotBlank() && course.isNotBlank()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    if (student == null) Icons.Default.PersonAdd else Icons.Default.Edit,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(title)
            }
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name *") },
                    leadingIcon = { Icon(Icons.Default.Person, null) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    isError = name.isBlank()
                )
                OutlinedTextField(
                    value = course,
                    onValueChange = { course = it },
                    label = { Text("Course *") },
                    leadingIcon = { Icon(Icons.Default.Book, null) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    isError = course.isBlank()
                )
                if (!isValid) {
                    Text(
                        "* Required fields",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (isValid) {
                        onSave(name.trim(), course.trim())
                    }
                },
                enabled = isValid
            ) {
                Icon(Icons.Default.Check, null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Icon(Icons.Default.Close, null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Cancel")
            }
        }
    )
}
```

---

## Step 6: Connect Everything in MainActivity

```kotlin
package com.example.studentcrudapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studentcrudapp.data.database.AppDatabase
import com.example.studentcrudapp.data.repository.StudentRepository
import com.example.studentcrudapp.ui.screens.StudentListScreen
import com.example.studentcrudapp.viewmodel.StudentViewModel
import com.example.studentcrudapp.viewmodel.StudentViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // Install splash screen BEFORE super.onCreate()
        installSplashScreen()

        super.onCreate(savedInstanceState)

        // Initialize database, repository, and factory
        val database = AppDatabase.getDatabase(this)
        val dao = database.studentDao()
        val repository = StudentRepository(dao)
        val factory = StudentViewModelFactory(repository)

        setContent {
            MaterialTheme {
                Surface {
                    val studentViewModel: StudentViewModel = viewModel(factory = factory)
                    StudentListScreen(studentViewModel)
                }
            }
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




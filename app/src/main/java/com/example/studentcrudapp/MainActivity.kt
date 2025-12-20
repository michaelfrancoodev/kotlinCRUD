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
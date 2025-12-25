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
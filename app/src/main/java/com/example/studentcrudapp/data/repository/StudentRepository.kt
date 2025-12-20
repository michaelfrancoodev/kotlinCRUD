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
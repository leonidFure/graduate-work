package com.lgorev.ksuonlineeducation.service

import com.lgorev.ksuonlineeducation.repository.teacher.TeacherRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class TeacherService(private val teacherRepository: TeacherRepository) {
    fun existTeacherById(id: UUID) = teacherRepository.existsById(id)
}
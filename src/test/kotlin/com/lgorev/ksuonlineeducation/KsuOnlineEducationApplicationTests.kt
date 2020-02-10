package com.lgorev.ksuonlineeducation

import com.lgorev.ksuonlineeducation.domain.user.Role
import com.lgorev.ksuonlineeducation.repository.faculty.FacultyRepository
import com.lgorev.ksuonlineeducation.repository.teacher.TeacherEntity
import com.lgorev.ksuonlineeducation.repository.teacher.TeacherRepository
import com.lgorev.ksuonlineeducation.repository.user.UserRepository
import com.lgorev.ksuonlineeducation.service.AuthService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import java.time.LocalDate
import java.util.*

@SpringBootTest
class KsuOnlineEducationApplicationTests {

    @Autowired
    lateinit var authService: AuthService

    @Autowired
    lateinit var teacherRepository: TeacherRepository

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var facultyRepository: FacultyRepository

    @Test
    fun contextLoads() {
        print(Role.values().toList())
    }

    @Test
    fun testTeacherInsert() {
        val teacher = TeacherEntity(UUID.fromString("99bfa41e-c0b6-4952-80e2-5c83aac011ce"), LocalDate.now(), "test")
        teacherRepository.save(teacher)
    }

    @Test
    fun testTeacherSelect() {
        teacherRepository.findByIdOrNull(UUID.fromString("99bfa41e-c0b6-4952-80e2-5c83aac011ce"))?.let {
            print(it)
        }
    }

    @Test
    fun test() {
        userRepository.findByIdOrNull(UUID.fromString("99bfa41e-c0b6-4952-80e2-5c83aac011ce"))?.let {
            print(it)
        }
    }

}

package com.lgorev.ksuonlineeducation

import com.lgorev.ksuonlineeducation.domain.user.Gender
import com.lgorev.ksuonlineeducation.domain.user.Role
import com.lgorev.ksuonlineeducation.repository.department.DepartmentEntity
import com.lgorev.ksuonlineeducation.repository.department.DepartmentRepository
import com.lgorev.ksuonlineeducation.repository.faculty.FacultyEntity
import com.lgorev.ksuonlineeducation.repository.faculty.FacultyRepository
import com.lgorev.ksuonlineeducation.repository.teacher.TeacherEntity
import com.lgorev.ksuonlineeducation.repository.teacher.TeacherRepository
import com.lgorev.ksuonlineeducation.repository.user.UserEntity
import com.lgorev.ksuonlineeducation.repository.user.UserRepository
import com.lgorev.ksuonlineeducation.service.AuthService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.bcrypt.BCrypt
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.Month
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

    @Autowired
    lateinit var departmentRepository: DepartmentRepository

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

    @Test
    fun addNewFaculty() {
    }

    @Test
    fun generateAdminUUID() {
        userRepository.save(UserEntity(UUID(0L,0L), "Admin", "Admin", "Admin", Gender.MALE, "ksu-admin@gmail.com", BCrypt.hashpw("admin123", BCrypt.gensalt(12))))
    }

}

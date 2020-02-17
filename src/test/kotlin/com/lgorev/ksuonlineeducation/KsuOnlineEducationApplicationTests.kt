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

    @Test
    fun testDate() {
        val startDate = LocalDate.now()
        val endDate = startDate.plusMonths(2)
        println(startDate)
        println("=============================")
        println(endDate)
    }

}

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
import java.time.temporal.Temporal
import java.time.temporal.TemporalAdjuster
import java.util.*
import com.lgorev.ksuonlineeducation.Aaa as Aaa

@SpringBootTest
class KsuOnlineEducationApplicationTests {

    @Test
    fun testDate() {
        println(1 in listOf(1,2,3,4,5,6,7))
        println(9 in listOf(1,2,3,4,5,6,7))
    }

    @Test
    fun `САНЯ ПОШЕЛ НАХУЙ`() {
        val params = mutableMapOf(
                "ads1" to "asdas",
                "ads2" to "asdas",
                "ads3" to "asdas",
                "ads4" to "asdas",
                "ads5" to "asdas",
                "ads6" to "asdas"
        )
        println(params.map { "${it.key}=${it.value}" }.joinToString("&"))
    }

}
@FunctionalInterface
interface  Aaa {
    fun a(a: () -> String){}
}

package com.lgorev.ksuonlineeducation

import com.lgorev.ksuonlineeducation.service.CourseReviewService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

import java.util.*

@SpringBootTest
class KsuOnlineEducationApplicationTests {

    @Autowired
    lateinit var courseReviewService: CourseReviewService

    @Test
    fun testik() {
        val coursesRating = courseReviewService.getCoursesRating(
                mutableSetOf(
                        UUID.fromString("4a3c43a1-0a0f-433a-bfd7-8bb4656cef71"),
                        UUID.fromString("7c3ee021-4c00-4683-9cd7-2fbc316c45f1")
                )
        )
        coursesRating.forEach { println(it) }
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
interface Aaa {
    fun a(a: () -> String) {}
}

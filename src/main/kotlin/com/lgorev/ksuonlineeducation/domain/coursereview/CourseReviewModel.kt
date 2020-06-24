package com.lgorev.ksuonlineeducation.domain.coursereview

import com.lgorev.ksuonlineeducation.domain.user.UserResponseModel
import java.util.*
import javax.validation.constraints.Max
import javax.validation.constraints.Min

data class CourseReviewModel(
        val courseId: UUID,
        var userId: UUID? = null,
        @Min(
                value = 1,
                message = "Рэйтинг курса не может быть меньше единицы"
        )
        @Max(
                value = 5,
                message = "Рэйтинг курса не может быть больше пяти"
        )
        val rating: Int,
        val commentHead: String = "",
        val commentBody: String = "",
        val user: UserResponseModel? = null
)
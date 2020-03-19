package com.lgorev.ksuonlineeducation.domain.course

import java.util.*

data class CoursesTeachersRequestModel(
        val courseId: UUID,
        val teacherId: UUID
)
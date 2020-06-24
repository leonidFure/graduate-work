package com.lgorev.ksuonlineeducation.domain.course

import java.util.*

data class CourseSubscriptionModel (
        val courseId: UUID,
        var userId: UUID? = null
)

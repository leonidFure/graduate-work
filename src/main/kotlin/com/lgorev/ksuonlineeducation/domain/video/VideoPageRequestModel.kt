package com.lgorev.ksuonlineeducation.domain.video

import java.util.*

data class VideoPageRequestModel(
        val pageNum: Int,
        val pageSize: Int,
        val userId: UUID? = null,
        val courseId: UUID? = null
)
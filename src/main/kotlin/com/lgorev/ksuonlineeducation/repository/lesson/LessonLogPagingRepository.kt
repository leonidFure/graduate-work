package com.lgorev.ksuonlineeducation.repository.lesson

import com.lgorev.ksuonlineeducation.domain.lesson.LessonLogPageRequestModel
import org.springframework.data.domain.Page

interface LessonLogPagingRepository {
    fun getPage(model: LessonLogPageRequestModel): Page<LessonLogEntity>
}
package com.lgorev.ksuonlineeducation.repository.lesson

import com.lgorev.ksuonlineeducation.domain.lesson.LessonRequestPageModel
import org.springframework.data.domain.Page
import org.springframework.stereotype.Repository

@Repository
interface LessonPagingRepository {
    fun findLessonPage(model: LessonRequestPageModel): Page<LessonEntity>
}
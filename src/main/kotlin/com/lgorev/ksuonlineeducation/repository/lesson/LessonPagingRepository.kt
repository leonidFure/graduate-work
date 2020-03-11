package com.lgorev.ksuonlineeducation.repository.lesson

import com.lgorev.ksuonlineeducation.domain.common.PageResponseModel
import com.lgorev.ksuonlineeducation.domain.lesson.LessonRequestPageModel
import org.springframework.data.domain.Page
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface LessonPagingRepository {
    fun findLessonPage(model: LessonRequestPageModel, ids: MutableSet<UUID>?): PageResponseModel<LessonEntity>
}
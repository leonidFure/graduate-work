package com.lgorev.ksuonlineeducation.repository.course

import com.lgorev.ksuonlineeducation.domain.course.CourseRequestPageModel
import org.springframework.data.domain.Page
import org.springframework.stereotype.Repository

@Repository
interface CoursePagingRepository {
    fun findPage(model: CourseRequestPageModel): Page<CourseEntity>
}
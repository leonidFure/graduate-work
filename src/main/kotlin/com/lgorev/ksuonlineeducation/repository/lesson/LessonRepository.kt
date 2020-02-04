package com.lgorev.ksuonlineeducation.repository.lesson

import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface LessonRepository : PagingAndSortingRepository<LessonEntity, UUID>
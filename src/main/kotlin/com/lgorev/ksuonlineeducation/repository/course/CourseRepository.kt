package com.lgorev.ksuonlineeducation.repository.course

import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface CourseRepository : PagingAndSortingRepository<CourseEntity, UUID>
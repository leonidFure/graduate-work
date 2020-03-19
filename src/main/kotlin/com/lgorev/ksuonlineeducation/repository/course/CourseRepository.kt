package com.lgorev.ksuonlineeducation.repository.course

import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface CourseRepository : CrudRepository<CourseEntity, UUID>, CoursePagingRepository
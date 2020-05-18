package com.lgorev.ksuonlineeducation.repository.course

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface CourseRepository : CrudRepository<CourseEntity, UUID>, CoursePagingRepository {

    @Query("SELECT ep.subjectId FROM CourseEntity e LEFT JOIN EducationProgramEntity ep ON ep.id = e.educationProgramId where e.id = :id")
    fun getSubjectIdById(@Param("id") id: UUID): UUID?

}
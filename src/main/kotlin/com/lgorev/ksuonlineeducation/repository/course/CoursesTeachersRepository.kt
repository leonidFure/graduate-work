package com.lgorev.ksuonlineeducation.repository.course

import com.lgorev.ksuonlineeducation.repository.lesson.LessonsThemesEntity
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface CoursesTeachersRepository : CrudRepository<CoursesTeachersEntity, CoursesTeachersId> {

    @Query("SELECT e FROM CoursesTeachersEntity e WHERE e.coursesTeachersId.courseId = :courseId")
    fun findByCourseId(@Param("courseId") id: UUID): MutableSet<CoursesTeachersEntity>

    @Query("SELECT e FROM CoursesTeachersEntity e WHERE e.coursesTeachersId.teacherId = :teacherId")
    fun findByTeacherId(@Param("teacherId") id: UUID): MutableSet<CoursesTeachersEntity>

}
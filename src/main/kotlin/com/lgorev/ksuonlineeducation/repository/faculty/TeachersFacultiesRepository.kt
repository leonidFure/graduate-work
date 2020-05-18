package com.lgorev.ksuonlineeducation.repository.faculty

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.*

interface TeachersFacultiesRepository : JpaRepository<TeachersFacultiesEntity, TeachersFacultiesId> {
    @Query("SELECT e FROM TeachersFacultiesEntity e where e.teachersFacultiesId.teacherId = :teacherId")
    fun findByTeacherId(@Param("teacherId") id: UUID): MutableSet<TeachersFacultiesEntity>
}
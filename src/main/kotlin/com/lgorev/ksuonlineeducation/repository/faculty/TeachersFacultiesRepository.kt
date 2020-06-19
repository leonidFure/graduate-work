package com.lgorev.ksuonlineeducation.repository.faculty

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface TeachersFacultiesRepository : JpaRepository<TeachersFacultiesEntity, TeachersFacultiesId> {
    @Query("SELECT e FROM TeachersFacultiesEntity e where e.teachersFacultiesId.teacherId = :teacherId")
    fun findByTeacherId(@Param("teacherId") id: UUID): MutableSet<TeachersFacultiesEntity>

    @Query("SELECT e FROM TeachersFacultiesEntity e where e.teachersFacultiesId.teacherId in :teacherIds")
    fun findByTeacherIds(@Param("teacherIds") ids: MutableSet<UUID>): MutableSet<TeachersFacultiesEntity>

    @Query("SELECT e FROM TeachersFacultiesEntity e where e.teachersFacultiesId.facultyId in :facultyIds")
    fun findByFacultyIds(@Param("facultyIds") ids: MutableSet<UUID>): MutableSet<TeachersFacultiesEntity>

    @Modifying
    @Query("DELETE FROM TeachersFacultiesEntity e where e.teachersFacultiesId.teacherId = :teacherId")
    fun deleteAllByTeacherId(@Param("teacherId") id: UUID)
}
package com.lgorev.ksuonlineeducation.repository.educationprogram

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface TeachersEducationProgramsRepository :
        CrudRepository<TeachersEducationProgramsEntity, TeachersEducationProgramsId> {
    @Query("SELECT e FROM TeachersEducationProgramsEntity e WHERE e.teachersEducationProgramsId.teacherId = :teacherId")
    fun findAllByTeacherId(@Param("teacherId") id: UUID): MutableSet<TeachersEducationProgramsEntity>

}
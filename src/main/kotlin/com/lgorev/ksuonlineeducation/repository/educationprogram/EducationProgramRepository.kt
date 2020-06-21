package com.lgorev.ksuonlineeducation.repository.educationprogram

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface EducationProgramRepository : CrudRepository<EducationProgramEntity, UUID> , EducationProgramPagingRepository{
    fun existsByName(name: String): Boolean
    fun findByName(name: String): EducationProgramEntity?
    fun findAllByNameContainingIgnoreCase(name: String): MutableSet<EducationProgramEntity>
    fun findAllByIdIn(ids: MutableSet<UUID>): MutableSet<EducationProgramEntity>
    fun findAllBySubjectId(id: UUID): MutableSet<EducationProgramEntity>
}
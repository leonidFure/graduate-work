package com.lgorev.ksuonlineeducation.repository.trainingdirection

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import java.util.*

interface SubjectForEntranceRepository : CrudRepository<SubjectsForEntranceEntity, SubjectsForEntranceId> {
    @Query("SELECT e FROM SubjectsForEntranceEntity e WHERE e.subjectsForEntranceId.trainingDirectionId in :directionIds")
    fun findByDirectionIds(@Param("directionIds") ids: MutableSet<UUID>): MutableSet<SubjectsForEntranceEntity>

    @Query("SELECT e FROM SubjectsForEntranceEntity e WHERE e.subjectsForEntranceId.subjectId in :subjectIds")
    fun findBySubjectIds(@Param("subjectIds") ids: MutableSet<UUID>): MutableSet<SubjectsForEntranceEntity>
}
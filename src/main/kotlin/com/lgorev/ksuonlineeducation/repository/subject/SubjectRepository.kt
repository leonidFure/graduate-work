package com.lgorev.ksuonlineeducation.repository.subject

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface SubjectRepository : PagingAndSortingRepository<SubjectEntity, UUID>, SubjectPagingRepository {
    fun existsByName(name: String): Boolean
    fun findByName(name: String): SubjectEntity?
    fun existsByIdIn(ids: MutableSet<UUID>): Boolean
    fun findByIdIn(@Param("ids") ids: MutableSet<UUID>): MutableSet<SubjectEntity>
}
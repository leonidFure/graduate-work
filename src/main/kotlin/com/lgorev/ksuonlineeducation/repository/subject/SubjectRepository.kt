package com.lgorev.ksuonlineeducation.repository.subject

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface SubjectRepository : PagingAndSortingRepository<SubjectEntity, UUID>, SubjectPagingRepository {
    fun existsByName(name: String): Boolean
    fun findByName(name: String): SubjectEntity?
    fun existsByIdIn(ids: MutableSet<UUID>):Boolean
}
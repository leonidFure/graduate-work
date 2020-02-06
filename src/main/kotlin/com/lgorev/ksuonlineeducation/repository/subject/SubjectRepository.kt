package com.lgorev.ksuonlineeducation.repository.subject

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface SubjectRepository : PagingAndSortingRepository<SubjectEntity, UUID>, SubjectPagingRepository {
    fun findAllByNameContainingIgnoreCase(pageable: Pageable, name: String): Page<SubjectEntity>
}
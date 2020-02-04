package com.lgorev.ksuonlineeducation.repository.faculty

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface FacultyRepository : PagingAndSortingRepository<FacultyEntity, UUID> {
    fun findByName(name: String): FacultyEntity?
    fun existsByManagerId(managerId: UUID): Boolean
    fun findByManagerId(managerId: UUID): FacultyEntity?
    fun existsByName(name: String): Boolean
    fun findAllByNameContainingIgnoreCase(pageable: Pageable, name: String): Page<FacultyEntity>
}
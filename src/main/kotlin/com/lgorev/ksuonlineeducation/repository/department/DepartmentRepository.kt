package com.lgorev.ksuonlineeducation.repository.department

import com.lgorev.ksuonlineeducation.repository.faculty.FacultyEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface DepartmentRepository : PagingAndSortingRepository<DepartmentEntity, UUID>{
    fun findByName(name: String): DepartmentEntity?
    fun existsByName(name: String): Boolean
    fun existsByManagerId(managerId: UUID): Boolean
    fun findByManagerId(managerId: UUID): DepartmentEntity?
    fun findAllByNameContainingIgnoreCase(pageable: Pageable, name: String): Page<DepartmentEntity>
}
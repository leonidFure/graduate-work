package com.lgorev.ksuonlineeducation.repository.department

import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface DepartmentRepository : PagingAndSortingRepository<DepartmentEntity, UUID>{
    fun findByName(name: String): DepartmentEntity?
}
package com.lgorev.ksuonlineeducation.repository.faculty

import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface FacultyRepository : PagingAndSortingRepository<FacultyEntity, UUID> {
    fun findByName(name: String): FacultyEntity?
}
package com.lgorev.ksuonlineeducation.repository.trainingdirection

import com.lgorev.ksuonlineeducation.repository.department.DepartmentEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface TrainingDirectionRepository : PagingAndSortingRepository<TrainingDirectionEntity, UUID> {
    fun existsByName(name: String): Boolean
    fun findByName(name: String): TrainingDirectionEntity?
    fun findAllByNameContainingIgnoreCase(pageable: Pageable, name: String): Page<TrainingDirectionEntity>

}
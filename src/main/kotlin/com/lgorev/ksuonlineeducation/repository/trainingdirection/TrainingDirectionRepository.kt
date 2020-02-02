package com.lgorev.ksuonlineeducation.repository.trainingdirection

import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface TrainingDirectionRepository : PagingAndSortingRepository<TrainingDirectionEntity, UUID> {
    fun findByName(name: String): TrainingDirectionEntity?
}
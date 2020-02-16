package com.lgorev.ksuonlineeducation.repository.trainingdirection

import com.lgorev.ksuonlineeducation.domain.trainingdirection.TrainingDirectionPageRequestModel
import org.springframework.data.domain.Page
import org.springframework.stereotype.Repository

@Repository
interface TrainingDirectionPagingRepository {
    fun findPage(model: TrainingDirectionPageRequestModel, ids: MutableSet<SubjectsForEntranceEntity>): Page<TrainingDirectionEntity>
}
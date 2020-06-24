package com.lgorev.ksuonlineeducation.repository.trainingdirection

import com.lgorev.ksuonlineeducation.domain.common.PageResponseModel
import com.lgorev.ksuonlineeducation.domain.trainingdirection.TrainingDirectionPageRequestModel
import org.springframework.data.domain.Page
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface TrainingDirectionPagingRepository {
    fun findPage(model: TrainingDirectionPageRequestModel, ids: MutableSet<UUID>?): PageResponseModel<TrainingDirectionEntity>
}
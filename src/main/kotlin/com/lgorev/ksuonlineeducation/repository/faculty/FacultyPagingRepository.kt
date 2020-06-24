package com.lgorev.ksuonlineeducation.repository.faculty

import com.lgorev.ksuonlineeducation.domain.common.PageResponseModel
import com.lgorev.ksuonlineeducation.domain.faculty.FacultyPageRequestModel
import org.springframework.stereotype.Repository

@Repository
interface FacultyPagingRepository {
    fun findPage(model: FacultyPageRequestModel) : PageResponseModel<FacultyEntity>
}

package com.lgorev.ksuonlineeducation.repository.educationprogram

import com.lgorev.ksuonlineeducation.domain.educationprogram.EducationProgramRequestPageModel
import org.springframework.data.domain.Page
import org.springframework.stereotype.Repository

@Repository
interface EducationProgramPagingRepository {
    fun findPage(model: EducationProgramRequestPageModel): Page<EducationProgramEntity>
}
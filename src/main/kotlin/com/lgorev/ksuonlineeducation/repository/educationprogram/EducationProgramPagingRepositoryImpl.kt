package com.lgorev.ksuonlineeducation.repository.educationprogram

import com.lgorev.ksuonlineeducation.domain.educationprogram.EducationProgramRequestPageModel
import org.springframework.data.domain.Page

class EducationProgramPagingRepositoryImpl : EducationProgramPagingRepository {
    override fun findPage(model: EducationProgramRequestPageModel): Page<EducationProgramEntity> {
        TODO("прописать пагинацию и фильтрацию, продумать фильтрацию по полям смежных сущьностей")
    }
}
package com.lgorev.ksuonlineeducation.repository.subject

import com.lgorev.ksuonlineeducation.domain.subject.SubjectRequestPageModel
import org.springframework.data.domain.Page
import org.springframework.stereotype.Repository

@Repository
interface SubjectPagingRepository {
    fun findPage(model: SubjectRequestPageModel): Page<SubjectEntity>
}
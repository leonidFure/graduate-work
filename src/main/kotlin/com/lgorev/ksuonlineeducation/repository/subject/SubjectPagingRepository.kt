package com.lgorev.ksuonlineeducation.repository.subject

import com.lgorev.ksuonlineeducation.domain.subject.SubjectRequestPageModel
import com.lgorev.ksuonlineeducation.repository.trainingdirection.SubjectsForEntranceEntity
import org.springframework.data.domain.Page
import org.springframework.stereotype.Repository

@Repository
interface SubjectPagingRepository {
    fun findPage(model: SubjectRequestPageModel, subjectIds: MutableSet<SubjectsForEntranceEntity>): Page<SubjectEntity>
}
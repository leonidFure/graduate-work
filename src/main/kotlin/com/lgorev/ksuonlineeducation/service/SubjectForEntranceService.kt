package com.lgorev.ksuonlineeducation.service

import com.lgorev.ksuonlineeducation.repository.trainingdirection.SubjectForEntranceRepository
import com.lgorev.ksuonlineeducation.repository.trainingdirection.SubjectsForEntranceEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional(isolation = Isolation.READ_UNCOMMITTED)
class SubjectForEntranceService(private val subjectForEntranceRepository: SubjectForEntranceRepository) {

    fun getSubjectForEntranceByDirectionIds(ids: MutableSet<UUID>) =
            subjectForEntranceRepository.findByDirectionIds(ids)

    fun saveAll(subjectsForEntrance: MutableSet<SubjectsForEntranceEntity>) {
        subjectForEntranceRepository.saveAll(subjectsForEntrance)
    }
}
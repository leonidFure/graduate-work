package com.lgorev.ksuonlineeducation.repository.subject

import com.lgorev.ksuonlineeducation.domain.subject.SubjectRequestPageModel
import com.lgorev.ksuonlineeducation.domain.subject.SubjectType
import com.lgorev.ksuonlineeducation.repository.trainingdirection.SubjectsForEntranceEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Sort
import java.util.*
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.persistence.criteria.Predicate

class SubjectPagingRepositoryImpl(@PersistenceContext private val em: EntityManager) : SubjectPagingRepository {

    private val subject = SubjectEntity::class.java

    override fun findPage(model: SubjectRequestPageModel, subjectIds: MutableSet<SubjectsForEntranceEntity>): Page<SubjectEntity> {
        val cb = em.criteriaBuilder
        val cq = cb.createQuery(subject)
        val root = cq.from(subject)

        val predicates = mutableSetOf<Predicate>()
        if (model.nameFilter != null)
            predicates.add(cb.like(cb.upper(root.get<String>("name")), "%${model.nameFilter.toUpperCase()}%"))
        if (model.typeFilter != null)
            predicates.add(cb.equal(root.get<SubjectType>("type"), model.typeFilter))
        if(subjectIds.isNotEmpty())
            predicates.add(root.get<UUID>("id").`in`(subjectIds.map { it.subjectsForEntranceId.subjectId }))

        cq.where(cb.and(*predicates.toTypedArray()))
        if (model.sortType == Sort.Direction.DESC)
            cq.orderBy(cb.desc(root.get<String>("name")))
        else
            cq.orderBy(cb.asc(root.get<String>("name")))

        val typedQuery = em.createQuery(cq)
        typedQuery.firstResult = (model.pageNum) * model.pageSize
        typedQuery.maxResults = model.pageSize

        return PageImpl<SubjectEntity>(typedQuery.resultList)
    }
}
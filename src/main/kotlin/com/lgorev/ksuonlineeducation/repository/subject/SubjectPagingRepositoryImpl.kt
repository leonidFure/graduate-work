package com.lgorev.ksuonlineeducation.repository.subject

import com.lgorev.ksuonlineeducation.domain.common.PageResponseModel
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

    override fun findPage(model: SubjectRequestPageModel, ids: MutableSet<UUID>?): PageResponseModel<SubjectEntity> {
        val cb = em.criteriaBuilder
        val cq = cb.createQuery(subject)
        val root = cq.from(subject)
        val countQuery = cb.createQuery(Long::class.java)

        val predicates = mutableSetOf<Predicate>()
        if (model.nameFilter != null)
            predicates.add(cb.like(cb.upper(root.get<String>("name")), "%${model.nameFilter.toUpperCase()}%"))
        if (model.typeFilter != null)
            predicates.add(cb.equal(root.get<SubjectType>("type"), model.typeFilter))
        if(ids != null)
            predicates.add(root.get<UUID>("id").`in`(ids))

        if(model.ids != null)
            predicates.add(root.get<UUID>("id").`in`(ids))

        cq.where(cb.and(*predicates.toTypedArray()))
        if (model.sortType == Sort.Direction.DESC)
            cq.orderBy(cb.desc(root.get<String>("name")))
        else
            cq.orderBy(cb.asc(root.get<String>("name")))
        countQuery.select(cb.count(countQuery.from(subject)))
        countQuery.where(cb.and(*predicates.toTypedArray()))
        val typedQuery = em.createQuery(cq)
        typedQuery.firstResult = (model.pageNum) * model.pageSize
        typedQuery.maxResults = model.pageSize
        val query = em.createQuery(countQuery)
        val count = query.singleResult
        val resultList = typedQuery.resultList
        return PageResponseModel(resultList.toMutableSet(), count ?: 0)
    }
}
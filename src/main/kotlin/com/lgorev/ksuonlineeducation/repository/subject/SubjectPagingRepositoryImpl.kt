package com.lgorev.ksuonlineeducation.repository.subject

import com.lgorev.ksuonlineeducation.domain.subject.SubjectRequestPageModel
import com.lgorev.ksuonlineeducation.domain.subject.SubjectType
import org.hibernate.criterion.Order
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Sort
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.persistence.criteria.Predicate

class SubjectPagingRepositoryImpl(@PersistenceContext private val em: EntityManager) : SubjectPagingRepository {

    private val subject = SubjectEntity::class.java

    override fun findPage(model: SubjectRequestPageModel): Page<SubjectEntity> {
        val cb = em.criteriaBuilder
        val cq = cb.createQuery(subject)
        val root = cq.from(subject)

        val predicates = mutableSetOf<Predicate>()
        if (model.nameFilter != null)
            predicates.add(cb.like(cb.upper(root.get<String>("name")), "%${model.nameFilter.toUpperCase()}%"))
        if (model.typeFilter != null)
            predicates.add(cb.equal(root.get<SubjectType>("type"), model.typeFilter))

        cq.where(cb.and(*predicates.toTypedArray()))
        if (model.sortType == Sort.Direction.DESC)
            cq.orderBy(cb.desc(root.get<String>(model.sortField)))
        else
            cq.orderBy(cb.asc(root.get<String>(model.sortField)))

        val typedQuery = em.createQuery(cq)
        typedQuery.firstResult = (model.pageNum) * model.pageSize
        typedQuery.maxResults = model.pageSize

        return PageImpl<SubjectEntity>(typedQuery.resultList)
    }
}
package com.lgorev.ksuonlineeducation.repository.subject

import com.lgorev.ksuonlineeducation.domain.subject.SubjectRequestPageModel
import org.hibernate.criterion.Order
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Sort
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

class SubjectPagingRepositoryImpl(@PersistenceContext private val em: EntityManager) : SubjectPagingRepository {

    private val subject = SubjectEntity::class.java

    override fun findPage(model: SubjectRequestPageModel): Page<SubjectEntity> {
        val cb = em.criteriaBuilder
        val cq = cb.createQuery(subject)
        val root = cq.from(subject)

        if (model.nameFilter != null)
            cq.where(cb.equal(cb.upper(root.get<String>("name")), "%${model.nameFilter.toUpperCase()}%"))
        if (model.typeFilter != null)
            cq.where(cb.equal(root.get<String>("type"), model.typeFilter.toString()))

        if (model.sortType == Sort.Direction.DESC)
            cq.orderBy(cb.desc(root.get<String>(model.sortField)))
        else
            cq.orderBy(cb.asc(root.get<String>(model.sortField)))

        val typedQuery = em.createQuery(cq)
        typedQuery.firstResult = model.pageNum * model.pageSize - 1
        typedQuery.maxResults = model.pageSize

        return PageImpl<SubjectEntity>(typedQuery.resultList)
    }
}
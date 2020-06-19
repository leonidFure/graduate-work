package com.lgorev.ksuonlineeducation.repository.faculty

import com.lgorev.ksuonlineeducation.domain.common.PageResponseModel
import com.lgorev.ksuonlineeducation.domain.faculty.FacultyPageRequestModel
import com.lgorev.ksuonlineeducation.repository.faculty.FacultyEntity_.name
import org.springframework.data.domain.Sort
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.persistence.criteria.Predicate

class FacultyPagingRepositoryImpl(@PersistenceContext private val em: EntityManager) : FacultyPagingRepository {

    val faculty = FacultyEntity::class.java

    override fun findPage(model: FacultyPageRequestModel): PageResponseModel<FacultyEntity> {
        val cb = em.criteriaBuilder

        val cq = cb.createQuery(faculty)
        val root = cq.from(faculty)
        val countQuery = cb.createQuery(Long::class.java)
        val predicates = mutableSetOf<Predicate>()

        if(model.nameFilter != null)
            predicates.add(cb.like(cb.upper(root.get(name)), "%${model.nameFilter.toUpperCase()}%"))


        cq.where(cb.and(*predicates.toTypedArray()))
        if (model.sortType == Sort.Direction.DESC)
            cq.orderBy(cb.desc(root.get(name)))
        else
            cq.orderBy(cb.asc(root.get(name)))

        val typedQuery = em.createQuery(cq)
        countQuery.select(cb.count(countQuery.from(faculty)))
        countQuery.where(cb.and(*predicates.toTypedArray()))
        typedQuery.firstResult = (model.pageNum - 1) * model.pageSize
        typedQuery.maxResults = model.pageSize

        val query = em.createQuery(countQuery)
        val count = query.singleResult
        val resultList = typedQuery.resultList
        return PageResponseModel(resultList.toMutableSet(), count ?: 0)
    }
}
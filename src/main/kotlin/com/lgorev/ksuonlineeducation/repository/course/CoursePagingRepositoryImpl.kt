package com.lgorev.ksuonlineeducation.repository.course

import com.lgorev.ksuonlineeducation.domain.common.PageResponseModel
import com.lgorev.ksuonlineeducation.domain.course.CourseRequestPageModel
import com.lgorev.ksuonlineeducation.repository.course.CourseEntity.Fields.END_DATE
import com.lgorev.ksuonlineeducation.repository.course.CourseEntity.Fields.START_DATE
import com.lgorev.ksuonlineeducation.repository.course.CourseEntity_.*
import org.springframework.data.domain.Sort
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.persistence.criteria.Predicate

class CoursePagingRepositoryImpl(@PersistenceContext private val em: EntityManager) : CoursePagingRepository {

    private val course = CourseEntity::class.java

    override fun findPage(model: CourseRequestPageModel): PageResponseModel<CourseEntity> {
        val cb = em.criteriaBuilder

        val cq = cb.createQuery(course)
        val root = cq.from(course)
        val countQuery = cb.createQuery(Long::class.java)

        val predicates = mutableSetOf<Predicate>()
        if (model.ids != null)
            predicates.add(root.get(id).`in`(model.ids))
        if (model.actualFilter != null)
            predicates.add(cb.equal(root.get(isActual), model.actualFilter))
        if (model.statusFilter != null)
            predicates.add(cb.equal(root.get(status), model.statusFilter))
        if (model.educationProgramId != null)
            predicates.add(cb.equal(root.get(educationProgramId), model.educationProgramId))
        if (model.educationProgramIds != null)
            predicates.add(root.get(educationProgramId).`in`(model.educationProgramIds))
        if (model.dateFrom != null && model.dateTo != null) {
            predicates.add(
                    cb.or(
                            cb.between(root.get(startDate), model.dateFrom, model.dateTo),
                            cb.between(root.get(endDate), model.dateFrom, model.dateTo),
                            cb.and(
                                    cb.greaterThanOrEqualTo(root.get(endDate), model.dateTo),
                                    cb.lessThanOrEqualTo(root.get(startDate), model.dateFrom)
                            )
                    )
            )
        }

        val sortField = when (model.sortField) {
            START_DATE -> startDate
            END_DATE -> endDate
            else -> creationDateTime
        }

        cq.where(cb.and(*predicates.toTypedArray()))
        if (model.sortType == Sort.Direction.DESC)
            cq.orderBy(cb.desc(root.get(sortField)))
        else
            cq.orderBy(cb.asc(root.get(sortField)))

        val typedQuery = em.createQuery(cq)
        countQuery.select(cb.count(countQuery.from(course)))
        countQuery.where(cb.and(*predicates.toTypedArray()))
        typedQuery.firstResult = (model.pageNum - 1) * model.pageSize
        typedQuery.maxResults = model.pageSize

        val query = em.createQuery(countQuery)
        val count = query.singleResult
        val resultList = typedQuery.resultList
        return PageResponseModel(resultList.toMutableSet(), count ?: 0)
    }
}
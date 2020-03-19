package com.lgorev.ksuonlineeducation.repository.course

import com.lgorev.ksuonlineeducation.domain.common.PageResponseModel
import com.lgorev.ksuonlineeducation.domain.course.CourseRequestPageModel
import com.lgorev.ksuonlineeducation.domain.educationprogram.EducationProgramStatus
import org.springframework.data.domain.Sort
import java.time.LocalDate
import java.util.*
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
            predicates.add(root.get<UUID>("id").`in`(model.ids))
        if (model.actualFilter != null)
            predicates.add(cb.equal(root.get<Boolean>("isActual"), model.actualFilter))
        if (model.statusFilter != null)
            predicates.add(cb.equal(root.get<EducationProgramStatus>("status"), model.statusFilter))
        if (model.educationProgramId != null)
            predicates.add(cb.equal(root.get<UUID>("educationProgramId"), model.educationProgramId))
        if (model.creationDateFrom != null && model.creationDateTo != null)
            predicates.add(cb.between(root.get<LocalDate>("creationDate"), model.creationDateFrom, model.creationDateTo))
        if (model.creationDateFrom != null && model.creationDateTo != null)
            predicates.add(cb.between(root.get<LocalDate>("creationDate"), model.creationDateFrom, model.creationDateTo))

        if (model.dateFrom != null && model.dateTo != null) {
            predicates.add(
                    cb.or(
                            cb.between(root.get<LocalDate>("startDate"), model.dateFrom, model.dateTo),
                            cb.between(root.get<LocalDate>("endDate"), model.dateFrom, model.dateTo),
                            cb.and(
                                    cb.greaterThanOrEqualTo(root.get<LocalDate>("endDate"), model.dateTo),
                                    cb.lessThanOrEqualTo(root.get<LocalDate>("startDate"), model.dateFrom)
                            )
                    )
            )
        }

        cq.where(cb.and(*predicates.toTypedArray()))
        if (model.sortType == Sort.Direction.DESC)
            cq.orderBy(cb.desc(root.get<LocalDate>("startDate")))
        else
            cq.orderBy(cb.asc(root.get<LocalDate>("startDate")))

        val typedQuery = em.createQuery(cq)
        countQuery.select(cb.count(countQuery.from(course)))
        countQuery.where(cb.and(*predicates.toTypedArray()))
        typedQuery.firstResult = (model.pageNum) * model.pageSize
        typedQuery.maxResults = model.pageSize

        val query = em.createQuery(countQuery)
        val count = query.singleResult
        val resultList = typedQuery.resultList
        return PageResponseModel(resultList.toMutableSet(), count ?: 0)
    }
}
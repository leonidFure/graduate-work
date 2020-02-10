package com.lgorev.ksuonlineeducation.repository.course

import com.lgorev.ksuonlineeducation.domain.course.CourseRequestPageModel
import com.lgorev.ksuonlineeducation.domain.educationprogram.EducationProgramStatus
import com.lgorev.ksuonlineeducation.repository.educationprogram.EducationProgramEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Sort
import java.time.LocalDate
import java.util.*
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.persistence.criteria.Predicate

class CoursePagingRepositoryImpl(@PersistenceContext private val em: EntityManager) : CoursePagingRepository {

    private val course = CourseEntity::class.java

    override fun findPage(model: CourseRequestPageModel): Page<CourseEntity> {
        val cb = em.criteriaBuilder

        val cq = cb.createQuery(course)
        val root = cq.from(course)

        val predicates = mutableSetOf<Predicate>()
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
            cq.orderBy(cb.desc(root.get<String>(model.sortField)))
        else
            cq.orderBy(cb.asc(root.get<String>(model.sortField)))

        val typedQuery = em.createQuery(cq)
        typedQuery.firstResult = (model.pageNum) * model.pageSize
        typedQuery.maxResults = model.pageSize

        return PageImpl<CourseEntity>(typedQuery.resultList)
    }
}
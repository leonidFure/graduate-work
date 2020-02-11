package com.lgorev.ksuonlineeducation.repository.lesson

import com.lgorev.ksuonlineeducation.domain.lesson.LessonRequestPageModel
import com.lgorev.ksuonlineeducation.domain.lesson.LessonStatus
import com.lgorev.ksuonlineeducation.repository.course.CourseEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Sort
import java.time.LocalDate
import java.util.*
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.persistence.criteria.Predicate

class LessonPagingRepositoryImpl(@PersistenceContext private val em: EntityManager) : LessonPagingRepository {

    val lesson = LessonEntity::class.java

    override fun findLessonPage(model: LessonRequestPageModel): Page<LessonEntity> {
        val cb = em.criteriaBuilder

        val cq = cb.createQuery(lesson)
        val root = cq.from(lesson)

        val predicates = mutableSetOf<Predicate>()
        if (model.courseId != null)
            predicates.add(cb.equal(root.get<UUID>("courseId"), model.courseId))
        if (model.fromDate != null && model.toDate != null)
            predicates.add(cb.between(root.get<LocalDate>("date"), model.fromDate, model.toDate))
        if (model.statusFilter != null)
            predicates.add(cb.equal(root.get<LessonStatus>("status"), model.statusFilter))
        cq.where(cb.and(*predicates.toTypedArray()))

        if (model.sortType == Sort.Direction.DESC)
            cq.orderBy(cb.desc(root.get<String>(model.sortField)))
        else
            cq.orderBy(cb.asc(root.get<String>(model.sortField)))

        val typedQuery = em.createQuery(cq)
        typedQuery.firstResult = (model.pageNum) * model.pageSize
        typedQuery.maxResults = model.pageSize

        return PageImpl<LessonEntity>(typedQuery.resultList)

    }
}
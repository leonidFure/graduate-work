package com.lgorev.ksuonlineeducation.repository.lesson

import com.lgorev.ksuonlineeducation.domain.lesson.LessonLogPageRequestModel
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Sort
import java.time.LocalDateTime
import java.util.*
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.persistence.criteria.Predicate

class LessonLogPagingRepositoryImpl(@PersistenceContext private val em: EntityManager) : LessonLogPagingRepository {

    val lessonLog = LessonLogEntity::class.java

    override fun getPage(model: LessonLogPageRequestModel): Page<LessonLogEntity> {
        val cb = em.criteriaBuilder

        val cq = cb.createQuery(lessonLog)
        val root = cq.from(lessonLog)
        val predicates = mutableSetOf<Predicate>()

        if(model.lessonId != null)
            predicates.add(cb.equal(root.get<UUID>("lessonId"), model.lessonId))
        if (model.fromDateTime != null && model.toDateTime != null)
            predicates.add(cb.between(root.get<LocalDateTime>("datetime"), model.fromDateTime, model.toDateTime))

        cq.where(cb.and(*predicates.toTypedArray()))

        if (model.sortType == Sort.Direction.DESC)
            cq.orderBy(cb.desc(root.get<LocalDateTime>("datetime")))
        else
            cq.orderBy(cb.asc(root.get<LocalDateTime>("datetime")))

        val typedQuery = em.createQuery(cq)
        typedQuery.firstResult = (model.pageNum) * model.pageSize
        typedQuery.maxResults = model.pageSize

        return PageImpl<LessonLogEntity>(typedQuery.resultList)
    }
}
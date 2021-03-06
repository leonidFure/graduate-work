package com.lgorev.ksuonlineeducation.repository.lesson

import com.lgorev.ksuonlineeducation.domain.common.PageResponseModel
import com.lgorev.ksuonlineeducation.domain.lesson.LessonRequestPageModel
import com.lgorev.ksuonlineeducation.repository.lesson.LessonEntity_.*
import org.springframework.data.domain.Sort
import java.util.*
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.persistence.criteria.Predicate

class LessonPagingRepositoryImpl(@PersistenceContext private val em: EntityManager) : LessonPagingRepository {

    val lesson = LessonEntity::class.java

    override fun findLessonPage(model: LessonRequestPageModel, ids: MutableSet<UUID>?): PageResponseModel<LessonEntity> {
        val cb = em.criteriaBuilder

        val cq = cb.createQuery(lesson)
        val root = cq.from(lesson)
        val countQuery = cb.createQuery(Long::class.java)

        val predicates = mutableSetOf<Predicate>()
        if (ids != null) predicates.add(root.get(id).`in`(ids))
        if (model.courseId != null)
            predicates.add(cb.equal(root.get(courseId), model.courseId))
        if (model.timetableIds.isNotEmpty())
            predicates.add(root.get(timetableId).`in`(model.timetableIds))
        if (model.fromDate != null && model.toDate != null)
            predicates.add(cb.between(root.get(date), model.fromDate, model.toDate))
        if (model.statusFilter != null)
            predicates.add(cb.equal(root.get(status), model.statusFilter))
        if(model.courseIds != null)
            predicates.add(root.get(courseId).`in`(model.courseIds))
        cq.where(cb.and(*predicates.toTypedArray()))

        if (model.sortType == Sort.Direction.DESC)
            cq.orderBy(cb.desc(root.get(date)))
        else
            cq.orderBy(cb.asc(root.get(date)))

        countQuery.select(cb.count(countQuery.from(lesson)))
        countQuery.where(cb.and(*predicates.toTypedArray()))
        val typedQuery = em.createQuery(cq)
        typedQuery.firstResult = (model.pageNum - 1) * model.pageSize
        typedQuery.maxResults = model.pageSize

        val query = em.createQuery(countQuery)
        val count = query.singleResult
        val resultList = typedQuery.resultList
        return PageResponseModel(resultList.toMutableSet(), count ?: 0)
    }
}
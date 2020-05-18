package com.lgorev.ksuonlineeducation.repository.educationprogram

import com.lgorev.ksuonlineeducation.domain.common.PageResponseModel
import com.lgorev.ksuonlineeducation.domain.educationprogram.EducationProgramRequestPageModel
import com.lgorev.ksuonlineeducation.domain.educationprogram.EducationProgramStatus
import com.lgorev.ksuonlineeducation.repository.educationprogram.EducationProgramEntity_.*
import org.springframework.data.domain.Sort
import java.time.LocalDate
import java.util.*
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.persistence.criteria.Predicate

class EducationProgramPagingRepositoryImpl(@PersistenceContext private val em: EntityManager) : EducationProgramPagingRepository {

    private val educationProgram = EducationProgramEntity::class.java

    override fun findPage(model: EducationProgramRequestPageModel): PageResponseModel<EducationProgramEntity> {
        val cb = em.criteriaBuilder

        val cq = cb.createQuery(educationProgram)
        val root = cq.from(educationProgram)
        val countQuery = cb.createQuery(Long::class.java)

        val predicates = mutableSetOf<Predicate>()
        if (model.nameFilter != null)
            predicates.add(cb.like(cb.upper(root.get(name)), "%${model.nameFilter.toUpperCase()}%"))
        if (model.actualFilter != null)
            predicates.add(cb.equal(root.get(isActual), model.actualFilter))
        if (model.statusFilter != null)
            predicates.add(cb.equal(root.get(status), model.statusFilter))
        if (model.subjectIds.isNotEmpty())
            predicates.add(root.get(subjectId).`in`(model.subjectIds))
        if (model.creationDateFrom != null && model.creationDateTo != null)
            predicates.add(cb.between(root.get(creationDate), model.creationDateFrom, model.creationDateTo))
        if (model.ids != null)
            predicates.add(root.get(id).`in`(model.ids))
        cq.where(cb.and(*predicates.toTypedArray()))
        if (model.sortType == Sort.Direction.DESC)
            cq.orderBy(cb.desc(root.get(creationDate)))
        else
            cq.orderBy(cb.asc(root.get(creationDate)))
        countQuery.select(cb.count(countQuery.from(educationProgram)))
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
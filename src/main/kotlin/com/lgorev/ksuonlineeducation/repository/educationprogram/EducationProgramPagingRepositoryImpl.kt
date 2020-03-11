package com.lgorev.ksuonlineeducation.repository.educationprogram

import com.lgorev.ksuonlineeducation.domain.common.PageResponseModel
import com.lgorev.ksuonlineeducation.domain.educationprogram.EducationProgramRequestPageModel
import com.lgorev.ksuonlineeducation.domain.educationprogram.EducationProgramStatus
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
            predicates.add(cb.like(cb.upper(root.get<String>("name")), "%${model.nameFilter.toUpperCase()}%"))
        if (model.actualFilter != null)
            predicates.add(cb.equal(root.get<Boolean>("isActual"), model.actualFilter))
        if (model.statusFilter != null)
            predicates.add(cb.equal(root.get<EducationProgramStatus>("status"), model.statusFilter))
        if (model.subjectIds.isNotEmpty())
            predicates.add(root.get<UUID>("subjectId").`in`(model.subjectIds))
        if (model.creationDateFrom != null && model.creationDateTo != null)
            predicates.add(cb.between(root.get<LocalDate>("creationDate"), model.creationDateFrom, model.creationDateTo))

        cq.where(cb.and(*predicates.toTypedArray()))
        if (model.sortType == Sort.Direction.DESC)
            cq.orderBy(cb.desc(root.get<LocalDate>("creationDate")))
        else
            cq.orderBy(cb.asc(root.get<LocalDate>("creationDate")))
        countQuery.select(cb.count(countQuery.from(educationProgram)))
        countQuery.where(cb.and(*predicates.toTypedArray()))
        val typedQuery = em.createQuery(cq)
        typedQuery.firstResult = (model.pageNum) * model.pageSize
        typedQuery.maxResults = model.pageSize
        val query = em.createQuery(countQuery)
        val count = query.singleResult
        val resultList = typedQuery.resultList
        return PageResponseModel(resultList.toMutableSet(), count ?: 0)
    }

}
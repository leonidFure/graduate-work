package com.lgorev.ksuonlineeducation.repository.educationprogram

import com.lgorev.ksuonlineeducation.domain.educationprogram.EducationProgramRequestPageModel
import com.lgorev.ksuonlineeducation.domain.educationprogram.EducationProgramStatus
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Sort
import java.time.LocalDate
import java.util.*
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.persistence.criteria.Predicate

class EducationProgramPagingRepositoryImpl(@PersistenceContext private val em: EntityManager) : EducationProgramPagingRepository {

    private val educationProgram = EducationProgramEntity::class.java

    override fun findPage(model: EducationProgramRequestPageModel): Page<EducationProgramEntity> {
        val cb = em.criteriaBuilder

        val cq = cb.createQuery(educationProgram)
        val root = cq.from(educationProgram)

        val predicates = mutableSetOf<Predicate>()
        if (model.nameFilter != null)
            predicates.add(cb.like(cb.upper(root.get<String>("name")), "%${model.nameFilter.toUpperCase()}%"))
        if (model.actualFilter != null)
            predicates.add(cb.equal(root.get<Boolean>("isActual"), model.actualFilter))
        if (model.statusFilter != null)
            predicates.add(cb.equal(root.get<EducationProgramStatus>("status"), model.statusFilter))
        if(model.subjectId != null)
            predicates.add(cb.equal(root.get<UUID>("subjectId"), model.subjectId))
        if(model.creationDateFrom != null && model.creationDateTo != null)
            predicates.add(cb.between(root.get<LocalDate>("creationDate"), model.creationDateFrom, model.creationDateTo))

        cq.where(cb.and(*predicates.toTypedArray()))
        if (model.sortType == Sort.Direction.DESC)
            cq.orderBy(cb.desc(root.get<LocalDate>("creationDate")))
        else
            cq.orderBy(cb.asc(root.get<LocalDate>("creationDate")))

        val typedQuery = em.createQuery(cq)
        typedQuery.firstResult = (model.pageNum) * model.pageSize
        typedQuery.maxResults = model.pageSize

        return PageImpl<EducationProgramEntity>(typedQuery.resultList)
    }
}
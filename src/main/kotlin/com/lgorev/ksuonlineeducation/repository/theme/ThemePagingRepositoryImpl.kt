package com.lgorev.ksuonlineeducation.repository.theme

import com.lgorev.ksuonlineeducation.domain.theme.ThemeRequestPageModel
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Sort
import java.util.*
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.persistence.criteria.Predicate


class ThemePagingRepositoryImpl(@PersistenceContext private val em: EntityManager) : ThemePagingRepository {

    private val theme = ThemeEntity::class.java

    override fun findPage(model: ThemeRequestPageModel): Page<ThemeEntity> {
        val cb = em.criteriaBuilder

        val cq = cb.createQuery(theme)
        val root = cq.from(theme)

        val predicates = mutableSetOf<Predicate>()
        if (model.nameFilter != null)
            predicates.add(cb.like(cb.upper(root.get<String>("name")), "%${model.nameFilter.toUpperCase()}%"))
        if (model.parentThemeId != null)
            predicates.add(cb.equal(root.get<UUID>("parentThemeId"), model.parentThemeId))
        if (model.educationProgramId != null)
            predicates.add(cb.equal(root.get<UUID>("educationProgramId"), model.educationProgramId))
        cq.where(cb.and(*predicates.toTypedArray()))
        if (model.sortType == Sort.Direction.DESC)
            cq.orderBy(cb.desc(root.get<String>(model.sortField)))
        else
            cq.orderBy(cb.asc(root.get<String>(model.sortField)))

        val typedQuery = em.createQuery(cq)
        typedQuery.firstResult = (model.pageNum) * model.pageSize
        typedQuery.maxResults = model.pageSize

        return PageImpl<ThemeEntity>(typedQuery.resultList)
    }
}
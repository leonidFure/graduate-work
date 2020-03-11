package com.lgorev.ksuonlineeducation.repository.theme

import com.lgorev.ksuonlineeducation.domain.common.PageResponseModel
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

    override fun findPage(model: ThemeRequestPageModel, ids: MutableSet<UUID>?): PageResponseModel<ThemeEntity> {
        val cb = em.criteriaBuilder

        val cq = cb.createQuery(theme)
        val root = cq.from(theme)
        val countQuery = cb.createQuery(Long::class.java)

        val predicates = mutableSetOf<Predicate>()
        if(ids != null) predicates.add(root.get<UUID>("id").`in`(ids))
        if (model.nameFilter != null)
            predicates.add(cb.like(cb.upper(root.get<String>("name")), "%${model.nameFilter.toUpperCase()}%"))
        if (model.parentThemeId != null)
            predicates.add(cb.equal(root.get<UUID>("parentThemeId"), model.parentThemeId))
        if (model.educationProgramId != null)
            predicates.add(cb.equal(root.get<UUID>("educationProgramId"), model.educationProgramId))
        cq.where(cb.and(*predicates.toTypedArray()))
        if (model.sortType == Sort.Direction.DESC)
            cq.orderBy(cb.desc(root.get<String>("name")))
        else
            cq.orderBy(cb.asc(root.get<String>("name")))

        countQuery.select(cb.count(countQuery.from(theme)))
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
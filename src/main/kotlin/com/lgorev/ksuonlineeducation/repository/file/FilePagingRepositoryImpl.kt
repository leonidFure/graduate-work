package com.lgorev.ksuonlineeducation.repository.file

import com.lgorev.ksuonlineeducation.domain.common.PageResponseModel
import com.lgorev.ksuonlineeducation.domain.file.FileRequestPageModel
import com.lgorev.ksuonlineeducation.repository.file.FileEntity_.*
import org.springframework.data.domain.Sort.Direction.*
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.persistence.criteria.Predicate

class FilePagingRepositoryImpl(@PersistenceContext private val em: EntityManager) : FilePagingRepository {

    val file = FileEntity::class.java

    override fun findPage(model: FileRequestPageModel): PageResponseModel<FileEntity> {
        val cb = em.criteriaBuilder

        val cq = cb.createQuery(file)
        val root = cq.from(file)
        val countQuery = cb.createQuery(Long::class.java)

        val predicates = mutableSetOf<Predicate>()
        if (model.ids != null)
            predicates.add(root.get(id).`in`(model.ids))


        cq.where(cb.and(*predicates.toTypedArray()))
        if (model.sortType == DESC)
            cq.orderBy(cb.desc(root.get(uploadingDateTime)))
        else
            cq.orderBy(cb.asc(root.get(uploadingDateTime)))

        val typedQuery = em.createQuery(cq)
        countQuery.select(cb.count(countQuery.from(file)))
        countQuery.where(cb.and(*predicates.toTypedArray()))
        typedQuery.firstResult = (model.pageNum - 1) * model.pageSize
        typedQuery.maxResults = model.pageSize

        val query = em.createQuery(countQuery)
        val count = query.singleResult
        val resultList = typedQuery.resultList
        return PageResponseModel(resultList.toMutableSet(), count ?: 0)
    }
}
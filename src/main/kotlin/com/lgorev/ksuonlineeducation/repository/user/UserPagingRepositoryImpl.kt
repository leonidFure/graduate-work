package com.lgorev.ksuonlineeducation.repository.user

import com.lgorev.ksuonlineeducation.domain.common.PageResponseModel
import com.lgorev.ksuonlineeducation.domain.user.Role
import com.lgorev.ksuonlineeducation.domain.user.UserPageRequestModel
import com.lgorev.ksuonlineeducation.repository.user.UserEntity_.*
import com.lgorev.ksuonlineeducation.util.getRole
import org.springframework.data.domain.Sort
import java.security.Principal
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.persistence.criteria.Predicate

class UserPagingRepositoryImpl(@PersistenceContext private val em: EntityManager) : UserPagingRepository {

    private val user = UserEntity::class.java

    override fun getPage(model: UserPageRequestModel, principal: Principal): PageResponseModel<UserEntity> {
        val cb = em.criteriaBuilder
        val cq = cb.createQuery(user)
        val root = cq.from(user)
        val countQuery = cb.createQuery(Long::class.java)
        val predicates = mutableSetOf<Predicate>()

        if (model.ids != null)
            predicates.add(root.get(id).`in`(model.ids))
        if (model.filter != null) {
            val concatWithPatronymic = cb.upper(cb.concat(root.get(lastName), cb.concat(" ", cb.concat(root.get(firstName), cb.concat(" ", root.get(patronymic))))))
            val concatWithoutPatronymic = cb.upper(cb.concat(root.get(lastName), cb.concat(" ", root.get(firstName))))
            val expression = cb.selectCase<String>()
                    .`when`(cb.isNotNull(root.get(patronymic)), concatWithPatronymic)
                    .otherwise(concatWithoutPatronymic)

            val nameLikeExpresion = cb.like(expression, "%${model.filter}%".toUpperCase())
            val emailLikeExpresion = cb.like(cb.upper(root[email]), "%${model.filter}%".toUpperCase())
            predicates.add(cb.or(nameLikeExpresion, emailLikeExpresion))
        }
        if (model.isTeacherFilter != null) {
            if (model.isTeacherFilter) {
                predicates.add(cb.equal(root.get(role), Role.TEACHER))
            } else {
                predicates.add(cb.notEqual(root.get(role), Role.TEACHER))
            }
        }
        val role1 = getRole(principal)

        if (model.userRoleFilter != null)
            predicates.add(cb.equal(root[role], model.userRoleFilter))
        else if(role1 != Role.ADMIN) {
            predicates.add(root[role].`in`(listOf(Role.TEACHER, Role.STUDENT)))
        }

        cq.where(cb.and(*predicates.toTypedArray()))
        if (model.sortType == Sort.Direction.DESC)
            cq.orderBy(cb.desc(root.get(registrationDate)))
        else
            cq.orderBy(cb.asc(root.get(registrationDate)))

        val typedQuery = em.createQuery(cq)
        countQuery.select(cb.count(countQuery.from(user)))
        countQuery.where(cb.and(*predicates.toTypedArray()))
        typedQuery.firstResult = (model.pageNum - 1) * model.pageSize
        typedQuery.maxResults = model.pageSize

        val query = em.createQuery(countQuery)
        val count = query.singleResult
        val resultList = typedQuery.resultList
        return PageResponseModel(resultList.toMutableSet(), count ?: 0)
    }
}
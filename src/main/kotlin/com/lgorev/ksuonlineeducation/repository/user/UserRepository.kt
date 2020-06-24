package com.lgorev.ksuonlineeducation.repository.user

import com.lgorev.ksuonlineeducation.domain.user.Role
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : PagingAndSortingRepository<UserEntity, UUID>, UserPagingRepository {
    fun findByEmail(email: String): UserEntity?
    fun existsByIdAndRole(id: UUID, role: Role): Boolean
    fun existsByEmail(email: String): Boolean
    fun findAllByRole(role: Role): List<UserEntity>
    fun findAllByRoleAndIdNotIn(role: Role, ids: List<UUID>): List<UserEntity>
}
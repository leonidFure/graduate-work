package com.lgorev.ksuonlineeducation.repository.user

import com.lgorev.ksuonlineeducation.domain.user.Role
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository: PagingAndSortingRepository<UserEntity, UUID>, UserPagingRepository {
    fun findByEmail(email: String): UserEntity?
    fun existsByIdAndRole(id: UUID, role: Role): Boolean
}
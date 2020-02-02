package com.lgorev.ksuonlineeducation.repository.user

import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository: PagingAndSortingRepository<UserEntity, UUID> {
    fun findByEmail(email: String): UserEntity?
}
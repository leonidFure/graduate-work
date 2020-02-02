package com.lgorev.ksuonlineeducation.repository.group

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserGroupRepository : CrudRepository<UserGroupEntity, UserGroupId> {

    @Query("SELECT u FROM UserGroupEntity u WHERE u.userGroupId.userId = :id")
    fun findByUserId(@Param("id") id: UUID): UserGroupEntity?
}
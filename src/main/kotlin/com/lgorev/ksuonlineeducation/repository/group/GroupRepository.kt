package com.lgorev.ksuonlineeducation.repository.group

import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface GroupRepository : PagingAndSortingRepository<GroupEntity, UUID> {
    fun findByName(name: String): GroupEntity?
}
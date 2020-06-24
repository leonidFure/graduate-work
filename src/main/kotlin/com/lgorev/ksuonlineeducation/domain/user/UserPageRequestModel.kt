package com.lgorev.ksuonlineeducation.domain.user

import org.springframework.data.domain.Sort.Direction
import org.springframework.data.domain.Sort.Direction.*
import java.util.*

class UserPageRequestModel (
        val pageNum: Int = 0,
        val pageSize: Int = 10,
        val sortType: Direction = ASC,
        val filter: String? = null,
        var ids: MutableSet<UUID>? = null,
        val courseIdForTeacher: UUID? = null,
        val courseIdForSubscription: UUID? = null,
        val isTeacherFilter: Boolean? = null,
        val userRoleFilter: Role? = null
)

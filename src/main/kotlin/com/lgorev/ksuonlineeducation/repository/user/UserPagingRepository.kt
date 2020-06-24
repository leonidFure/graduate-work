package com.lgorev.ksuonlineeducation.repository.user

import com.lgorev.ksuonlineeducation.domain.common.PageResponseModel
import com.lgorev.ksuonlineeducation.domain.user.UserPageRequestModel
import org.springframework.stereotype.Repository
import java.security.Principal

@Repository
interface UserPagingRepository {
    fun getPage(model: UserPageRequestModel, principal: Principal): PageResponseModel<UserEntity>
}

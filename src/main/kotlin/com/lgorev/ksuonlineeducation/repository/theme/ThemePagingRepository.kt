package com.lgorev.ksuonlineeducation.repository.theme

import com.lgorev.ksuonlineeducation.domain.common.PageResponseModel
import com.lgorev.ksuonlineeducation.domain.theme.ThemeRequestPageModel
import org.springframework.data.domain.Page
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ThemePagingRepository {
    fun findPage(model: ThemeRequestPageModel, ids: MutableSet<UUID>?): PageResponseModel<ThemeEntity>
}
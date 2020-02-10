package com.lgorev.ksuonlineeducation.repository.theme

import com.lgorev.ksuonlineeducation.domain.theme.ThemeRequestPageModel
import org.springframework.data.domain.Page
import org.springframework.stereotype.Repository

@Repository
interface ThemePagingRepository {
    fun findPage(model: ThemeRequestPageModel): Page<ThemeEntity>
}
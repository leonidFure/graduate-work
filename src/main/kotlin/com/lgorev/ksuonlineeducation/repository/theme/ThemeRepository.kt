package com.lgorev.ksuonlineeducation.repository.theme

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ThemeRepository : CrudRepository<ThemeEntity, UUID>, ThemePagingRepository {
    fun findAllByEducationProgramId(id: UUID): MutableSet<ThemeEntity>
}
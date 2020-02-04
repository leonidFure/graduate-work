package com.lgorev.ksuonlineeducation.repository.theme

import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ThemeRepository : PagingAndSortingRepository<ThemeEntity, UUID>
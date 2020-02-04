package com.lgorev.ksuonlineeducation.repository.timetable

import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface TimetableRepository : PagingAndSortingRepository<TimetableEntity, UUID>
package com.lgorev.ksuonlineeducation.repository.lesson

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface LessonsThemesRepository : CrudRepository<LessonsThemesEntity, LessonsThemesId>
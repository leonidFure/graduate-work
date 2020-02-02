package com.lgorev.ksuonlineeducation.repository.teacher

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface TeacherRepository: CrudRepository<TeacherEntity, UUID>
package com.lgorev.ksuonlineeducation.repository.course

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface CoursesTeachersRepository : CrudRepository<CoursesTeachersEntity, CoursesTeachersId>
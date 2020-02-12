package com.lgorev.ksuonlineeducation.repository.educationprogram

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface TeachersEducationProgramsRepository :
        CrudRepository<TeachersEducationProgramsEntity, TeachersEducationProgramsId>
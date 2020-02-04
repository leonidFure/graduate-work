package com.lgorev.ksuonlineeducation.repository.educationprogram

import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface EducationProgramRepository : PagingAndSortingRepository<EducationProgramEntity, UUID>
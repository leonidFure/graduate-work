package com.lgorev.ksuonlineeducation.repository.subject

import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface SubjectRepository : PagingAndSortingRepository<SubjectEntity, UUID>
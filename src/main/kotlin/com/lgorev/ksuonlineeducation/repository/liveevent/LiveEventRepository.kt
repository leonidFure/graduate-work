package com.lgorev.ksuonlineeducation.repository.liveevent

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface LiveEventRepository: CrudRepository<LiveEventEntity, String>
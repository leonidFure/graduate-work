package com.lgorev.ksuonlineeducation.repository.session

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.*

@Repository
interface SessionRepository: CrudRepository<SessionEntity, UUID> {
    fun deleteAllByExpirationDatetimeBefore(date: LocalDateTime)
}
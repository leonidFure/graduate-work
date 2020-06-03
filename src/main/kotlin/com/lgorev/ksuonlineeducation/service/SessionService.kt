package com.lgorev.ksuonlineeducation.service

import com.lgorev.ksuonlineeducation.repository.session.SessionEntity
import com.lgorev.ksuonlineeducation.repository.session.SessionRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*

@Service
@Transactional
@EnableScheduling
class SessionService(private val sessionRepository: SessionRepository) {

    fun addSession(entity: SessionEntity) = sessionRepository.save(entity)

    fun updateSession(id: UUID, expirationDateTime: LocalDateTime) {
        sessionRepository.findByIdOrNull(id)?.let { session ->
            session.expirationDatetime = expirationDateTime
        }
    }

    fun deleteSession(sessionId: UUID) = sessionRepository.deleteById(sessionId)

    @Scheduled(fixedRate = 5000)
    fun dropOldSessions() {
        sessionRepository.deleteAllByExpirationDatetimeBefore(LocalDateTime.now())
    }
}
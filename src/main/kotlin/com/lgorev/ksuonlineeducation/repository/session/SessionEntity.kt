package com.lgorev.ksuonlineeducation.repository.session

import java.time.LocalDateTime
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "session")
data class SessionEntity(
        @Id
        @Column(name = "id")
        val id: UUID,
        @Column(name = "user_id")
        val userId: UUID,
        @Column(name = "expiration_datetime")
        var expirationDatetime: LocalDateTime
)
package com.lgorev.ksuonlineeducation.repository.liveevent

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "live_event")
data class LiveEventEntity(
        @Id
        @Column(name = "id")
        val id: String,
        @Column(name = "name")
        val name: String,
        @Column(name = "primary_server")
        val primaryServer: String,
        @Column(name = "player_id")
        val playerId: String
)
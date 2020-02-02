package com.lgorev.ksuonlineeducation.repository.group

import java.util.*
import javax.persistence.*


@Entity
@Table(name = "std_group")
data class GroupEntity(
        @Id
        @Column(name = "id")
        val id: UUID = UUID.randomUUID(),
        @Column(name = "name", unique = true)
        var name: String = "",
        @Column(name = "training_direction_id")
        var trainingDirectionId: UUID
)
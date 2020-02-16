package com.lgorev.ksuonlineeducation.repository.trainingdirection

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "training_direction")
data class TrainingDirectionEntity(
        @Id
        @Column(name = "id")
        val id: UUID = UUID.randomUUID(),
        @Column(name = "name", unique = true)
        var name: String = "",
        @Column(name = "description")
        var description: String,
        @Column(name = "faculty_id")
        var facultyId: UUID
)
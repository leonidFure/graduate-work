package com.lgorev.ksuonlineeducation.repository.trainingdirection

import java.io.Serializable
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "subjects_for_entrance")
data class SubjectsForEntranceEntity(
        @EmbeddedId
        val subjectsForEntranceId: SubjectsForEntranceId
)

@Embeddable
data class SubjectsForEntranceId(
        @Column(name = "direction_id")
        val trainingDirectionId: UUID,
        @Column(name = "subject_id")
        val subjectId: UUID
) : Serializable
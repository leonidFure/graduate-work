package com.lgorev.ksuonlineeducation.repository.subject

import com.lgorev.ksuonlineeducation.domain.subject.SubjectType
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "subject")
data class SubjectEntity(
        @Id
        @Column(name = "id")
        val id: UUID = UUID.randomUUID(),
        @Column(name = "name")
        var name: String,
        @Column(name = "description")
        var description: String,
        @Column(name = "type")
        @Enumerated(value = EnumType.STRING)
        var type: SubjectType,
        @Column(name = "image_id")
        var imageId: UUID?
)


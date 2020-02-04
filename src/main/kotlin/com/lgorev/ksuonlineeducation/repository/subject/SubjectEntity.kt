package com.lgorev.ksuonlineeducation.repository.subject

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "subject")
data class SubjectEntity(
        @Id
        @Column(name = "id")
        val id: UUID = UUID.randomUUID(),
        @Column(name = "name")
        val name: String,
        @Column(name = "description")
        val description: String,
        @Column(name = "type")
        @Enumerated(value = EnumType.STRING)
        val type: SubjectType
)

enum class SubjectType { EXAM, OLYMPIAD }
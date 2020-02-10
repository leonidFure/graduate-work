package com.lgorev.ksuonlineeducation.repository.theme

import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table
data class ThemeEntity(
        @Id
        @Column(name = "id")
        val id: UUID = UUID.randomUUID(),
        @Column(name = "parent_theme_id")
        var parentThemeId: UUID?,
        @Column(name = "number")
        var number: Short,
        @Column(name = "education_program_id")
        var educationProgramId: UUID,
        @Column(name = "name")
        var name: String,
        @Column(name = "description")
        var description: String
)
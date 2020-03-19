package com.lgorev.ksuonlineeducation.repository.lesson

import java.io.Serializable
import java.util.*
import javax.persistence.*


@Entity
@Table(name = "lessons_themes")
data class LessonsThemesEntity(
        @EmbeddedId
        val lessonsThemesId: LessonsThemesId
)

@Embeddable
data class LessonsThemesId(
        @Column(name = "lesson_id")
        val lessonId: UUID,
        @Column(name = "theme_id")
        val themesId: UUID
) : Serializable
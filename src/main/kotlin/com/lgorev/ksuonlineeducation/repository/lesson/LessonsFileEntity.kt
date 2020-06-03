package com.lgorev.ksuonlineeducation.repository.lesson

import java.io.Serializable
import java.util.*
import javax.persistence.*


@Entity
@Table(name = "lessons_files")
data class LessonsFilesEntity(
        @EmbeddedId
        val lessonsFilesId: LessonsFilesId
)

@Embeddable
data class LessonsFilesId(
        @Column(name = "lesson_id")
        val lessonId: UUID,
        @Column(name = "file_id")
        val fileId: UUID
) : Serializable
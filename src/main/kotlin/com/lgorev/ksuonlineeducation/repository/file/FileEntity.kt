package com.lgorev.ksuonlineeducation.repository.file

import com.lgorev.ksuonlineeducation.domain.file.FileType
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "file")
data class FileEntity(
        @Id
        @Column(name = "id")
        val id: UUID = UUID.randomUUID(),
        @Column(name = "name")
        var name: String,
        @Column(name = "type")
        @Enumerated(EnumType.STRING)
        val type: FileType,
        @Column(name = "content_type")
        val contentType: String?,
        @Column(name = "uploading_date_time")
        val uploadingDateTime: LocalDateTime,
        @Column(name = "last_update_date_time")
        var lastUpdateDateTime: LocalDateTime? = null
)

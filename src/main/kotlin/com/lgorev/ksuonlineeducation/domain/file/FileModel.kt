package com.lgorev.ksuonlineeducation.domain.file

import java.time.LocalDateTime
import java.util.*

data class FileModel(
        val id: UUID,
        val name: String,
        val type: FileType,
        var url: String,
        val contentType: String?,
        val uploadingDateTime: LocalDateTime,
        val lastUpdateDateTime: LocalDateTime?
)
package com.lgorev.ksuonlineeducation.domain.file

import javafx.scene.control.TableColumn
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.*
import org.springframework.data.domain.Sort.Direction.*
import java.util.*

data class FileRequestPageModel (
        val pageNum: Int = 0,
        val pageSize: Int = 10,
        val sortType: Direction = ASC,
        val lessonId: UUID? = null,
        var ids: MutableSet<UUID>? = null
)

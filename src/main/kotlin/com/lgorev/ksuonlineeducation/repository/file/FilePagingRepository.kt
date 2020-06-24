package com.lgorev.ksuonlineeducation.repository.file

import com.lgorev.ksuonlineeducation.domain.common.PageResponseModel
import com.lgorev.ksuonlineeducation.domain.file.FileRequestPageModel
import org.springframework.stereotype.Repository

@Repository
interface FilePagingRepository {
    fun findPage(model: FileRequestPageModel): PageResponseModel<FileEntity>
}

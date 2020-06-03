package com.lgorev.ksuonlineeducation.repository.file

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface FileRepository: CrudRepository<FileEntity, UUID>, FilePagingRepository

package com.lgorev.ksuonlineeducation.infrostructure.vimeo.model

data class VimeoVideoPageRequestModel (
        val pageNum: Int,
        val pageSize: Int,
        val videoUrls: String,
        val direction: String = "asc",
        val sortField: String = "date"
)

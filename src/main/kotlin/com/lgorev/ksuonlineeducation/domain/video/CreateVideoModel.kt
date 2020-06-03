package com.lgorev.ksuonlineeducation.domain.video

import com.fasterxml.jackson.annotation.JsonProperty

data class CreateVideoModel(
        val upload: Upload
)

data class Upload(
        val approach: String = "post",
        val size: Long,
        @JsonProperty("redirect_url")
        val redirectUri: String
)
package com.lgorev.ksuonlineeducation.infrostructure.wowza.models

data class DirectPlaybackUrls(
    val rtmp: List<String>?,
    val rtsp: List<String>?,
    val wowz: List<String>?
)
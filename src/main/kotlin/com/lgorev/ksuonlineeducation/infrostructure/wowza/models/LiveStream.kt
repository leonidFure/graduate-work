package com.lgorev.ksuonlineeducation.infrostructure.wowza.models

data class LiveStream(
    val aspect_ratio_height: Int = 1280,
    val aspect_ratio_width: Int = 720,
    val billing_mode: String = "pay_as_you_go",
    val broadcast_location: String = "eu_germany",
    val closed_caption_type: String = "none",
    val delivery_method: String = "push",
    val encoder: String = "other_rtmp",
    val hosted_page: Boolean = false,
    val hosted_page_sharing_icons: Boolean = false,
    val name: String,
    val password: String,
    val username: String,
    val player_responsive: Boolean = true,
    val player_type: String = "wowza_player",
    val transcoder_type: String = "transcoded"
)
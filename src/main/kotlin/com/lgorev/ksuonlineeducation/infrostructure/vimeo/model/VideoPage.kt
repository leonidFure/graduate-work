package com.lgorev.ksuonlineeducation.infrostructure.vimeo.model

import com.fasterxml.jackson.annotation.JsonProperty

data class VideoPage(
        val data: List<Video>,
        val page: Int?,
        @JsonProperty("per_page")
        val perPage: Int?,
        val total: Int?
)

data class Video(
        @JsonProperty("created_time")
        val createdTime: String?,
        val description: String?,
        val duration: Int?,
        val embed: Embed?,
        val height: Int?,
        val width: Int?,
        val link: String?,
        val user: User,
        @JsonProperty("modified_time")
        val modifiedTime: String?,
        val name: String?,
        val pictures: Pictures,
        val privacy: Privacy,
        @JsonProperty("release_time")
        val releaseTime: String?,
        @JsonProperty("resource_key")
        val resourceKey: String?,
        val stats: Stats,
        val status: String?,
        val transcode: Transcode,
        val type: String?,
        val uri: String?,
        val upload: Upload
)

data class Embed(
        val badges: Badges,
        val buttons: Buttons,
        val color: String?,
        val html: String?,
        @JsonProperty("playbar")
        val playBar: Boolean?,
        val speed: Boolean?,
        val title: Title,
        val volume: Boolean?
)

data class Badges(
        val hdr: Boolean?,
        val live: Live,
        val vod: Boolean?,
        @JsonProperty("weekend_challenge")
        val weekendChallenge: Boolean?
)

data class Buttons(
        val embed: Boolean?,
        val fullscreen: Boolean?,
        val hd: Boolean?,
        val like: Boolean?,
        val scaling: Boolean?,
        val share: Boolean?,
        @JsonProperty("watchlater")
        val watchLater: Boolean?
)

data class Stats(val plays: Int?)

data class Title(val name: String?, val owner: String?, val portrait: String?)

data class Live(val archived: Boolean?, val streaming: Boolean?)

data class User(
        val account: String?,
        @JsonProperty("content_filter")
        val contentFilter: List<String>,
        @JsonProperty("created_time")
        val createdTime: String?,
        val gender: String?,
        val link: String?,
        val location: String?,
        val name: String?,
        @JsonProperty("resource_key")
        val resourceKey: String?,
        val uri: String?
)

data class Upload (
        val status: String?,
        val link: String?,
        @JsonProperty("upload_link")
        val uploadLink: String?,
        @JsonProperty("complete_uri")
        val completeUri: String?,
        val form: String?,
        val approach: String?,
        val size: Long?,
        @JsonProperty("redirect_url")
        val redirectUrl: String?
)

data class Pictures(
        val active: Boolean?,
        @JsonProperty("resource_key")
        val resourceKey: String?,
        val sizes: List<Size>,
        val type: String?,
        val uri: String?
)

data class Size(
        val height: Int?,
        val width: Int?,
        val link: String?,
        @JsonProperty("link_with_play_button")
        val linkWithPlayButton: String?
)

data class Privacy(
        val add: Boolean?,
        val comments: String?,
        val download: Boolean?,
        val embed: String?,
        val view: String?
)

data class Transcode(val status: String?)
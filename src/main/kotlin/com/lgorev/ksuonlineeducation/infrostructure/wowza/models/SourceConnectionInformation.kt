package com.lgorev.ksuonlineeducation.infrostructure.wowza.models

data class SourceConnectionInformation(
    val application: String?,
    val disable_authentication: Boolean?,
    val host_port: Int?,
    val password: String?,
    val primary_server: String?,
    val stream_name: String?,
    val username: String?
)
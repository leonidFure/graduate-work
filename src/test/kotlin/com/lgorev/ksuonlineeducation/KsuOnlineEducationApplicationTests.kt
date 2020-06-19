package com.lgorev.ksuonlineeducation

import com.lgorev.ksuonlineeducation.infrostructure.vimeo.VimeoClient
import com.lgorev.ksuonlineeducation.infrostructure.wowza.WowzaClient
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest


@SpringBootTest
class KsuOnlineEducationApplicationTests {

    @Autowired
    private lateinit var vimeoClient: VimeoClient

    @Autowired
    private lateinit var wowzaClient: WowzaClient

    @Test
    fun testGetVideo() {
        val createLiveEvent = wowzaClient.createLiveEvent("server_side3", "lgorev", "paleto81")
        println(createLiveEvent)
    }

    @Test
    fun startStream() {
        val startLiveEvent = wowzaClient.startLiveEvent("knjj1gjj")
        println(startLiveEvent)
    }

    @Test
    fun stopStream() {
        val startLiveEvent = wowzaClient.stopLiveEvent("knjj1gjj")
        println(startLiveEvent)
    }

}
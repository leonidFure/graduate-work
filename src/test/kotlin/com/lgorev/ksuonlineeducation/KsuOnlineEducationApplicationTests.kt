package com.lgorev.ksuonlineeducation

import com.clickntap.vimeo.Vimeo
import com.lgorev.ksuonlineeducation.infrostructure.vimeo.VimeoClient
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest


@SpringBootTest
class KsuOnlineEducationApplicationTests {

    @Autowired
    private lateinit var vimeoClient: VimeoClient

    @Test
    fun testGetVideo() {
    }

}
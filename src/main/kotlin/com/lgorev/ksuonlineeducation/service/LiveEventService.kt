package com.lgorev.ksuonlineeducation.service

import com.lgorev.ksuonlineeducation.exception.BadRequestException
import com.lgorev.ksuonlineeducation.infrostructure.wowza.WowzaClient
import com.lgorev.ksuonlineeducation.infrostructure.wowza.models.LiveEventStateResponse
import com.lgorev.ksuonlineeducation.repository.liveevent.LiveEventEntity
import com.lgorev.ksuonlineeducation.repository.liveevent.LiveEventRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.*

@Service
class LiveEventService(private val liveEventRepository: LiveEventRepository, private val wowzaClient: WowzaClient) {

    @Autowired
    private lateinit var courseService: CourseService

    fun createLiveEvent(name: String, courseId: UUID) {
        val liveEvent = wowzaClient.createLiveEvent(name, courseId.toString(), courseId.toString())
        if (liveEvent != null) {
            if (liveEvent.live_stream != null) {
                if (liveEvent.live_stream.id != null && liveEvent.live_stream.source_connection_information != null) {
                    if (liveEvent.live_stream.source_connection_information.stream_name != null &&
                            liveEvent.live_stream.source_connection_information.primary_server != null &&
                            liveEvent.live_stream.player_id != null) {
                        liveEventRepository.save(LiveEventEntity(
                                liveEvent.live_stream.id,
                                liveEvent.live_stream.source_connection_information.stream_name,
                                liveEvent.live_stream.source_connection_information.primary_server,
                                liveEvent.live_stream.player_id
                        ))
                        courseService.changeLiveEventId(courseId, liveEvent.live_stream.id)
                    }
                }
            }
        }
    }

    fun startLiveEvent(id: String): LiveEventStateResponse? {
        val state = getState(id)
        if (state?.live_stream?.state != null) {
            if (state.live_stream.state == "stopped") {
                return wowzaClient.startLiveEvent(id)
            }
        }
        throw BadRequestException("Ошибка сервиса трансляций")
    }

    fun stopLiveEvent(id: String): LiveEventStateResponse? {
        val state = getState(id)
        if (state?.live_stream?.state != null) {
            if (state.live_stream.state == "started") {
                return wowzaClient.stopLiveEvent(id)
            }
        }
        throw BadRequestException("Ошибка сервиса трансляций")

    }

    fun getState(id: String) = wowzaClient.getState(id)
    fun deleteLiveEvent(id: String) {}

    fun getLiveEvent(id: String): LiveEventEntity {
        liveEventRepository.findByIdOrNull(id)?.let { return it }
        throw BadRequestException("Трансляция не найдена")
    }
}
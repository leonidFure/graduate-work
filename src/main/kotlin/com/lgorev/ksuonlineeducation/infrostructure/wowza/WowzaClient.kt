package com.lgorev.ksuonlineeducation.infrostructure.wowza

import com.lgorev.ksuonlineeducation.exception.VideoResponseException
import com.lgorev.ksuonlineeducation.infrostructure.wowza.models.LiveEventResponse
import com.lgorev.ksuonlineeducation.infrostructure.wowza.models.LiveEventStateResponse
import com.lgorev.ksuonlineeducation.infrostructure.wowza.models.LiveStream
import com.lgorev.ksuonlineeducation.infrostructure.wowza.models.StartLiveEventModel
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder

@Service
class WowzaClient {

    @Value("\${wowza.access_key}")
    private lateinit var accessKey: String

    @Value("\${wowza.api_key}")
    private lateinit var apiKey: String

    @Value("\${wowza.url}")
    private lateinit var wowzaUrl: String

    fun createLiveEvent(name: String, username: String, password: String): LiveEventResponse? {
        val url = "${wowzaUrl}/live_streams"
        return post(url, apiKey, accessKey, StartLiveEventModel(LiveStream(name = name, username = username, password = password)))
    }

    fun startLiveEvent(liveStreamId: String): LiveEventStateResponse? {
        val url = "${wowzaUrl}/live_streams/${liveStreamId}/start"
        return put<Unit, LiveEventStateResponse>(url, apiKey, accessKey)
        //curl -X PUT \
        //-H "Content-Type: application/json" \
        //-H "wsc-api-key: ${WSC_API_KEY}" \
        //-H "wsc-access-key: ${WSC_ACCESS_KEY}" \
        //"${WSC_HOST}/api/${WSC_VERSION}/live_streams/[live_stream_id]/start"
    }

    fun stopLiveEvent(liveStreamId: String): LiveEventStateResponse? {
        val url = "${wowzaUrl}/live_streams/${liveStreamId}/stop"
        return put<Unit, LiveEventStateResponse>(url, apiKey, accessKey)
        //curl -X PUT \
        //-H "wsc-api-key: ${WSC_API_KEY}" \
        //-H "wsc-access-key: ${WSC_ACCESS_KEY}" \
        //"${WSC_HOST}/api/${WSC_VERSION}/live_streams/[live_stream_id]/stop"
    }

    fun viewLiveEvent() {
        //curl -X GET \
        //-H "wsc-api-key: ${WSC_API_KEY}" \
        //-H "wsc-access-key: ${WSC_ACCESS_KEY}" \
        //"${WSC_HOST}/api/${WSC_VERSION}/live_streams/[live_stream_id]"
    }

    fun getState(id: String): LiveEventStateResponse? {
        val url = "${wowzaUrl}/live_streams/${id}/state"
        return get(url, apiKey, accessKey)
    }

    private companion object {


        /**
         * GET method for Vimeo API.
         *
         * Retrieves the JSON representation of a resource.
         *
         * @param T the type of response
         * @property url the url of API
         * @property accessToken the token of authorization user in Vimeo
         * @property params the map of params (keys and values). Can be null
         * @throws VideoResponseException
         * */
        @Throws(VideoResponseException::class)
        inline fun <reified T : Any> get(url: String, apiKey: String, accessKey: String, params: MutableMap<String, String>? = null): T? {
            val restTemplate = RestTemplate()
            val headers = HttpHeaders()
            headers["wsc-api-key"] = apiKey
            headers["wsc-access-key"] = accessKey

            val request = HttpEntity<Unit>(headers)
            var httpUrl = UriComponentsBuilder.fromHttpUrl(url)
            params?.forEach { (k, v) -> httpUrl = httpUrl.queryParam(k, v) }
            try {
                val response = restTemplate.exchange(httpUrl.toUriString(), HttpMethod.GET, request, T::class.java)
                if (response.statusCode.isError) throw VideoResponseException()
                return response.body
            } catch (e: Throwable) {
                throw VideoResponseException(e.message)
            }
        }

        /**
         * POST method for Vimeo API
         *
         * Adds a new resource to a collection. This method generally requires additional parameters.
         *
         * @param T the type of response
         * @param R the type of body
         * @property url the url of API
         * @property accessToken the token of authorization user in Vimeo
         * @property body the body in http method
         * @throws VideoResponseException
         * */
        @Throws(VideoResponseException::class)
        inline fun <R, reified T : Any> post(url: String, apiKey: String, accessKey: String, body: R? = null): T? {
            val restTemplate = RestTemplate()
            val headers = HttpHeaders()
            headers[HttpHeaders.CONTENT_TYPE] = "application/json"
            headers["wsc-api-key"] = apiKey
            headers["wsc-access-key"] = accessKey

            val request = HttpEntity<R>(body, headers)
            try {
                val response = restTemplate.exchange(url, HttpMethod.POST, request, T::class.java)
                if (response.statusCode.isError) throw VideoResponseException()
                return response.body
            } catch (e: Throwable) {
                throw VideoResponseException(e.message)
            }
        }

        /**
         * PUT method for Vimeo API
         *
         * Replaces a resource or associates one resource with another.
         *
         * @param T the type of response
         * @param R the type of body
         * @property url the url of API
         * @property accessToken the token of authorization user in Vimeo
         * @property body the body in http method
         * @throws VideoResponseException
         * */
        @Throws(VideoResponseException::class)
        inline fun <R, reified T : Any> put(url: String, apiKey: String, accessKey: String, body: R? = null): T? {
            val restTemplate = RestTemplate()
            val headers = HttpHeaders()
            headers[HttpHeaders.CONTENT_TYPE] = "application/json"
            headers["wsc-api-key"] = apiKey
            headers["wsc-access-key"] = accessKey

            val request = HttpEntity<R>(body, headers)
            try {
                val response = restTemplate.exchange(url, HttpMethod.PUT, request, T::class.java)
                if (response.statusCode.isError) throw VideoResponseException()
                return response.body
            } catch (e: Throwable) {
                throw VideoResponseException(e.message)
            }
        }

//        /**
//         * PATCH method for Vimeo API
//         *
//         * Modifies a resource. The body of the request contains the new resource representation.
//         *
//         * @param T the type of response
//         * @param R the type of body
//         * @property url the url of API
//         * @property accessToken the token of authorization user in Vimeo
//         * @property body the body in http method
//         * @throws VideoResponseException
//         * */
//        @Throws(VideoResponseException::class)
//        inline fun <R, reified T : Any> patch(url: String, accessToken: String, body: R? = null): T? {
//            val restTemplate = RestTemplate()
//            val requestFactory = HttpComponentsClientHttpRequestFactory()
//            restTemplate.requestFactory = requestFactory
//            val headers = HttpHeaders()
//            val token = "bearer $accessToken"
//            headers[HttpHeaders.AUTHORIZATION] = token
//            headers[HttpHeaders.ACCEPT] = VIMEO_ACCEPT
//            val request = HttpEntity<R>(body, headers)
//            try {
//                val response = restTemplate.exchange(url, HttpMethod.PATCH, request, T::class.java)
//                if (response.statusCode.isError) throw VideoResponseException("Ошибка при зарпосе к сервису Vimeo")
//                return response.body
//            } catch (e: Throwable) {
//                throw VideoResponseException(e.message)
//            }
//        }
//
//        /**
//         * DELETE method for Vimeo API
//         *
//         * Deletes a resource or disassociates one resource from another.
//         *
//         * @property url the url of API
//         * @property accessToken the token of authorization user in Vimeo
//         * @throws VideoResponseException
//         * */
//        @Throws(VideoResponseException::class)
//        fun delete(url: String, accessToken: String) {
//            val restTemplate = RestTemplate()
//            val headers = HttpHeaders()
//            val token = "bearer $accessToken"
//            headers[HttpHeaders.AUTHORIZATION] = token
//            headers[HttpHeaders.ACCEPT] = VIMEO_ACCEPT
//            val request = HttpEntity<Unit>(headers)
//            try {
//                val response = restTemplate.exchange(url, HttpMethod.DELETE, request, Unit::class.java)
//                if (response.statusCode.isError) throw VideoResponseException("Ошибка при зарпосе к сервису Vimeo")
//            } catch (e: Throwable) {
//                throw VideoResponseException(e.message)
//            }
//        }
    }


}
package com.raillylinker.springboot_webflux_template.data_sources.network_sources

import com.fasterxml.jackson.annotation.JsonProperty
import com.raillylinker.springboot_webflux_template.custom_classes.CustomDataClasses
import com.raillylinker.springboot_webflux_template.custom_objects.CustomUtilObject
import io.netty.channel.ChannelOption
import io.netty.handler.timeout.ReadTimeoutHandler
import io.netty.handler.timeout.WriteTimeoutHandler
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.ClientRequest
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.ExchangeFilterFunction
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import reactor.netty.Connection
import reactor.netty.http.client.HttpClient
import java.time.Duration
import java.util.concurrent.TimeUnit
import java.util.function.Consumer

object FcmGoogleapisComRequestApi {
    private val webClient: WebClient = WebClient.builder()
        .baseUrl("https://fcm.googleapis.com")
        .clientConnector(
            ReactorClientHttpConnector(HttpClient.create()
                // 리다이렉트 결과물 받아오기 설정
                .followRedirect(true) { headers, request ->
                    request.headers(headers)
                }
                // 타임아웃 설정
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .responseTimeout(Duration.ofMillis(5000))
                .doOnConnected { conn: Connection ->
                    conn.addHandlerLast(ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS))
                        .addHandlerLast(WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS))
                })
        )
        //Request Header 로깅 필터
        .filter(
            ExchangeFilterFunction.ofRequestProcessor { clientRequest: ClientRequest ->
                val classLogger: Logger = LoggerFactory.getLogger(this::class.java)
                classLogger.info(">>>>>>>>> WebClient REQUEST <<<<<<<<<<")
                classLogger.info(">>>WebClient<<< Request: ${clientRequest.method()} ${clientRequest.url()}")
                clientRequest.headers().forEach { name: String?, values: List<String?> ->
                    values.forEach(
                        Consumer<String?> { value: String? ->
                            classLogger.info(">>>WebClient<<< $name : $value")
                        })
                }
                Mono.just(clientRequest)
            }
        )
        //Response Header 로깅 필터
        .filter(
            ExchangeFilterFunction.ofResponseProcessor { clientResponse: ClientResponse ->
                val classLogger: Logger = LoggerFactory.getLogger(this::class.java)
                classLogger.info(">>>>>>>>>> WebClient RESPONSE <<<<<<<<<<")
                clientResponse.headers().asHttpHeaders().forEach { name: String?, values: List<String?> ->
                    values.forEach(
                        Consumer<String?> { value: String? ->
                            classLogger.info(">>>WebClient<<< $name : $value")
                        })
                }
                Mono.just(clientResponse)
            }
        )
        .build()

    ////
    // [FCM 메세지 보내기]
    fun postFcmSend(
        requestHeaderVo: PostFcmSendRequestHeaderVo,
        requestBodyVo: PostFcmSendRequestBodyVo
    ): Mono<CustomDataClasses.WebClientResponse<Unit?>> {

        return webClient.post()
            .uri { uriBuilder ->
                CustomUtilObject.buildToUri(
                    uriBuilder,
                    "/fcm/send",
                    null
                )
            }
            .headers {
                it.addAll(HttpHeaders().apply {
                    this.add("Authorization", requestHeaderVo.authorization)
                })
            }
            .bodyValue(requestBodyVo)
            .exchangeToMono { response ->
                Mono.just(
                    CustomDataClasses.WebClientResponse(
                        response,
                        null
                    )
                )
            }
    }

    data class PostFcmSendRequestHeaderVo(
        val authorization: String // ex : "key=$serverKey"
    )

    data class PostFcmSendRequestBodyVo(
        @JsonProperty("registration_ids")
        val registrationIds: List<String>, // 메세지 수신자의 FCM 토큰 리스트
        @JsonProperty("priority")
        val priority: String, // 기본 "high"
        @JsonProperty("content_available")
        val contentAvailable: Boolean, // 기본 true
        @JsonProperty("notification")
        val notification: Notification?,
        @JsonProperty("data")
        val data: Map<String, Any?>?
    ) {
        data class Notification(
            @JsonProperty("title")
            val title: String,
            @JsonProperty("body")
            val body: String
        )
    }
}
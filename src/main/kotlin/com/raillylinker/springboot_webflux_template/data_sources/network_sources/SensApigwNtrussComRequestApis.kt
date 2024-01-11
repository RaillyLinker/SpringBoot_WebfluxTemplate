package com.raillylinker.springboot_webflux_template.data_sources.network_sources

import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
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

object SensApigwNtrussComRequestApis {
    private val webClient: WebClient = WebClient.builder()
        .baseUrl("https://sens.apigw.ntruss.com")
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
    // [Naver SMS 발송]
    fun postSmsV2ServicesNaverSmsServiceIdMessages(
        requestPathVo: PostService1TkV1RequestTestPostRequestApplicationJsonRequestPathParamVo,
        requestHeaderVo: PostService1TkV1RequestTestPostRequestApplicationJsonRequestHeaderVo,
        requestBodyVo: PostService1TkV1RequestTestPostRequestApplicationJsonRequestBodyVo
    ): Mono<CustomDataClasses.WebClientResponse<Unit?>> {

        return webClient.post()
            .uri { uriBuilder ->
                CustomUtilObject.buildToUri(
                    uriBuilder,
                    "/sms/v2/services/${requestPathVo.naverSmsServiceId}/messages",
                    null
                )
            }
            .headers {
                it.addAll(HttpHeaders().apply {
                    this.add("x-ncp-apigw-timestamp", requestHeaderVo.xNcpApigwTimestamp)
                    this.add("x-ncp-iam-access-key", requestHeaderVo.xNcpIamAccessKey)
                    this.add("x-ncp-apigw-signature-v2", requestHeaderVo.xNcpApigwSignatureV2)
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

    data class PostService1TkV1RequestTestPostRequestApplicationJsonRequestPathParamVo(
        val naverSmsServiceId: String
    )

    data class PostService1TkV1RequestTestPostRequestApplicationJsonRequestHeaderVo(
        val xNcpApigwTimestamp: String,
        val xNcpIamAccessKey: String,
        val xNcpApigwSignatureV2: String
    )

    data class PostService1TkV1RequestTestPostRequestApplicationJsonRequestBodyVo(
        @JsonProperty("type")
        val type: String,
        @JsonProperty("contentType")
        val contentType: String,
        @JsonProperty("countryCode")
        val countryCode: String,
        @JsonProperty("from")
        val from: String,
        @JsonProperty("content")
        val content: String,
        @JsonProperty("messages")
        val messages: List<MessageVo>
    ) {
        data class MessageVo(
            @JsonProperty("to")
            var to: String,
            @JsonProperty("content")
            val content: String
        )
    }
}
package com.raillylinker.springboot_webflux_template.filters

import org.reactivestreams.Publisher
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.http.HttpMethod
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.http.server.reactive.ServerHttpRequestDecorator
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.http.server.reactive.ServerHttpResponseDecorator
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.ServerWebExchangeDecorator
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.io.ByteArrayOutputStream
import java.nio.channels.Channels
import java.util.*

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
class LoggingFilter(
    @Value("\${customConfig.loggingDenyIpList:}#{T(java.util.Collections).emptyList()}")
    private var loggingDenyIpList: List<String>
) : WebFilter {
    private val classLogger: Logger = LoggerFactory.getLogger(this::class.java)

    // (로깅 필터 구현 함수)
    override fun filter(serverWebExchange: ServerWebExchange, webFilterChain: WebFilterChain): Mono<Void> {
        if (serverWebExchange.request.remoteAddress?.address?.hostAddress in loggingDenyIpList) {
            // loggingDenyIpList 에 포함된 IP 주소일 경우 로깅을 하지 않고 바로 다음 필터로 넘어감
            return webFilterChain.filter(serverWebExchange)
        }

        val logUuid = UUID.randomUUID().toString()

        // 요청 로깅
        return webFilterChain.filter(LoggingWebExchange(logUuid, classLogger, serverWebExchange))
            .doFinally {
                val responseHeaders = serverWebExchange.response.headers

                if (responseHeaders.contentLength == 0L) {
                    classLogger.info(
                        "\n----------------------------------------------------------------------\n" +
                                "logUuid : $logUuid,\n" +
                                "type : response ok,\n" +
                                "headers : $responseHeaders,\n" +
                                "body : null\n" +
                                "----------------------------------------------------------------------\n"
                    )
                }
            }
            .doOnError { throwable ->
                // 예외가 발생하면 에러 로그를 남깁니다.
                classLogger.info(
                    "\n----------------------------------------------------------------------\n" +
                            "logUuid : $logUuid,\n" +
                            "type : response error,\n" +
                            "error : ${throwable.javaClass.simpleName},\n" +
                            "message : ${throwable.message}\n" +
                            "----------------------------------------------------------------------\n"
                )
            }
    }

    class LoggingWebExchange(logUUID: String, logger: Logger, serverWebExchange: ServerWebExchange) :
        ServerWebExchangeDecorator(serverWebExchange) {
        private val requestDecorator = LoggingRequestDecorator(logUUID, logger, serverWebExchange.request)
        private val responseDecorator = LoggingResponseDecorator(logUUID, logger, serverWebExchange.response)
        override fun getRequest(): ServerHttpRequest {
            return requestDecorator
        }

        override fun getResponse(): ServerHttpResponse {
            return responseDecorator
        }
    }

    class LoggingRequestDecorator internal constructor(
        logUUID: String,
        logger: Logger,
        serverHttpRequest: ServerHttpRequest
    ) : ServerHttpRequestDecorator(serverHttpRequest) {
        private val requestBodyFlux: Flux<DataBuffer>

        init {
            val path = serverHttpRequest.uri.path
            val query = serverHttpRequest.uri.query
            val method = Optional.ofNullable(serverHttpRequest.method).orElse(HttpMethod.GET).name()
            val headers = serverHttpRequest.headers
            val bodyStringBuilder = StringBuilder()
            requestBodyFlux = if (serverHttpRequest.headers.contentLength > 0) {
                super.getBody().doOnNext { buffer: DataBuffer ->
                    val bodyStream = ByteArrayOutputStream()
                    Channels.newChannel(bodyStream).write(buffer.readableByteBuffers().next())
                    bodyStringBuilder.append(String(bodyStream.toByteArray()))
                }.doFinally {
                    logger.info(
                        "\n----------------------------------------------------------------------\n" +
                                "logUuid : $logUUID,\n" +
                                "type : request $method,\n" +
                                "path : ${path + (if (StringUtils.hasText(query)) "?$query" else "")},\n" +
                                "headers : $headers,\n" +
                                "body : $bodyStringBuilder\n" +
                                "----------------------------------------------------------------------\n"
                    )
                }
            } else {
                logger.info(
                    "\n----------------------------------------------------------------------\n" +
                            "logUuid : $logUUID,\n" +
                            "type : request $method,\n" +
                            "path : ${path + (if (StringUtils.hasText(query)) "?$query" else "")},\n" +
                            "headers : $headers,\n" +
                            "body : null\n" +
                            "----------------------------------------------------------------------\n"
                )
                Flux.empty()
            }
        }

        override fun getBody(): Flux<DataBuffer> {
            return requestBodyFlux
        }
    }

    class LoggingResponseDecorator internal constructor(
        private val logUUID: String,
        private val logger: Logger,
        serverHttpResponse: ServerHttpResponse
    ) :
        ServerHttpResponseDecorator(serverHttpResponse) {
        override fun writeWith(body: Publisher<out DataBuffer>): Mono<Void> {
            var bodyString: String? = null
            return super.writeWith(Flux.from(body)
                .doOnNext { buffer: DataBuffer ->
                    val bodyStream = ByteArrayOutputStream()
                    Channels.newChannel(bodyStream).write(buffer.readableByteBuffers().next())
                    bodyString = String(bodyStream.toByteArray())
                }).doFinally {
                logger.info(
                    "\n----------------------------------------------------------------------\n" +
                            "logUuid : $logUUID,\n" +
                            "type : response ok,\n" +
                            "status : ${delegate.statusCode},\n" +
                            "headers : ${delegate.headers},\n" +
                            "body : $bodyString\n" +
                            "----------------------------------------------------------------------\n"
                )
            }
        }
    }
}

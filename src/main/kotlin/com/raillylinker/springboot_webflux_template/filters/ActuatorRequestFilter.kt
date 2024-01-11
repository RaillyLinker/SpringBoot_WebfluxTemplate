package com.raillylinker.springboot_webflux_template.filters

import org.springframework.beans.factory.annotation.Value
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
class ActuatorRequestFilter(
    @Value("\${customConfig.actuatorAllowIpList:}#{T(java.util.Collections).emptyList()}")
    private var actuatorAllowIpList: List<String>
) : WebFilter {
    override fun filter(
        exchange: ServerWebExchange,
        chain: WebFilterChain
    ): Mono<Void> {
        // /actuator 로 시작되는 요청은 allowedIps 에 설정된 IP 만 허용
        return if (exchange.request.path.pathWithinApplication().value().startsWith("/actuator") &&
            exchange.request.remoteAddress?.address?.hostAddress !in actuatorAllowIpList
        ) {
            ServerResponse.status(404).build().then()
        } else {
            chain.filter(exchange)
        }
    }
}
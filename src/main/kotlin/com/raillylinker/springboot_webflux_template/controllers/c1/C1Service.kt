package com.raillylinker.springboot_webflux_template.controllers.c1

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.stereotype.Service
import org.springframework.web.reactive.result.view.Rendering
import reactor.core.publisher.Mono

@Service
class C1Service(
    // (프로젝트 실행시 사용 설정한 프로필명 (ex : dev8080, prod80, local8080, 설정 안하면 default 반환))
    @Value("\${spring.profiles.active:default}") private var activeProfile: String
) {
    // <멤버 변수 공간>
    private val classLogger: Logger = LoggerFactory.getLogger(this::class.java)


    // ---------------------------------------------------------------------------------------------
    // <공개 메소드 공간>
    fun api1(
        serverHttpResponse: ServerHttpResponse
    ): Mono<Rendering> {

        return Mono.create { sink ->
            serverHttpResponse.setStatusCode(HttpStatus.OK)
            serverHttpResponse.headers.set("api-result-code", "0")

            sink.success(
                Rendering.view("template_c1_n1/home_page")
                    .modelAttribute(
                        "viewModel", Api1ViewModel(
                            activeProfile
                        )
                    ).build()
            )
        }
    }

    data class Api1ViewModel(
        val env: String
    )
}
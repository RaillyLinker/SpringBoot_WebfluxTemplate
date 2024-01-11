package com.raillylinker.springboot_webflux_template.custom_classes

import org.springframework.web.reactive.function.client.ClientResponse

class CustomDataClasses {
    // (WebClient 응답 클래스)
    data class WebClientResponse<ResponseBodyClass>(
        val clientResponse: ClientResponse,
        val responseBody: ResponseBodyClass
    )
}
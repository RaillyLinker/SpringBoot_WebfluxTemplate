package com.raillylinker.springboot_webflux_template

import org.springframework.boot.SpringApplication
import org.springframework.boot.WebApplicationType
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling // 스케쥴러 사용 설정
@EnableAsync // 스케쥴러의 Async 사용 설정
@SpringBootApplication
class ApplicationMain

fun main(args: Array<String>) {
    val application = SpringApplication(ApplicationMain::class.java)

    // Application Reactive 모드 실행
    application.webApplicationType = WebApplicationType.REACTIVE

    application.run(*args)
}

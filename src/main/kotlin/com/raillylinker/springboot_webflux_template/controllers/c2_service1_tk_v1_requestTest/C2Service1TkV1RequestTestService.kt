package com.raillylinker.springboot_webflux_template.controllers.c2_service1_tk_v1_requestTest

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.Resource
import org.springframework.http.HttpStatus
import org.springframework.http.codec.ServerSentEvent
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.stereotype.Service
import org.springframework.util.FileCopyUtils
import org.springframework.web.reactive.result.view.Rendering
import reactor.core.publisher.*
import java.io.File
import java.io.FileInputStream
import java.net.URI
import java.nio.file.Paths
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@Service
class C2Service1TkV1RequestTestService(
    // (프로젝트 실행시 사용 설정한 프로필명 (ex : dev8080, prod80, local8080, 설정 안하면 default 반환))
    @Value("\${spring.profiles.active:default}") private var activeProfile: String
) {
    // <멤버 변수 공간>
    private val classLogger: Logger = LoggerFactory.getLogger(this::class.java)


    // ---------------------------------------------------------------------------------------------
    // <공개 메소드 공간>
    fun api1(
        serverHttpResponse: ServerHttpResponse
    ): Mono<String> {
        serverHttpResponse.setStatusCode(HttpStatus.OK)
        serverHttpResponse.headers.set("api-result-code", "0")

        return Mono.just(activeProfile)
    }

    ////
    fun api2(serverHttpResponse: ServerHttpResponse): Mono<Void> {
        serverHttpResponse.setStatusCode(HttpStatus.PERMANENT_REDIRECT)
        serverHttpResponse.headers.location = URI.create("/service1/tk/v1/request-test")

        return serverHttpResponse.setComplete()
    }

    ////
    fun api3(
        serverHttpResponse: ServerHttpResponse,
        queryParamString: String,
        queryParamStringNullable: String?,
        queryParamInt: Int,
        queryParamIntNullable: Int?,
        queryParamDouble: Double,
        queryParamDoubleNullable: Double?,
        queryParamBoolean: Boolean,
        queryParamBooleanNullable: Boolean?,
        queryParamStringList: List<String>,
        queryParamStringListNullable: List<String>?
    ): Mono<C2Service1TkV1RequestTestController.Api3OutputVo> {
        serverHttpResponse.setStatusCode(HttpStatus.OK)
        serverHttpResponse.headers.set("api-result-code", "0")

        return Mono.just(
            C2Service1TkV1RequestTestController.Api3OutputVo(
                queryParamString,
                queryParamStringNullable,
                queryParamInt,
                queryParamIntNullable,
                queryParamDouble,
                queryParamDoubleNullable,
                queryParamBoolean,
                queryParamBooleanNullable,
                queryParamStringList,
                queryParamStringListNullable
            )
        )
    }

    ////
    fun api4(
        serverHttpResponse: ServerHttpResponse,
        pathParamInt: Int
    ): Mono<C2Service1TkV1RequestTestController.Api4OutputVo> {
        serverHttpResponse.setStatusCode(HttpStatus.OK)
        serverHttpResponse.headers.set("api-result-code", "0")

        return Mono.just(
            C2Service1TkV1RequestTestController.Api4OutputVo(
                pathParamInt
            )
        )
    }

    ////
    fun api5(
        serverHttpResponse: ServerHttpResponse, inputVo: C2Service1TkV1RequestTestController.Api5InputVo
    ): Mono<C2Service1TkV1RequestTestController.Api5OutputVo> {
        serverHttpResponse.setStatusCode(HttpStatus.OK)
        serverHttpResponse.headers.set("api-result-code", "0")

        return Mono.just(
            C2Service1TkV1RequestTestController.Api5OutputVo(
                inputVo.requestBodyString,
                inputVo.requestBodyStringNullable,
                inputVo.requestBodyInt,
                inputVo.requestBodyIntNullable,
                inputVo.requestBodyDouble,
                inputVo.requestBodyDoubleNullable,
                inputVo.requestBodyBoolean,
                inputVo.requestBodyBooleanNullable,
                inputVo.requestBodyStringList,
                inputVo.requestBodyStringListNullable
            )
        )
    }

    ////
    fun api6(
        serverHttpResponse: ServerHttpResponse, inputVo: C2Service1TkV1RequestTestController.Api6InputVo
    ): Mono<C2Service1TkV1RequestTestController.Api6OutputVo> {
        serverHttpResponse.setStatusCode(HttpStatus.OK)
        serverHttpResponse.headers.set("api-result-code", "0")

        return Mono.just(
            C2Service1TkV1RequestTestController.Api6OutputVo(
                inputVo.requestFormString,
                inputVo.requestFormStringNullable,
                inputVo.requestFormInt,
                inputVo.requestFormIntNullable,
                inputVo.requestFormDouble,
                inputVo.requestFormDoubleNullable,
                inputVo.requestFormBoolean,
                inputVo.requestFormBooleanNullable,
                inputVo.requestFormStringList,
                inputVo.requestFormStringListNullable
            )
        )
    }

    ////
    fun api7(
        serverHttpResponse: ServerHttpResponse, inputVoMono: Mono<C2Service1TkV1RequestTestController.Api7InputVo>
    ): Mono<C2Service1TkV1RequestTestController.Api7OutputVo> {
        return inputVoMono.flatMap { inputVo ->
            // 비동기적으로 파일 저장
            val saveFile1 = inputVo.multipartFile.transferTo(
                Paths.get(
                    "./files/temp/${
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss_SSS"))
                    }_${inputVo.multipartFile.filename()}"
                )
            )

            val saveFile2 = inputVo.multipartFileNullable?.let { nullableFile ->
                // 비동기적으로 파일 저장
                nullableFile.transferTo(
                    Paths.get(
                        "./files/temp/${
                            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss_SSS"))
                        }_${nullableFile.filename()}"
                    )
                )
            } ?: Mono.empty()

            // saveFile1, saveFile2 의 비동기 작업이 모두 끝났을 때
            serverHttpResponse.setStatusCode(HttpStatus.OK)
            serverHttpResponse.headers.set("api-result-code", "0")

            saveFile1.and(saveFile2).then(
                Mono.just(
                    C2Service1TkV1RequestTestController.Api7OutputVo(
                        inputVo.requestFormString,
                        inputVo.requestFormStringNullable,
                        inputVo.requestFormInt,
                        inputVo.requestFormIntNullable,
                        inputVo.requestFormDouble,
                        inputVo.requestFormDoubleNullable,
                        inputVo.requestFormBoolean,
                        inputVo.requestFormBooleanNullable,
                        inputVo.requestFormStringList,
                        inputVo.requestFormStringListNullable
                    )
                )
            )
        }
    }

    ////
    fun api8(
        serverHttpResponse: ServerHttpResponse, inputVoMono: Mono<C2Service1TkV1RequestTestController.Api8InputVo>
    ): Mono<C2Service1TkV1RequestTestController.Api8OutputVo> {
        return inputVoMono.flatMap { inputVo ->
            // 비동기적으로 파일 저장
            val saveFiles = inputVo.multipartFileList.map { filePart ->
                filePart.transferTo(
                    Paths.get(
                        "./files/temp/${
                            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss_SSS"))
                        }_${filePart.filename()}"
                    )
                )
            }

            val saveNullableFiles = inputVo.multipartFileListNullable?.map { nullableFile ->
                nullableFile.transferTo(
                    Paths.get(
                        "./files/temp/${
                            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss_SSS"))
                        }_${nullableFile.filename()}"
                    )
                )
            } ?: emptyList()

            serverHttpResponse.setStatusCode(HttpStatus.OK)
            serverHttpResponse.headers.set("api-result-code", "0")

            // 모든 파일이 저장된 후에 계속 진행
            Flux.concat(saveFiles + saveNullableFiles)
                .then(
                    Mono.just(
                        C2Service1TkV1RequestTestController.Api8OutputVo(
                            inputVo.requestFormString,
                            inputVo.requestFormStringNullable,
                            inputVo.requestFormInt,
                            inputVo.requestFormIntNullable,
                            inputVo.requestFormDouble,
                            inputVo.requestFormDoubleNullable,
                            inputVo.requestFormBoolean,
                            inputVo.requestFormBooleanNullable,
                            inputVo.requestFormStringList,
                            inputVo.requestFormStringListNullable
                        )
                    )
                )
        }
    }

    ////
    fun api9(
        serverHttpResponse: ServerHttpResponse, inputVoMono: Mono<C2Service1TkV1RequestTestController.Api9InputVo>
    ): Mono<C2Service1TkV1RequestTestController.Api9OutputVo> {
        return inputVoMono.flatMap { inputVo ->
            // 비동기적으로 파일 저장
            val saveFile1 = inputVo.multipartFile.transferTo(
                Paths.get(
                    "./files/temp/${
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss_SSS"))
                    }_${inputVo.multipartFile.filename()}"
                )
            )

            val saveFile2 = inputVo.multipartFileNullable?.let { nullableFile ->
                // 비동기적으로 파일 저장
                nullableFile.transferTo(
                    Paths.get(
                        "./files/temp/${
                            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss_SSS"))
                        }_${nullableFile.filename()}"
                    )
                )
            } ?: Mono.empty()

            // saveFile1, saveFile2 의 비동기 작업이 모두 끝났을 때

            // input Json String to Object
            val inputJsonObject = Gson().fromJson<C2Service1TkV1RequestTestController.Api9InputVo.InputJsonObject>(
                inputVo.jsonString, // 해석하려는 json 형식의 String
                object :
                    TypeToken<C2Service1TkV1RequestTestController.Api9InputVo.InputJsonObject>() {}.type // 파싱할 데이터 객체 타입
            )

            serverHttpResponse.setStatusCode(HttpStatus.OK)
            serverHttpResponse.headers.set("api-result-code", "0")

            saveFile1.and(saveFile2).then(
                Mono.just(
                    C2Service1TkV1RequestTestController.Api9OutputVo(
                        inputJsonObject.requestFormString,
                        inputJsonObject.requestFormStringNullable,
                        inputJsonObject.requestFormInt,
                        inputJsonObject.requestFormIntNullable,
                        inputJsonObject.requestFormDouble,
                        inputJsonObject.requestFormDoubleNullable,
                        inputJsonObject.requestFormBoolean,
                        inputJsonObject.requestFormBooleanNullable,
                        inputJsonObject.requestFormStringList,
                        inputJsonObject.requestFormStringListNullable
                    )
                )
            )
        }
    }

    ////
    fun api10(
        serverHttpResponse: ServerHttpResponse
    ): Mono<Void> {
        throw RuntimeException("Test Error")
    }

    ////
    fun api11(
        serverHttpResponse: ServerHttpResponse, errorType: C2Service1TkV1RequestTestController.Api12ErrorTypeEnum?
    ): Mono<Void> {
        return if (errorType == null) {
            serverHttpResponse.setStatusCode(HttpStatus.OK)
            serverHttpResponse.headers.set("api-result-code", "0")

            serverHttpResponse.setComplete()
        } else {
            when (errorType) {
                C2Service1TkV1RequestTestController.Api12ErrorTypeEnum.A -> {
                    serverHttpResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                    serverHttpResponse.headers.set("api-result-code", "1")

                    serverHttpResponse.setComplete()
                }

                C2Service1TkV1RequestTestController.Api12ErrorTypeEnum.B -> {
                    serverHttpResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                    serverHttpResponse.headers.set("api-result-code", "2")

                    serverHttpResponse.setComplete()
                }

                C2Service1TkV1RequestTestController.Api12ErrorTypeEnum.C -> {
                    serverHttpResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                    serverHttpResponse.headers.set("api-result-code", "3")

                    serverHttpResponse.setComplete()
                }
            }
        }
    }

    ////
    fun api12(
        serverHttpResponse: ServerHttpResponse, delayTimeSec: Long
    ): Mono<Void> {
        serverHttpResponse.setStatusCode(HttpStatus.OK)
        serverHttpResponse.headers.set("api-result-code", "0")

        return Mono.delay(Duration.ofSeconds(delayTimeSec)).then(serverHttpResponse.setComplete())
    }

    ////
    fun api13(
        serverHttpResponse: ServerHttpResponse
    ): Mono<String> {
        serverHttpResponse.setStatusCode(HttpStatus.OK)
        serverHttpResponse.headers.set("api-result-code", "0")

        return Mono.just("test Complete!")
    }

    ////
    fun api14(
        serverHttpResponse: ServerHttpResponse
    ): Mono<Rendering> {
        serverHttpResponse.setStatusCode(HttpStatus.OK)
        serverHttpResponse.headers.set("api-result-code", "0")

        return Mono.just(
            Rendering.view("template_c2_n14/html_response_example").build()
        )
    }

    ////
    fun api15(
        serverHttpResponse: ServerHttpResponse
    ): Mono<Resource> {
        serverHttpResponse.setStatusCode(HttpStatus.OK)
        serverHttpResponse.headers.set("api-result-code", "0")

        return Mono.just(
            // ByteArray 를 Resource 타입으로 변형하여 반환
            ByteArrayResource(
                byteArrayOf(
                    'a'.code.toByte(),
                    'b'.code.toByte(),
                    'c'.code.toByte(),
                    'd'.code.toByte(),
                    'e'.code.toByte(),
                    'f'.code.toByte()
                )
            )
        )
    }

    ////
    fun api16(
        serverHttpResponse: ServerHttpResponse, videoHeight: C2Service1TkV1RequestTestController.Api16VideoHeight
    ): Mono<Resource> {
        // 프로젝트 루트 경로 (프로젝트 settings.gradle 이 있는 경로)
        val projectRootAbsolutePathString: String = File("").absolutePath

        // 파일 절대 경로 및 파일명
        val serverFileAbsolutePathString = "$projectRootAbsolutePathString/src/main/resources/static/resource_c2_n16"

        // 멤버십 등의 정보로 해상도 제한을 걸 수도 있음
        val serverFileNameString =
            when (videoHeight) {
                C2Service1TkV1RequestTestController.Api16VideoHeight.H240 -> {
                    "test_240p.mp4"
                }

                C2Service1TkV1RequestTestController.Api16VideoHeight.H360 -> {
                    "test_360p.mp4"
                }

                C2Service1TkV1RequestTestController.Api16VideoHeight.H480 -> {
                    "test_480p.mp4"
                }

                C2Service1TkV1RequestTestController.Api16VideoHeight.H720 -> {
                    "test_720p.mp4"
                }
            }

        // 반환값에 전해줄 FIS
        val fileInputStream = FileInputStream("$serverFileAbsolutePathString/$serverFileNameString")

        serverHttpResponse.setStatusCode(HttpStatus.OK)
        serverHttpResponse.headers.set("api-result-code", "0")

        return Mono.just(
            // fileInputStream 을 Resource 타입으로 변형하여 반환
            ByteArrayResource(FileCopyUtils.copyToByteArray(fileInputStream))
        )
    }

    ////
    fun api17(
        serverHttpResponse: ServerHttpResponse
    ): Mono<Resource> {
        // 프로젝트 루트 경로 (프로젝트 settings.gradle 이 있는 경로)
        val projectRootAbsolutePathString: String = File("").absolutePath

        // 파일 절대 경로 및 파일명
        val serverFileAbsolutePathString = "$projectRootAbsolutePathString/src/main/resources/static/resource_c2_n17"
        val serverFileNameString = "test.mp3"

        // 반환값에 전해줄 FIS
        val fileInputStream = FileInputStream("$serverFileAbsolutePathString/$serverFileNameString")

        serverHttpResponse.setStatusCode(HttpStatus.OK)
        serverHttpResponse.headers.set("api-result-code", "0")

        return Mono.just(
            // fileInputStream 을 Resource 타입으로 변형하여 반환
            ByteArrayResource(FileCopyUtils.copyToByteArray(fileInputStream))
        )
    }

    ////
    private val sseStringChannel = Sinks.many().multicast().directAllOrNothing<String>()
    fun api18(
        serverHttpResponse: ServerHttpResponse
    ): Flux<ServerSentEvent<String>> {
        // 채널 연결 및 채널 객체 가져오기
        serverHttpResponse.setStatusCode(HttpStatus.OK)
        serverHttpResponse.headers.set("api-result-code", "0")
        return Flux.merge( // Flux Stream 병합
            // 접속 완료시 바로 반환
            Flux.just("[[CONNECT]]"),

            // 주기적 신호 반환
            Flux.interval(Duration.ofSeconds(3))
                .map { _: Long? ->
                    "[[HEARTBEAT]] ${
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss_SSS"))
                    }"
                },

            // 메인 SSE Stream
            sseStringChannel.asFlux()
        ).map { sseMsg: String ->
            // Flux Stream 에서 message String 이 올 때마다 응답 반환
            ServerSentEvent.builder(sseMsg).build()
        }
    }

    ////
    fun api19(
        serverHttpResponse: ServerHttpResponse
    ): Mono<Void> {
        sseStringChannel.tryEmitNext("message")

        serverHttpResponse.setStatusCode(HttpStatus.OK)
        serverHttpResponse.headers.set("api-result-code", "0")
        return serverHttpResponse.setComplete()
    }
}
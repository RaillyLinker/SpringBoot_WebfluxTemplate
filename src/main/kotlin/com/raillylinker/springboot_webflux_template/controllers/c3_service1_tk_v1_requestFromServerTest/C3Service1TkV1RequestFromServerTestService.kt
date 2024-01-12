package com.raillylinker.springboot_webflux_template.controllers.c3_service1_tk_v1_requestFromServerTest

import com.google.gson.Gson
import com.raillylinker.springboot_webflux_template.data_sources.network_sources.LocalHostApis
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.FileSystemResource
import org.springframework.http.HttpStatus
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.io.File
import java.nio.file.Paths

@Service
class C3Service1TkV1RequestFromServerTestService(
    // (프로젝트 실행시 사용 설정한 프로필명 (ex : dev8080, prod80, local8080, 설정 안하면 default 반환))
    @Value("\${spring.profiles.active:default}") private var activeProfile: String
) {
    // <멤버 변수 공간>
    private val classLogger: Logger = LoggerFactory.getLogger(this::class.java)


    // ---------------------------------------------------------------------------------------------
    // <공개 메소드 공간>
    fun api1(serverHttpResponse: ServerHttpResponse): Mono<String> {

        return LocalHostApis.getService1TkV1RequestTest().flatMap { response ->
            val httpStatus = response.clientResponse.statusCode()
            if (httpStatus.is2xxSuccessful) {
                serverHttpResponse.setStatusCode(HttpStatus.OK)
                serverHttpResponse.headers.set("api-result-code", "0")
                Mono.just(response.responseBody!!)
            } else {
                serverHttpResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                serverHttpResponse.headers.set("api-result-code", "2")
                Mono.empty()
            }
        }.onErrorResume {
            serverHttpResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR)
            serverHttpResponse.headers.set("api-result-code", "1")
            Mono.empty()
        }
    }

    ////
    fun api2(serverHttpResponse: ServerHttpResponse): Mono<String> {

        return LocalHostApis.getService1TkV1RequestTestRedirectToBlank().flatMap { response ->
            val httpStatus = response.clientResponse.statusCode()
            if (httpStatus.is2xxSuccessful) {
                serverHttpResponse.setStatusCode(HttpStatus.OK)
                serverHttpResponse.headers.set("api-result-code", "0")
                Mono.just(response.responseBody!!)
            } else {
                serverHttpResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                serverHttpResponse.headers.set("api-result-code", "2")
                Mono.empty()
            }
        }.onErrorResume {
            serverHttpResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR)
            serverHttpResponse.headers.set("api-result-code", "1")
            Mono.empty()
        }
    }

    ////
    fun api3(serverHttpResponse: ServerHttpResponse): Mono<C3Service1TkV1RequestFromServerTestController.Api3OutputVo> {

        return LocalHostApis.getService1TkV1RequestTestGetRequest(
            LocalHostApis.GetService1TkV1RequestTestGetRequestRequestQueryParamVo(
                "paramFromServer",
                null,
                1,
                null,
                1.1,
                null,
                true,
                null,
                listOf("paramFromServer", "paramFromServer"),
                null
            )
        )
            .flatMap { response ->
                val httpStatus = response.clientResponse.statusCode()
                if (httpStatus.is2xxSuccessful) {
                    serverHttpResponse.setStatusCode(HttpStatus.OK)
                    serverHttpResponse.headers.set("api-result-code", "0")
                    Mono.just(
                        C3Service1TkV1RequestFromServerTestController.Api3OutputVo(
                            response.responseBody!!.queryParamString,
                            response.responseBody.queryParamStringNullable,
                            response.responseBody.queryParamInt,
                            response.responseBody.queryParamIntNullable,
                            response.responseBody.queryParamDouble,
                            response.responseBody.queryParamDoubleNullable,
                            response.responseBody.queryParamBoolean,
                            response.responseBody.queryParamBooleanNullable,
                            response.responseBody.queryParamStringList,
                            response.responseBody.queryParamStringListNullable
                        )
                    )
                } else {
                    serverHttpResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                    serverHttpResponse.headers.set("api-result-code", "2")
                    Mono.empty()
                }
            }.onErrorResume {
                serverHttpResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                serverHttpResponse.headers.set("api-result-code", "1")
                Mono.empty()
            }
    }

    fun api4(serverHttpResponse: ServerHttpResponse): Mono<C3Service1TkV1RequestFromServerTestController.Api4OutputVo> {

        return LocalHostApis.getService1TkV1RequestTestGetRequestPathParamInt(
            LocalHostApis.GetService1TkV1RequestTestGetRequestPathParamIntRequestPathParamVo(
                1234
            )
        ).flatMap { response ->
            val httpStatus = response.clientResponse.statusCode()
            if (httpStatus.is2xxSuccessful) {
                serverHttpResponse.setStatusCode(HttpStatus.OK)
                serverHttpResponse.headers.set("api-result-code", "0")
                Mono.just(
                    C3Service1TkV1RequestFromServerTestController.Api4OutputVo(
                        response.responseBody!!.pathParamInt
                    )
                )
            } else {
                serverHttpResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                serverHttpResponse.headers.set("api-result-code", "2")
                Mono.empty()
            }
        }.onErrorResume {
            serverHttpResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR)
            serverHttpResponse.headers.set("api-result-code", "1")
            Mono.empty()
        }
    }

    ////
    fun api5(serverHttpResponse: ServerHttpResponse): Mono<C3Service1TkV1RequestFromServerTestController.Api5OutputVo> {

        return LocalHostApis.postService1TkV1RequestTestPostRequestApplicationJson(
            LocalHostApis.PostService1TkV1RequestTestPostRequestApplicationJsonRequestBodyVo(
                "paramFromServer",
                null,
                1,
                null,
                1.1,
                null,
                true,
                null,
                listOf("paramFromServer", "paramFromServer"),
                null
            )
        )
            .flatMap { response ->
                val httpStatus = response.clientResponse.statusCode()
                if (httpStatus.is2xxSuccessful) {
                    serverHttpResponse.setStatusCode(HttpStatus.OK)
                    serverHttpResponse.headers.set("api-result-code", "0")
                    Mono.just(
                        C3Service1TkV1RequestFromServerTestController.Api5OutputVo(
                            response.responseBody!!.requestBodyString,
                            response.responseBody.requestBodyStringNullable,
                            response.responseBody.requestBodyInt,
                            response.responseBody.requestBodyIntNullable,
                            response.responseBody.requestBodyDouble,
                            response.responseBody.requestBodyDoubleNullable,
                            response.responseBody.requestBodyBoolean,
                            response.responseBody.requestBodyBooleanNullable,
                            response.responseBody.requestBodyStringList,
                            response.responseBody.requestBodyStringListNullable
                        )
                    )
                } else {
                    serverHttpResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                    serverHttpResponse.headers.set("api-result-code", "2")
                    Mono.empty()
                }
            }.onErrorResume {
                serverHttpResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                serverHttpResponse.headers.set("api-result-code", "1")
                Mono.empty()
            }
    }

    ////
    fun api6(serverHttpResponse: ServerHttpResponse): Mono<C3Service1TkV1RequestFromServerTestController.Api6OutputVo> {

        return LocalHostApis.postService1TkV1RequestTestPostRequestXWwwFormUrlencoded(
            LocalHostApis.PostService1TkV1RequestTestPostRequestXWwwFormUrlencodedRequestFormVo(
                "paramFromServer",
                null,
                1,
                null,
                1.1,
                null,
                true,
                null,
                listOf("paramFromServer", "paramFromServer"),
                null
            )
        ).flatMap { response ->
            val httpStatus = response.clientResponse.statusCode()
            if (httpStatus.is2xxSuccessful) {
                serverHttpResponse.setStatusCode(HttpStatus.OK)
                serverHttpResponse.headers.set("api-result-code", "0")
                Mono.just(
                    C3Service1TkV1RequestFromServerTestController.Api6OutputVo(
                        response.responseBody!!.requestFormString,
                        response.responseBody.requestFormStringNullable,
                        response.responseBody.requestFormInt,
                        response.responseBody.requestFormIntNullable,
                        response.responseBody.requestFormDouble,
                        response.responseBody.requestFormDoubleNullable,
                        response.responseBody.requestFormBoolean,
                        response.responseBody.requestFormBooleanNullable,
                        response.responseBody.requestFormStringList,
                        response.responseBody.requestFormStringListNullable
                    )
                )
            } else {
                serverHttpResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                serverHttpResponse.headers.set("api-result-code", "2")
                Mono.empty()
            }
        }.onErrorResume {
            serverHttpResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR)
            serverHttpResponse.headers.set("api-result-code", "1")
            Mono.empty()
        }
    }

    ////
    fun api7(serverHttpResponse: ServerHttpResponse): Mono<C3Service1TkV1RequestFromServerTestController.Api7OutputVo> {
        val serverFile =
            Paths.get("${File("").absolutePath}/src/main/resources/static/resource_c3_n7/test.txt")
                .toFile()

        return LocalHostApis.postService1TkV1RequestTestPostRequestMultipartFormData(
            LocalHostApis.PostService1TkV1RequestTestPostRequestMultipartFormDataRequestFormVo(
                "paramFromServer",
                null,
                1,
                null,
                1.1,
                null,
                true,
                null,
                listOf("paramFromServer", "paramFromServer"),
                null,
                FileSystemResource(serverFile),
                null
            )
        ).flatMap { response ->
            val httpStatus = response.clientResponse.statusCode()
            if (httpStatus.is2xxSuccessful) {
                serverHttpResponse.setStatusCode(HttpStatus.OK)
                serverHttpResponse.headers.set("api-result-code", "0")
                Mono.just(
                    C3Service1TkV1RequestFromServerTestController.Api7OutputVo(
                        response.responseBody!!.requestFormString,
                        response.responseBody.requestFormStringNullable,
                        response.responseBody.requestFormInt,
                        response.responseBody.requestFormIntNullable,
                        response.responseBody.requestFormDouble,
                        response.responseBody.requestFormDoubleNullable,
                        response.responseBody.requestFormBoolean,
                        response.responseBody.requestFormBooleanNullable,
                        response.responseBody.requestFormStringList,
                        response.responseBody.requestFormStringListNullable
                    )
                )
            } else {
                serverHttpResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                serverHttpResponse.headers.set("api-result-code", "2")
                Mono.empty()
            }
        }.onErrorResume {
            serverHttpResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR)
            serverHttpResponse.headers.set("api-result-code", "1")
            Mono.empty()
        }
    }

    ////
    fun api8(serverHttpResponse: ServerHttpResponse): Mono<C3Service1TkV1RequestFromServerTestController.Api8OutputVo> {
        val serverFile1 =
            Paths.get("${File("").absolutePath}/src/main/resources/static/resource_c3_n8/test1.txt")
                .toFile()
        val serverFile2 =
            Paths.get("${File("").absolutePath}/src/main/resources/static/resource_c3_n8/test2.txt")
                .toFile()

        return LocalHostApis.postService1TkV1RequestTestPostRequestMultipartFormData2(
            LocalHostApis.PostService1TkV1RequestTestPostRequestMultipartFormData2RequestFormVo(
                "paramFromServer",
                null,
                1,
                null,
                1.1,
                null,
                true,
                null,
                listOf("paramFromServer", "paramFromServer"),
                null,
                listOf(FileSystemResource(serverFile1), FileSystemResource(serverFile2)),
                null
            )
        ).flatMap { response ->
            val httpStatus = response.clientResponse.statusCode()
            if (httpStatus.is2xxSuccessful) {
                serverHttpResponse.setStatusCode(HttpStatus.OK)
                serverHttpResponse.headers.set("api-result-code", "0")
                Mono.just(
                    C3Service1TkV1RequestFromServerTestController.Api8OutputVo(
                        response.responseBody!!.requestFormString,
                        response.responseBody.requestFormStringNullable,
                        response.responseBody.requestFormInt,
                        response.responseBody.requestFormIntNullable,
                        response.responseBody.requestFormDouble,
                        response.responseBody.requestFormDoubleNullable,
                        response.responseBody.requestFormBoolean,
                        response.responseBody.requestFormBooleanNullable,
                        response.responseBody.requestFormStringList,
                        response.responseBody.requestFormStringListNullable
                    )
                )
            } else {
                serverHttpResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                serverHttpResponse.headers.set("api-result-code", "2")
                Mono.empty()
            }
        }.onErrorResume {
            serverHttpResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR)
            serverHttpResponse.headers.set("api-result-code", "1")
            Mono.empty()
        }
    }


    ////
    fun api9(serverHttpResponse: ServerHttpResponse): Mono<C3Service1TkV1RequestFromServerTestController.Api9OutputVo> {
        val serverFile =
            Paths.get("${File("").absolutePath}/src/main/resources/static/resource_c3_n7/test.txt")
                .toFile()

        val jsonString = Gson().toJson(
            LocalHostApis.PostService1TkV1RequestTestPostRequestMultipartFormDataJsonRequestFormVo.PostService1TkV1RequestTestPostRequestMultipartFormDataJsonJsonStringVo(
                "paramFromServer",
                null,
                1,
                null,
                1.1,
                null,
                true,
                null,
                listOf("paramFromServer"),
                null
            )
        )

        return LocalHostApis.postService1TkV1RequestTestPostRequestMultipartFormDataJson(
            LocalHostApis.PostService1TkV1RequestTestPostRequestMultipartFormDataJsonRequestFormVo(
                jsonString,
                FileSystemResource(serverFile),
                null
            )
        ).flatMap { response ->
            val httpStatus = response.clientResponse.statusCode()
            if (httpStatus.is2xxSuccessful) {
                serverHttpResponse.setStatusCode(HttpStatus.OK)
                serverHttpResponse.headers.set("api-result-code", "0")
                Mono.just(
                    C3Service1TkV1RequestFromServerTestController.Api9OutputVo(
                        response.responseBody!!.requestFormString,
                        response.responseBody.requestFormStringNullable,
                        response.responseBody.requestFormInt,
                        response.responseBody.requestFormIntNullable,
                        response.responseBody.requestFormDouble,
                        response.responseBody.requestFormDoubleNullable,
                        response.responseBody.requestFormBoolean,
                        response.responseBody.requestFormBooleanNullable,
                        response.responseBody.requestFormStringList,
                        response.responseBody.requestFormStringListNullable
                    )
                )
            } else {
                serverHttpResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                serverHttpResponse.headers.set("api-result-code", "2")
                Mono.empty()
            }
        }.onErrorResume {
            serverHttpResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR)
            serverHttpResponse.headers.set("api-result-code", "1")
            Mono.empty()
        }
    }

    ////
    fun api10(serverHttpResponse: ServerHttpResponse): Mono<Void> {
        return LocalHostApis.postService1TkV1RequestTestGenerateError().flatMap { response ->
            val httpStatus = response.clientResponse.statusCode()
            if (httpStatus.is2xxSuccessful) {
                serverHttpResponse.setStatusCode(HttpStatus.OK)
                serverHttpResponse.headers.set("api-result-code", "0")
                serverHttpResponse.setComplete()
            } else {
                serverHttpResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                serverHttpResponse.headers.set("api-result-code", "2")
                Mono.empty()
            }
        }.onErrorResume {
            serverHttpResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR)
            serverHttpResponse.headers.set("api-result-code", "1")
            Mono.empty()
        }
    }

    ////
    fun api11(serverHttpResponse: ServerHttpResponse): Mono<Void> {
        return LocalHostApis.postService1TkV1RequestTestApiResultCodeTest(
            LocalHostApis.PostService1TkV1RequestTestApiResultCodeTestRequestQueryParamVo(
                LocalHostApis.PostService1TkV1RequestTestApiResultCodeTestRequestQueryParamVo.PostService1TkV1RequestTestApiResultCodeTestErrorTypeEnum.B
            )
        ).flatMap { response ->
            val httpStatus = response.clientResponse.statusCode()
            if (httpStatus.is2xxSuccessful) {
                serverHttpResponse.setStatusCode(HttpStatus.OK)
                serverHttpResponse.headers.set("api-result-code", "0")
                serverHttpResponse.setComplete()
            } else {
                serverHttpResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR)

                when (response.clientResponse.headers().header("api-result-code")[0]) {
                    "1" -> {
                        serverHttpResponse.headers.set("api-result-code", "3")
                        Mono.empty()
                    }

                    "2" -> {
                        serverHttpResponse.headers.set("api-result-code", "4")
                        Mono.empty()
                    }

                    "3" -> {
                        serverHttpResponse.headers.set("api-result-code", "5")
                        Mono.empty()
                    }

                    else -> {
                        serverHttpResponse.headers.set("api-result-code", "2")
                        Mono.empty()
                    }
                }
            }
        }.onErrorResume {
            serverHttpResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR)
            serverHttpResponse.headers.set("api-result-code", "1")
            Mono.empty()
        }
    }

    ////
    fun api12(serverHttpResponse: ServerHttpResponse): Mono<Void> {
        return LocalHostApis.postService1TkV1RequestTestGenerateTimeOutError(
            LocalHostApis.PostService1TkV1RequestTestGenerateTimeOutErrorQueryParamVo(10)
        ).flatMap { response ->
            val httpStatus = response.clientResponse.statusCode()
            if (httpStatus.is2xxSuccessful) {
                serverHttpResponse.setStatusCode(HttpStatus.OK)
                serverHttpResponse.headers.set("api-result-code", "0")
                serverHttpResponse.setComplete()
            } else {
                serverHttpResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                serverHttpResponse.headers.set("api-result-code", "2")
                Mono.empty()
            }
        }.onErrorResume {
            serverHttpResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR)
            serverHttpResponse.headers.set("api-result-code", "1")
            Mono.empty()
        }
    }

    ////
    fun api13(serverHttpResponse: ServerHttpResponse): Mono<String> {

        return LocalHostApis.getService1TkV1RequestTestReturnTextString().flatMap { response ->
            val httpStatus = response.clientResponse.statusCode()
            if (httpStatus.is2xxSuccessful) {
                serverHttpResponse.setStatusCode(HttpStatus.OK)
                serverHttpResponse.headers.set("api-result-code", "0")
                Mono.just(response.responseBody!!)
            } else {
                serverHttpResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                serverHttpResponse.headers.set("api-result-code", "2")
                Mono.empty()
            }
        }.onErrorResume {
            serverHttpResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR)
            serverHttpResponse.headers.set("api-result-code", "1")
            Mono.empty()
        }
    }

    ////
    fun api14(serverHttpResponse: ServerHttpResponse): Mono<String> {

        return LocalHostApis.getService1TkV1RequestTestReturnTextHtml().flatMap { response ->
            val httpStatus = response.clientResponse.statusCode()
            if (httpStatus.is2xxSuccessful) {
                serverHttpResponse.setStatusCode(HttpStatus.OK)
                serverHttpResponse.headers.set("api-result-code", "0")
                Mono.just(response.responseBody!!)
            } else {
                serverHttpResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                serverHttpResponse.headers.set("api-result-code", "2")
                Mono.empty()
            }
        }.onErrorResume {
            serverHttpResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR)
            serverHttpResponse.headers.set("api-result-code", "1")
            Mono.empty()
        }
    }
}
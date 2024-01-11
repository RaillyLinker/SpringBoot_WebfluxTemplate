package com.raillylinker.springboot_webflux_template.controllers.c3_service1_tk_v1_requestFromServerTest

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@Tag(name = "/service1/tk/v1/request-from-server-test APIs", description = "C3 : 서버에서 요청을 보내는 테스트 API 컨트롤러")
@RestController
@RequestMapping("/service1/tk/v1/request-from-server-test")
class C3Service1TkV1RequestFromServerTestController(
    private val service: C3Service1TkV1RequestFromServerTestService
) {
    // <멤버 변수 공간>


    // ---------------------------------------------------------------------------------------------
    // <매핑 함수 공간>
    @Operation(
        summary = "N1 : 기본 요청 테스트",
        description = "기본적인 Get 메소드 요청 테스트입니다.\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작\n\n" +
                "1 : 네트워크 에러\n\n" +
                "2 : 서버 에러"
    )
    @GetMapping(
        path = ["/request-test"],
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.TEXT_PLAIN_VALUE]
    )
    fun api1(
        @Parameter(hidden = true)
        serverHttpResponse: ServerHttpResponse
    ): Mono<String> {
        return service.api1(serverHttpResponse)
    }

    ////
    @Operation(
        summary = "N2 : Redirect 테스트",
        description = "Redirect 되었을 때의 응답 테스트입니다.\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작\n\n" +
                "1 : 네트워크 에러\n\n" +
                "2 : 서버 에러"
    )
    @GetMapping(
        path = ["/redirect-to-blank"],
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.TEXT_PLAIN_VALUE]
    )
    fun api2(
        @Parameter(hidden = true)
        serverHttpResponse: ServerHttpResponse
    ): Mono<String> {
        return service.api2(serverHttpResponse)
    }

    ////
    @Operation(
        summary = "N3 : Get 요청 테스트 (Query Parameter)",
        description = "Query 파라미터를 받는 Get 요청 테스트\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작\n\n" +
                "1 : 네트워크 에러\n\n" +
                "2 : 서버 에러"
    )
    @GetMapping(
        path = ["/get-request"],
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun api3(
        @Parameter(hidden = true)
        serverHttpResponse: ServerHttpResponse
    ): Mono<Api3OutputVo> {
        return service.api3(serverHttpResponse)
    }

    data class Api3OutputVo(
        @Schema(description = "입력한 String Query 파라미터", required = true, example = "testString")
        @JsonProperty("queryParamString")
        val queryParamString: String,
        @Schema(description = "입력한 String Nullable Query 파라미터", required = false, example = "testString")
        @JsonProperty("queryParamStringNullable")
        val queryParamStringNullable: String?,
        @Schema(description = "입력한 Int Query 파라미터", required = true, example = "1")
        @JsonProperty("queryParamInt")
        val queryParamInt: Int,
        @Schema(description = "입력한 Int Nullable Query 파라미터", required = false, example = "1")
        @JsonProperty("queryParamIntNullable")
        val queryParamIntNullable: Int?,
        @Schema(description = "입력한 Double Query 파라미터", required = true, example = "1.1")
        @JsonProperty("queryParamDouble")
        val queryParamDouble: Double,
        @Schema(description = "입력한 Double Nullable Query 파라미터", required = false, example = "1.1")
        @JsonProperty("queryParamDoubleNullable")
        val queryParamDoubleNullable: Double?,
        @Schema(description = "입력한 Boolean Query 파라미터", required = true, example = "true")
        @JsonProperty("queryParamBoolean")
        val queryParamBoolean: Boolean,
        @Schema(description = "입력한 Boolean Nullable Query 파라미터", required = false, example = "true")
        @JsonProperty("queryParamBooleanNullable")
        val queryParamBooleanNullable: Boolean?,
        @Schema(
            description = "입력한 StringList Query 파라미터",
            required = true,
            example = "[\"testString1\", \"testString2\"]"
        )
        @JsonProperty("queryParamStringList")
        val queryParamStringList: List<String>,
        @Schema(
            description = "입력한 StringList Nullable Query 파라미터",
            required = false,
            example = "[\"testString1\", \"testString2\"]"
        )
        @JsonProperty("queryParamStringListNullable")
        val queryParamStringListNullable: List<String>?
    )

    ////
    @Operation(
        summary = "N4 : Get 요청 테스트 (Path Parameter)",
        description = "Path 파라미터를 받는 Get 요청 테스트\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작\n\n" +
                "1 : 네트워크 에러\n\n" +
                "2 : 서버 에러"
    )
    @GetMapping(
        path = ["/get-request-path-param"],
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun api4(
        @Parameter(hidden = true)
        serverHttpResponse: ServerHttpResponse
    ): Mono<Api4OutputVo> {
        return service.api4(serverHttpResponse)
    }

    data class Api4OutputVo(
        @Schema(description = "입력한 Int Path 파라미터", required = true, example = "1")
        @JsonProperty("pathParamInt")
        val pathParamInt: Int
    )

    ////
    @Operation(
        summary = "N5 : Post 요청 테스트 (Request Body, application/json)",
        description = "application/json 형식의 Request Body 를 받는 Post 요청 테스트\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작\n\n" +
                "1 : 네트워크 에러\n\n" +
                "2 : 서버 에러"
    )
    @GetMapping(
        path = ["/post-request-application-json"],
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun api5(
        @Parameter(hidden = true)
        serverHttpResponse: ServerHttpResponse
    ): Mono<Api5OutputVo> {
        return service.api5(serverHttpResponse)
    }

    data class Api5OutputVo(
        @Schema(description = "입력한 String Body 파라미터", required = true, example = "testString")
        @JsonProperty("requestBodyString")
        val requestBodyString: String,
        @Schema(description = "입력한 String Nullable Body 파라미터", required = false, example = "testString")
        @JsonProperty("requestBodyStringNullable")
        val requestBodyStringNullable: String?,
        @Schema(description = "입력한 Int Body 파라미터", required = true, example = "1")
        @JsonProperty("requestBodyInt")
        val requestBodyInt: Int,
        @Schema(description = "입력한 Int Nullable Body 파라미터", required = false, example = "1")
        @JsonProperty("requestBodyIntNullable")
        val requestBodyIntNullable: Int?,
        @Schema(description = "입력한 Double Body 파라미터", required = true, example = "1.1")
        @JsonProperty("requestBodyDouble")
        val requestBodyDouble: Double,
        @Schema(description = "입력한 Double Nullable Body 파라미터", required = false, example = "1.1")
        @JsonProperty("requestBodyDoubleNullable")
        val requestBodyDoubleNullable: Double?,
        @Schema(description = "입력한 Boolean Body 파라미터", required = true, example = "true")
        @JsonProperty("requestBodyBoolean")
        val requestBodyBoolean: Boolean,
        @Schema(description = "입력한 Boolean Nullable Body 파라미터", required = false, example = "true")
        @JsonProperty("requestBodyBooleanNullable")
        val requestBodyBooleanNullable: Boolean?,
        @Schema(
            description = "입력한 StringList Body 파라미터",
            required = true,
            example = "[\"testString1\", \"testString2\"]"
        )
        @JsonProperty("requestBodyStringList")
        val requestBodyStringList: List<String>,
        @Schema(
            description = "입력한 StringList Nullable Body 파라미터",
            required = false,
            example = "[\"testString1\", \"testString2\"]"
        )
        @JsonProperty("requestBodyStringListNullable")
        val requestBodyStringListNullable: List<String>?
    )

    ////
    @Operation(
        summary = "N6 : Post 요청 테스트 (Request Body, x-www-form-urlencoded)",
        description = "x-www-form-urlencoded 형식의 Request Body 를 받는 Post 요청 테스트\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작\n\n" +
                "1 : 네트워크 에러\n\n" +
                "2 : 서버 에러"
    )
    @GetMapping(
        path = ["/post-request-x-www-form-urlencoded"],
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun api6(
        @Parameter(hidden = true)
        serverHttpResponse: ServerHttpResponse
    ): Mono<Api6OutputVo> {
        return service.api6(serverHttpResponse)
    }

    data class Api6OutputVo(
        @Schema(description = "입력한 String Body 파라미터", required = true, example = "testString")
        @JsonProperty("requestFormString")
        val requestFormString: String,
        @Schema(description = "입력한 String Nullable Body 파라미터", required = false, example = "testString")
        @JsonProperty("requestFormStringNullable")
        val requestFormStringNullable: String?,
        @Schema(description = "입력한 Int Body 파라미터", required = true, example = "1")
        @JsonProperty("requestFormInt")
        val requestFormInt: Int,
        @Schema(description = "입력한 Int Nullable Body 파라미터", required = false, example = "1")
        @JsonProperty("requestFormIntNullable")
        val requestFormIntNullable: Int?,
        @Schema(description = "입력한 Double Body 파라미터", required = true, example = "1.1")
        @JsonProperty("requestFormDouble")
        val requestFormDouble: Double,
        @Schema(description = "입력한 Double Nullable Body 파라미터", required = false, example = "1.1")
        @JsonProperty("requestFormDoubleNullable")
        val requestFormDoubleNullable: Double?,
        @Schema(description = "입력한 Boolean Body 파라미터", required = true, example = "true")
        @JsonProperty("requestFormBoolean")
        val requestFormBoolean: Boolean,
        @Schema(description = "입력한 Boolean Nullable Body 파라미터", required = false, example = "true")
        @JsonProperty("requestFormBooleanNullable")
        val requestFormBooleanNullable: Boolean?,
        @Schema(
            description = "입력한 StringList Body 파라미터",
            required = true,
            example = "[\"testString1\", \"testString2\"]"
        )
        @JsonProperty("requestFormStringList")
        val requestFormStringList: List<String>,
        @Schema(
            description = "입력한 StringList Nullable Body 파라미터",
            required = false,
            example = "[\"testString1\", \"testString2\"]"
        )
        @JsonProperty("requestFormStringListNullable")
        val requestFormStringListNullable: List<String>?
    )

    ////
    @Operation(
        summary = "N7 : Post 요청 테스트 (Request Body, multipart/form-data)",
        description = "multipart/form-data 형식의 Request Body 를 받는 Post 요청 테스트\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작\n\n" +
                "1 : 네트워크 에러\n\n" +
                "2 : 서버 에러"
    )
    @GetMapping(
        path = ["/post-request-multipart-form-data"],
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun api7(
        @Parameter(hidden = true)
        serverHttpResponse: ServerHttpResponse
    ): Mono<Api7OutputVo> {
        return service.api7(serverHttpResponse)
    }

    data class Api7OutputVo(
        @Schema(description = "입력한 String Body 파라미터", required = true, example = "testString")
        @JsonProperty("requestFormString")
        val requestFormString: String,
        @Schema(description = "입력한 String Nullable Body 파라미터", required = false, example = "testString")
        @JsonProperty("requestFormStringNullable")
        val requestFormStringNullable: String?,
        @Schema(description = "입력한 Int Body 파라미터", required = true, example = "1")
        @JsonProperty("requestFormInt")
        val requestFormInt: Int,
        @Schema(description = "입력한 Int Nullable Body 파라미터", required = false, example = "1")
        @JsonProperty("requestFormIntNullable")
        val requestFormIntNullable: Int?,
        @Schema(description = "입력한 Double Body 파라미터", required = true, example = "1.1")
        @JsonProperty("requestFormDouble")
        val requestFormDouble: Double,
        @Schema(description = "입력한 Double Nullable Body 파라미터", required = false, example = "1.1")
        @JsonProperty("requestFormDoubleNullable")
        val requestFormDoubleNullable: Double?,
        @Schema(description = "입력한 Boolean Body 파라미터", required = true, example = "true")
        @JsonProperty("requestFormBoolean")
        val requestFormBoolean: Boolean,
        @Schema(description = "입력한 Boolean Nullable Body 파라미터", required = false, example = "true")
        @JsonProperty("requestFormBooleanNullable")
        val requestFormBooleanNullable: Boolean?,
        @Schema(
            description = "입력한 StringList Body 파라미터",
            required = true,
            example = "[\"testString1\", \"testString2\"]"
        )
        @JsonProperty("requestFormStringList")
        val requestFormStringList: List<String>,
        @Schema(
            description = "입력한 StringList Nullable Body 파라미터",
            required = false,
            example = "[\"testString1\", \"testString2\"]"
        )
        @JsonProperty("requestFormStringListNullable")
        val requestFormStringListNullable: List<String>?
    )

    ////
    @Operation(
        summary = "N8 : Post 요청 테스트 (Request Body, multipart/form-data, MultipartFile List)",
        description = "multipart/form-data 형식의 Request Body 를 받는 Post 요청 테스트\n\n" +
                "MultipartFile 파라미터를 List 로 받습니다.\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작\n\n" +
                "1 : 네트워크 에러\n\n" +
                "2 : 서버 에러"
    )
    @GetMapping(
        path = ["/post-request-multipart-form-data2"],
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun api8(
        @Parameter(hidden = true)
        serverHttpResponse: ServerHttpResponse
    ): Mono<Api8OutputVo> {
        return service.api8(serverHttpResponse)
    }

    data class Api8OutputVo(
        @Schema(description = "입력한 String Body 파라미터", required = true, example = "testString")
        @JsonProperty("requestFormString")
        val requestFormString: String,
        @Schema(description = "입력한 String Nullable Body 파라미터", required = false, example = "testString")
        @JsonProperty("requestFormStringNullable")
        val requestFormStringNullable: String?,
        @Schema(description = "입력한 Int Body 파라미터", required = true, example = "1")
        @JsonProperty("requestFormInt")
        val requestFormInt: Int,
        @Schema(description = "입력한 Int Nullable Body 파라미터", required = false, example = "1")
        @JsonProperty("requestFormIntNullable")
        val requestFormIntNullable: Int?,
        @Schema(description = "입력한 Double Body 파라미터", required = true, example = "1.1")
        @JsonProperty("requestFormDouble")
        val requestFormDouble: Double,
        @Schema(description = "입력한 Double Nullable Body 파라미터", required = false, example = "1.1")
        @JsonProperty("requestFormDoubleNullable")
        val requestFormDoubleNullable: Double?,
        @Schema(description = "입력한 Boolean Body 파라미터", required = true, example = "true")
        @JsonProperty("requestFormBoolean")
        val requestFormBoolean: Boolean,
        @Schema(description = "입력한 Boolean Nullable Body 파라미터", required = false, example = "true")
        @JsonProperty("requestFormBooleanNullable")
        val requestFormBooleanNullable: Boolean?,
        @Schema(
            description = "입력한 StringList Body 파라미터",
            required = true,
            example = "[\"testString1\", \"testString2\"]"
        )
        @JsonProperty("requestFormStringList")
        val requestFormStringList: List<String>,
        @Schema(
            description = "입력한 StringList Nullable Body 파라미터",
            required = false,
            example = "[\"testString1\", \"testString2\"]"
        )
        @JsonProperty("requestFormStringListNullable")
        val requestFormStringListNullable: List<String>?
    )

    ////
    @Operation(
        summary = "N9 : Post 요청 테스트 (Request Body, multipart/form-data, with jsonString)",
        description = "multipart/form-data 형식의 Request Body 를 받는 Post 요청 테스트\n\n" +
                "파일 외의 파라미터를 JsonString 형식으로 받습니다.\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작\n\n" +
                "1 : 네트워크 에러\n\n" +
                "2 : 서버 에러"
    )
    @GetMapping(
        path = ["/post-request-multipart-form-data-json"],
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun api9(
        @Parameter(hidden = true)
        serverHttpResponse: ServerHttpResponse
    ): Mono<Api9OutputVo> {
        return service.api9(serverHttpResponse)
    }

    data class Api9OutputVo(
        @Schema(description = "입력한 String Body 파라미터", required = true, example = "testString")
        @JsonProperty("requestFormString")
        val requestFormString: String,
        @Schema(description = "입력한 String Nullable Body 파라미터", required = false, example = "testString")
        @JsonProperty("requestFormStringNullable")
        val requestFormStringNullable: String?,
        @Schema(description = "입력한 Int Body 파라미터", required = true, example = "1")
        @JsonProperty("requestFormInt")
        val requestFormInt: Int,
        @Schema(description = "입력한 Int Nullable Body 파라미터", required = false, example = "1")
        @JsonProperty("requestFormIntNullable")
        val requestFormIntNullable: Int?,
        @Schema(description = "입력한 Double Body 파라미터", required = true, example = "1.1")
        @JsonProperty("requestFormDouble")
        val requestFormDouble: Double,
        @Schema(description = "입력한 Double Nullable Body 파라미터", required = false, example = "1.1")
        @JsonProperty("requestFormDoubleNullable")
        val requestFormDoubleNullable: Double?,
        @Schema(description = "입력한 Boolean Body 파라미터", required = true, example = "true")
        @JsonProperty("requestFormBoolean")
        val requestFormBoolean: Boolean,
        @Schema(description = "입력한 Boolean Nullable Body 파라미터", required = false, example = "true")
        @JsonProperty("requestFormBooleanNullable")
        val requestFormBooleanNullable: Boolean?,
        @Schema(
            description = "입력한 StringList Body 파라미터",
            required = true,
            example = "[\"testString1\", \"testString2\"]"
        )
        @JsonProperty("requestFormStringList")
        val requestFormStringList: List<String>,
        @Schema(
            description = "입력한 StringList Nullable Body 파라미터",
            required = false,
            example = "[\"testString1\", \"testString2\"]"
        )
        @JsonProperty("requestFormStringListNullable")
        val requestFormStringListNullable: List<String>?
    )

    ////
    @Operation(
        summary = "N10 : 에러 발생 테스트",
        description = "요청시 에러가 발생했을 때의 테스트입니다.\n\n" +
                "파일 외의 파라미터를 JsonString 형식으로 받습니다.\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작\n\n" +
                "1 : 네트워크 에러\n\n" +
                "2 : 서버 에러"
    )
    @GetMapping(
        path = ["/generate-error"],
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.ALL_VALUE]
    )
    fun api10(
        @Parameter(hidden = true)
        serverHttpResponse: ServerHttpResponse
    ): Mono<Void> {
        return service.api10(serverHttpResponse)
    }

    ////
    @Operation(
        summary = "N11 : api-result-code 반환 테스트",
        description = "api-result-code 가 Response Header 로 반환되는 테스트입니다.\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작\n\n" +
                "1 : 네트워크 에러\n\n" +
                "2 : 서버 에러\n\n" +
                "3 : errorType 을 A 로 보냈습니다.\n\n" +
                "4 : errorType 을 B 로 보냈습니다.\n\n" +
                "5 : errorType 을 C 로 보냈습니다."
    )
    @GetMapping(
        path = ["/api-result-code-test"],
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.ALL_VALUE]
    )
    fun api11(
        @Parameter(hidden = true)
        serverHttpResponse: ServerHttpResponse
    ): Mono<Void> {
        return service.api11(serverHttpResponse)
    }

    ////
    @Operation(
        summary = "N12 : 타임아웃 발생 테스트",
        description = "요청을 보내어 타임아웃이 발생했을 때를 테스트합니다.\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작\n\n" +
                "1 : 네트워크 에러\n\n" +
                "2 : 서버 에러"
    )
    @GetMapping(
        path = ["/time-delay-test"],
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.ALL_VALUE]
    )
    fun api12(
        @Parameter(hidden = true)
        serverHttpResponse: ServerHttpResponse
    ): Mono<Void> {
        return service.api12(serverHttpResponse)
    }

    ////
    @Operation(
        summary = "N13 : text/string 형식 Response 받아오기",
        description = "text/string 형식 Response 를 받아옵니다.\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작\n\n" +
                "1 : 네트워크 에러\n\n" +
                "2 : 서버 에러"
    )
    @GetMapping(
        path = ["/text-string-response"],
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.TEXT_PLAIN_VALUE]
    )
    fun api13(
        @Parameter(hidden = true)
        serverHttpResponse: ServerHttpResponse
    ): Mono<String> {
        return service.api13(serverHttpResponse)
    }

    ////
    @Operation(
        summary = "N14 : text/html 형식 Response 받아오기",
        description = "text/html 형식 Response 를 받아옵니다.\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작\n\n" +
                "1 : 네트워크 에러\n\n" +
                "2 : 서버 에러"
    )
    @GetMapping(
        path = ["/text-html-response"],
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.TEXT_PLAIN_VALUE]
    )
    fun api14(
        @Parameter(hidden = true)
        serverHttpResponse: ServerHttpResponse
    ): Mono<String> {
        return service.api14(serverHttpResponse)
    }
}
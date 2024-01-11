package com.raillylinker.springboot_webflux_template.data_sources.network_sources

import com.fasterxml.jackson.annotation.JsonProperty
import com.raillylinker.springboot_webflux_template.custom_classes.CustomDataClasses
import com.raillylinker.springboot_webflux_template.custom_objects.CustomUtilObject
import io.netty.channel.ChannelOption
import io.netty.handler.timeout.ReadTimeoutHandler
import io.netty.handler.timeout.WriteTimeoutHandler
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.core.io.FileSystemResource
import org.springframework.http.MediaType
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.BodyInserters
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

object LocalHostApis {
    private val webClient: WebClient = WebClient.builder()
        .baseUrl("http://localhost:8080")
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
    // [기본 요청 테스트 API]
    // 이 API 를 요청하면 String 을 반환합니다.
    // (api-result-code)
    // 0 : 정상 동작
    fun getService1TkV1RequestTest(): Mono<CustomDataClasses.WebClientResponse<String?>> {
        return webClient.get()
            .uri { uriBuilder ->
                CustomUtilObject.buildToUri(
                    uriBuilder,
                    "/service1/tk/v1/request-test",
                    null
                )
            }
            .exchangeToMono { response ->
                response.bodyToMono(String::class.java)
                    .map { responseBody ->
                        CustomDataClasses.WebClientResponse(
                            response,
                            responseBody
                        )
                    }
            }
    }

    ////
    // [요청 Redirect 테스트 API]
    // 이 API 를 요청하면 /service1/tk/v1/request-test 로 Redirect 됩니다.
    // (api-result-code)
    // 0 : 정상 동작
    fun getService1TkV1RequestTestRedirectToBlank(): Mono<CustomDataClasses.WebClientResponse<String?>> {
        return webClient.get()
            .uri { uriBuilder ->
                CustomUtilObject.buildToUri(
                    uriBuilder,
                    "/service1/tk/v1/request-test/redirect-to-blank",
                    null
                )
            }
            .exchangeToMono { response ->
                response.bodyToMono(String::class.java)
                    .map { responseBody ->
                        CustomDataClasses.WebClientResponse(
                            response,
                            responseBody
                        )
                    }
            }
    }

    ////
    // [Get 요청(Query Parameter) 테스트 API]
    // Query Parameter 를 받는 Get 메소드 요청 테스트
    // (api-result-code)
    // 0 : 정상 동작
    fun getService1TkV1RequestTestGetRequest(requestQueryParamVo: GetService1TkV1RequestTestGetRequestRequestQueryParamVo): Mono<CustomDataClasses.WebClientResponse<GetService1TkV1RequestTestGetRequestResponseBodyVo?>> {
        return webClient.get()
            .uri { uriBuilder ->
                CustomUtilObject.buildToUri(
                    uriBuilder,
                    "/service1/tk/v1/request-test/get-request",
                    requestQueryParamVo
                )
            }
            .exchangeToMono { response ->
                response.bodyToMono(GetService1TkV1RequestTestGetRequestResponseBodyVo::class.java)
                    .map { responseBody ->
                        CustomDataClasses.WebClientResponse(
                            response,
                            responseBody
                        )
                    }
            }
    }

    data class GetService1TkV1RequestTestGetRequestRequestQueryParamVo(
        val queryParamString: String,
        val queryParamStringNullable: String?,
        val queryParamInt: Int,
        val queryParamIntNullable: Int?,
        val queryParamDouble: Double,
        val queryParamDoubleNullable: Double?,
        val queryParamBoolean: Boolean,
        val queryParamBooleanNullable: Boolean?,
        val queryParamStringList: List<String>,
        val queryParamStringListNullable: List<String>?
    )

    data class GetService1TkV1RequestTestGetRequestResponseBodyVo(
        @JsonProperty("queryParamString")
        val queryParamString: String,
        @JsonProperty("queryParamStringNullable")
        val queryParamStringNullable: String?,
        @JsonProperty("queryParamInt")
        val queryParamInt: Int,
        @JsonProperty("queryParamIntNullable")
        val queryParamIntNullable: Int?,
        @JsonProperty("queryParamDouble")
        val queryParamDouble: Double,
        @JsonProperty("queryParamDoubleNullable")
        val queryParamDoubleNullable: Double?,
        @JsonProperty("queryParamBoolean")
        val queryParamBoolean: Boolean,
        @JsonProperty("queryParamBooleanNullable")
        val queryParamBooleanNullable: Boolean?,
        @JsonProperty("queryParamStringList")
        val queryParamStringList: List<String>,
        @JsonProperty("queryParamStringListNullable")
        val queryParamStringListNullable: List<String>?
    )

    ////
    // [Get 요청(Path Parameter) 테스트 API]
    // Path Parameter 를 받는 Get 메소드 요청 테스트
    // (api-result-code)
    // 0 : 정상 동작
    fun getService1TkV1RequestTestGetRequestPathParamInt(requestPathParamVo: GetService1TkV1RequestTestGetRequestPathParamIntRequestPathParamVo): Mono<CustomDataClasses.WebClientResponse<GetService1TkV1RequestTestGetRequestPathParamIntResponseBodyVo?>> {
        return webClient.get()
            .uri { uriBuilder ->
                CustomUtilObject.buildToUri(
                    uriBuilder,
                    "/service1/tk/v1/request-test/get-request/${requestPathParamVo.pathParamInt}",
                    null
                )
            }
            .exchangeToMono { response ->
                response.bodyToMono(GetService1TkV1RequestTestGetRequestPathParamIntResponseBodyVo::class.java)
                    .map { responseBody ->
                        CustomDataClasses.WebClientResponse(
                            response,
                            responseBody
                        )
                    }
            }
    }

    data class GetService1TkV1RequestTestGetRequestPathParamIntRequestPathParamVo(
        val pathParamInt: Int
    )

    data class GetService1TkV1RequestTestGetRequestPathParamIntResponseBodyVo(
        @JsonProperty("pathParamInt")
        val pathParamInt: Int
    )

    ////
    // [Post 요청(Application-Json) 테스트 API]
    // application-json 형태의 Request Body 를 받는 Post 메소드 요청 테스트
    // (api-result-code)
    // 0 : 정상 동작
    fun postService1TkV1RequestTestPostRequestApplicationJson(requestBodyVo: PostService1TkV1RequestTestPostRequestApplicationJsonRequestBodyVo): Mono<CustomDataClasses.WebClientResponse<PostService1TkV1RequestTestPostRequestApplicationJsonResponseBodyVo?>> {
        return webClient.post()
            .uri { uriBuilder ->
                CustomUtilObject.buildToUri(
                    uriBuilder,
                    "/service1/tk/v1/request-test/post-request-application-json",
                    null
                )
            }
            .bodyValue(requestBodyVo)
            .exchangeToMono { response ->
                response.bodyToMono(PostService1TkV1RequestTestPostRequestApplicationJsonResponseBodyVo::class.java)
                    .map { responseBody ->
                        CustomDataClasses.WebClientResponse(
                            response,
                            responseBody
                        )
                    }
            }
    }

    data class PostService1TkV1RequestTestPostRequestApplicationJsonRequestBodyVo(
        @JsonProperty("requestBodyString")
        val requestBodyString: String,
        @JsonProperty("requestBodyStringNullable")
        val requestBodyStringNullable: String?,
        @JsonProperty("requestBodyInt")
        val requestBodyInt: Int,
        @JsonProperty("requestBodyIntNullable")
        val requestBodyIntNullable: Int?,
        @JsonProperty("requestBodyDouble")
        val requestBodyDouble: Double,
        @JsonProperty("requestBodyDoubleNullable")
        val requestBodyDoubleNullable: Double?,
        @JsonProperty("requestBodyBoolean")
        val requestBodyBoolean: Boolean,
        @JsonProperty("requestBodyBooleanNullable")
        val requestBodyBooleanNullable: Boolean?,
        @JsonProperty("requestBodyStringList")
        val requestBodyStringList: List<String>,
        @JsonProperty("requestBodyStringListNullable")
        val requestBodyStringListNullable: List<String>?
    )

    data class PostService1TkV1RequestTestPostRequestApplicationJsonResponseBodyVo(
        @JsonProperty("requestBodyString")
        val requestBodyString: String,
        @JsonProperty("requestBodyStringNullable")
        val requestBodyStringNullable: String?,
        @JsonProperty("requestBodyInt")
        val requestBodyInt: Int,
        @JsonProperty("requestBodyIntNullable")
        val requestBodyIntNullable: Int?,
        @JsonProperty("requestBodyDouble")
        val requestBodyDouble: Double,
        @JsonProperty("requestBodyDoubleNullable")
        val requestBodyDoubleNullable: Double?,
        @JsonProperty("requestBodyBoolean")
        val requestBodyBoolean: Boolean,
        @JsonProperty("requestBodyBooleanNullable")
        val requestBodyBooleanNullable: Boolean?,
        @JsonProperty("requestBodyStringList")
        val requestBodyStringList: List<String>,
        @JsonProperty("requestBodyStringListNullable")
        val requestBodyStringListNullable: List<String>?
    )

    ////
    // [Post 요청(x-www-form-urlencoded) 테스트 API]
    // x-www-form-urlencoded 형태의 Request Body 를 받는 Post 메소드 요청 테스트
    // (api-result-code)
    // 0 : 정상 동작
    fun postService1TkV1RequestTestPostRequestXWwwFormUrlencoded(requestFormVo: PostService1TkV1RequestTestPostRequestXWwwFormUrlencodedRequestFormVo): Mono<CustomDataClasses.WebClientResponse<PostService1TkV1RequestTestPostRequestXWwwFormUrlencodedResponseBodyVo?>> {
        return webClient.post()
            .uri { uriBuilder ->
                CustomUtilObject.buildToUri(
                    uriBuilder,
                    "/service1/tk/v1/request-test/post-request-x-www-form-urlencoded",
                    null
                )
            }
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(BodyInserters.fromFormData(CustomUtilObject.objectToMultiValueStringMap(requestFormVo)))
            .exchangeToMono { response ->
                response.bodyToMono(PostService1TkV1RequestTestPostRequestXWwwFormUrlencodedResponseBodyVo::class.java)
                    .map { responseBody ->
                        CustomDataClasses.WebClientResponse(
                            response,
                            responseBody
                        )
                    }
            }
    }

    data class PostService1TkV1RequestTestPostRequestXWwwFormUrlencodedRequestFormVo(
        @JsonProperty("requestFormString")
        val requestFormString: String,
        @JsonProperty("requestFormStringNullable")
        val requestFormStringNullable: String?,
        @JsonProperty("requestFormInt")
        val requestFormInt: Int,
        @JsonProperty("requestFormIntNullable")
        val requestFormIntNullable: Int?,
        @JsonProperty("requestFormDouble")
        val requestFormDouble: Double,
        @JsonProperty("requestFormDoubleNullable")
        val requestFormDoubleNullable: Double?,
        @JsonProperty("requestFormBoolean")
        val requestFormBoolean: Boolean,
        @JsonProperty("requestFormBooleanNullable")
        val requestFormBooleanNullable: Boolean?,
        @JsonProperty("requestFormStringList")
        val requestFormStringList: List<String>,
        @JsonProperty("requestFormStringListNullable")
        val requestFormStringListNullable: List<String>?
    )

    data class PostService1TkV1RequestTestPostRequestXWwwFormUrlencodedResponseBodyVo(
        @JsonProperty("requestFormString")
        val requestFormString: String,
        @JsonProperty("requestFormStringNullable")
        val requestFormStringNullable: String?,
        @JsonProperty("requestFormInt")
        val requestFormInt: Int,
        @JsonProperty("requestFormIntNullable")
        val requestFormIntNullable: Int?,
        @JsonProperty("requestFormDouble")
        val requestFormDouble: Double,
        @JsonProperty("requestFormDoubleNullable")
        val requestFormDoubleNullable: Double?,
        @JsonProperty("requestFormBoolean")
        val requestFormBoolean: Boolean,
        @JsonProperty("requestFormBooleanNullable")
        val requestFormBooleanNullable: Boolean?,
        @JsonProperty("requestFormStringList")
        val requestFormStringList: List<String>,
        @JsonProperty("requestFormStringListNullable")
        val requestFormStringListNullable: List<String>?
    )


    ////
    // [Post 요청(multipart/form-data) 테스트 API]
    // multipart/form-data 형태의 Request Body 를 받는 Post 메소드 요청 테스트(Multipart File List)
    // MultipartFile 파라미터가 null 이 아니라면 저장
    // (api-result-code)
    // 0 : 정상 동작
    fun postService1TkV1RequestTestPostRequestMultipartFormData(requestFormVo: PostService1TkV1RequestTestPostRequestMultipartFormDataRequestFormVo): Mono<CustomDataClasses.WebClientResponse<PostService1TkV1RequestTestPostRequestMultipartFormDataResponseBodyVo?>> {
        return webClient.post()
            .uri { uriBuilder ->
                CustomUtilObject.buildToUri(
                    uriBuilder,
                    "/service1/tk/v1/request-test/post-request-multipart-form-data",
                    null
                )
            }
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .body(BodyInserters.fromMultipartData(CustomUtilObject.objectToMultiValueAnyMap(requestFormVo)))
            .exchangeToMono { response ->
                response.bodyToMono(PostService1TkV1RequestTestPostRequestMultipartFormDataResponseBodyVo::class.java)
                    .map { responseBody ->
                        CustomDataClasses.WebClientResponse(
                            response,
                            responseBody
                        )
                    }
            }
    }

    data class PostService1TkV1RequestTestPostRequestMultipartFormDataRequestFormVo(
        @JsonProperty("requestFormString")
        val requestFormString: String,
        @JsonProperty("requestFormStringNullable")
        val requestFormStringNullable: String?,
        @JsonProperty("requestFormInt")
        val requestFormInt: Int,
        @JsonProperty("requestFormIntNullable")
        val requestFormIntNullable: Int?,
        @JsonProperty("requestFormDouble")
        val requestFormDouble: Double,
        @JsonProperty("requestFormDoubleNullable")
        val requestFormDoubleNullable: Double?,
        @JsonProperty("requestFormBoolean")
        val requestFormBoolean: Boolean,
        @JsonProperty("requestFormBooleanNullable")
        val requestFormBooleanNullable: Boolean?,
        @JsonProperty("requestFormStringList")
        val requestFormStringList: List<String>,
        @JsonProperty("requestFormStringListNullable")
        val requestFormStringListNullable: List<String>?,
        @JsonProperty("multipartFile")
        val multipartFile: FileSystemResource,
        @JsonProperty("multipartFileNullable")
        val multipartFileNullable: FileSystemResource?
    )

    data class PostService1TkV1RequestTestPostRequestMultipartFormDataResponseBodyVo(
        @JsonProperty("requestFormString")
        val requestFormString: String,
        @JsonProperty("requestFormStringNullable")
        val requestFormStringNullable: String?,
        @JsonProperty("requestFormInt")
        val requestFormInt: Int,
        @JsonProperty("requestFormIntNullable")
        val requestFormIntNullable: Int?,
        @JsonProperty("requestFormDouble")
        val requestFormDouble: Double,
        @JsonProperty("requestFormDoubleNullable")
        val requestFormDoubleNullable: Double?,
        @JsonProperty("requestFormBoolean")
        val requestFormBoolean: Boolean,
        @JsonProperty("requestFormBooleanNullable")
        val requestFormBooleanNullable: Boolean?,
        @JsonProperty("requestFormStringList")
        val requestFormStringList: List<String>,
        @JsonProperty("requestFormStringListNullable")
        val requestFormStringListNullable: List<String>?
    )


    ////
    // [Post 요청(multipart/form-data list) 테스트 API]
    // multipart/form-data 형태의 Request Body 를 받는 Post 메소드 요청 테스트(Multipart File List)
    // 파일 리스트가 null 이 아니라면 저장
    // (api-result-code)
    // 0 : 정상 동작
    fun postService1TkV1RequestTestPostRequestMultipartFormData2(requestFormVo: PostService1TkV1RequestTestPostRequestMultipartFormData2RequestFormVo): Mono<CustomDataClasses.WebClientResponse<PostService1TkV1RequestTestPostRequestMultipartFormData2ResponseBodyVo?>> {
        return webClient.post()
            .uri { uriBuilder ->
                CustomUtilObject.buildToUri(
                    uriBuilder,
                    "/service1/tk/v1/request-test/post-request-multipart-form-data2",
                    null
                )
            }
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .body(BodyInserters.fromMultipartData(CustomUtilObject.objectToMultiValueAnyMap(requestFormVo)))
            .exchangeToMono { response ->
                response.bodyToMono(PostService1TkV1RequestTestPostRequestMultipartFormData2ResponseBodyVo::class.java)
                    .map { responseBody ->
                        CustomDataClasses.WebClientResponse(
                            response,
                            responseBody
                        )
                    }
            }
    }

    data class PostService1TkV1RequestTestPostRequestMultipartFormData2RequestFormVo(
        @JsonProperty("requestFormString")
        val requestFormString: String,
        @JsonProperty("requestFormStringNullable")
        val requestFormStringNullable: String?,
        @JsonProperty("requestFormInt")
        val requestFormInt: Int,
        @JsonProperty("requestFormIntNullable")
        val requestFormIntNullable: Int?,
        @JsonProperty("requestFormDouble")
        val requestFormDouble: Double,
        @JsonProperty("requestFormDoubleNullable")
        val requestFormDoubleNullable: Double?,
        @JsonProperty("requestFormBoolean")
        val requestFormBoolean: Boolean,
        @JsonProperty("requestFormBooleanNullable")
        val requestFormBooleanNullable: Boolean?,
        @JsonProperty("requestFormStringList")
        val requestFormStringList: List<String>,
        @JsonProperty("requestFormStringListNullable")
        val requestFormStringListNullable: List<String>?,
        @JsonProperty("multipartFileList")
        val multipartFileList: List<FileSystemResource>,
        @JsonProperty("multipartFileNullableList")
        val multipartFileNullableList: List<FileSystemResource>?
    )

    data class PostService1TkV1RequestTestPostRequestMultipartFormData2ResponseBodyVo(
        @JsonProperty("requestFormString")
        val requestFormString: String,
        @JsonProperty("requestFormStringNullable")
        val requestFormStringNullable: String?,
        @JsonProperty("requestFormInt")
        val requestFormInt: Int,
        @JsonProperty("requestFormIntNullable")
        val requestFormIntNullable: Int?,
        @JsonProperty("requestFormDouble")
        val requestFormDouble: Double,
        @JsonProperty("requestFormDoubleNullable")
        val requestFormDoubleNullable: Double?,
        @JsonProperty("requestFormBoolean")
        val requestFormBoolean: Boolean,
        @JsonProperty("requestFormBooleanNullable")
        val requestFormBooleanNullable: Boolean?,
        @JsonProperty("requestFormStringList")
        val requestFormStringList: List<String>,
        @JsonProperty("requestFormStringListNullable")
        val requestFormStringListNullable: List<String>?
    )


    ////
    // [Post 요청(multipart/form-data list) 테스트 API]
    // multipart/form-data 형태의 Request Body 를 받는 Post 메소드 요청 테스트(Multipart File List)
    // 파일 리스트가 null 이 아니라면 저장
    // (api-result-code)
    // 0 : 정상 동작
    fun postService1TkV1RequestTestPostRequestMultipartFormDataJson(requestFormVo: PostService1TkV1RequestTestPostRequestMultipartFormDataJsonRequestFormVo): Mono<CustomDataClasses.WebClientResponse<PostService1TkV1RequestTestPostRequestMultipartFormDataJsonResponseBodyVo?>> {
        return webClient.post()
            .uri { uriBuilder ->
                CustomUtilObject.buildToUri(
                    uriBuilder,
                    "/service1/tk/v1/request-test/post-request-multipart-form-data-json",
                    null
                )
            }
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .body(BodyInserters.fromMultipartData(CustomUtilObject.objectToMultiValueAnyMap(requestFormVo)))
            .exchangeToMono { response ->
                response.bodyToMono(PostService1TkV1RequestTestPostRequestMultipartFormDataJsonResponseBodyVo::class.java)
                    .map { responseBody ->
                        CustomDataClasses.WebClientResponse(
                            response,
                            responseBody
                        )
                    }
            }
    }

    data class PostService1TkV1RequestTestPostRequestMultipartFormDataJsonRequestFormVo(
        @JsonProperty("jsonString")
        val jsonString: String,
        @JsonProperty("multipartFile")
        val multipartFile: FileSystemResource,
        @JsonProperty("multipartFileNullable")
        val multipartFileNullable: FileSystemResource?
    ) {
        data class PostService1TkV1RequestTestPostRequestMultipartFormDataJsonJsonStringVo(
            @JsonProperty("requestFormString")
            val requestFormString: String,
            @JsonProperty("requestFormStringNullable")
            val requestFormStringNullable: String?,
            @JsonProperty("requestFormInt")
            val requestFormInt: Int,
            @JsonProperty("requestFormIntNullable")
            val requestFormIntNullable: Int?,
            @JsonProperty("requestFormDouble")
            val requestFormDouble: Double,
            @JsonProperty("requestFormDoubleNullable")
            val requestFormDoubleNullable: Double?,
            @JsonProperty("requestFormBoolean")
            val requestFormBoolean: Boolean,
            @JsonProperty("requestFormBooleanNullable")
            val requestFormBooleanNullable: Boolean?,
            @JsonProperty("requestFormStringList")
            val requestFormStringList: List<String>,
            @JsonProperty("requestFormStringListNullable")
            val requestFormStringListNullable: List<String>?
        )
    }

    data class PostService1TkV1RequestTestPostRequestMultipartFormDataJsonResponseBodyVo(
        @JsonProperty("requestFormString")
        val requestFormString: String,
        @JsonProperty("requestFormStringNullable")
        val requestFormStringNullable: String?,
        @JsonProperty("requestFormInt")
        val requestFormInt: Int,
        @JsonProperty("requestFormIntNullable")
        val requestFormIntNullable: Int?,
        @JsonProperty("requestFormDouble")
        val requestFormDouble: Double,
        @JsonProperty("requestFormDoubleNullable")
        val requestFormDoubleNullable: Double?,
        @JsonProperty("requestFormBoolean")
        val requestFormBoolean: Boolean,
        @JsonProperty("requestFormBooleanNullable")
        val requestFormBooleanNullable: Boolean?,
        @JsonProperty("requestFormStringList")
        val requestFormStringList: List<String>,
        @JsonProperty("requestFormStringListNullable")
        val requestFormStringListNullable: List<String>?
    )

    ////
    // [인위적 에러 발생 테스트 API]
    // 요청 받으면 인위적인 서버 에러를 발생시킵니다.(Http Response Status 500)
    // (api-result-code)
    // 0 : 정상 동작
    fun postService1TkV1RequestTestGenerateError(): Mono<CustomDataClasses.WebClientResponse<Unit?>> {
        return webClient.post()
            .uri { uriBuilder ->
                CustomUtilObject.buildToUri(
                    uriBuilder,
                    "/service1/tk/v1/request-test/generate-error",
                    null
                )
            }
            .exchangeToMono { response ->
                Mono.just(
                    CustomDataClasses.WebClientResponse(
                        response,
                        null
                    )
                )
            }
    }

    ////
    // [결과 코드 발생 테스트 API]
    // Response Header 에 api-result-code 를 반환하는 테스트 API
    //(api-result-code)
    //0 : 정상 동작
    //1 : errorType 을 A 로 보냈습니다.
    //2 : errorType 을 B 로 보냈습니다.
    //3 : errorType 을 C 로 보냈습니다.
    fun postService1TkV1RequestTestApiResultCodeTest(requestQueryParamVo: PostService1TkV1RequestTestApiResultCodeTestRequestQueryParamVo): Mono<CustomDataClasses.WebClientResponse<Unit?>> {
        return webClient.post()
            .uri { uriBuilder ->
                CustomUtilObject.buildToUri(
                    uriBuilder,
                    "/service1/tk/v1/request-test/api-result-code-test",
                    requestQueryParamVo
                )
            }
            .exchangeToMono { response ->
                Mono.just(
                    CustomDataClasses.WebClientResponse(
                        response,
                        null
                    )
                )
            }
    }

    data class PostService1TkV1RequestTestApiResultCodeTestRequestQueryParamVo(
        val errorType: PostService1TkV1RequestTestApiResultCodeTestErrorTypeEnum
    ) {
        enum class PostService1TkV1RequestTestApiResultCodeTestErrorTypeEnum {
            A,
            B,
            C
        }
    }

    ////
    // [인위적 타임아웃 에러 발생 테스트]
    // 타임아웃 에러를 발생시키기 위해 임의로 응답 시간을 지연시킵니다.
    //(api-result-code)
    //0 : 정상 동작
    fun postService1TkV1RequestTestGenerateTimeOutError(requestQueryParamVo: PostService1TkV1RequestTestGenerateTimeOutErrorQueryParamVo): Mono<CustomDataClasses.WebClientResponse<Unit?>> {
        return webClient.post()
            .uri { uriBuilder ->
                CustomUtilObject.buildToUri(
                    uriBuilder,
                    "/service1/tk/v1/request-test/time-delay-test",
                    requestQueryParamVo
                )
            }
            .exchangeToMono { response ->
                Mono.just(
                    CustomDataClasses.WebClientResponse(
                        response,
                        null
                    )
                )
            }
    }

    data class PostService1TkV1RequestTestGenerateTimeOutErrorQueryParamVo(
        val delayTimeSec: Int
    )

    ////
    // [text/string 반환 샘플]
    // text/string 형식의 Response Body 를 반환합니다.
    // (api-result-code)
    // 0 : 정상 동작
    fun getService1TkV1RequestTestReturnTextString(): Mono<CustomDataClasses.WebClientResponse<String?>> {
        return webClient.get()
            .uri { uriBuilder ->
                CustomUtilObject.buildToUri(
                    uriBuilder,
                    "/service1/tk/v1/request-test/return-text-string",
                    null
                )
            }
            .exchangeToMono { response ->
                response.bodyToMono(String::class.java)
                    .map { responseBody ->
                        CustomDataClasses.WebClientResponse(
                            response,
                            responseBody
                        )
                    }
            }
    }

    ////
    // [text/html 반환 샘플]
    // text/html 형식의 Response Body 를 반환합니다.
    // (api-result-code)
    // 0 : 정상 동작
    fun getService1TkV1RequestTestReturnTextHtml(): Mono<CustomDataClasses.WebClientResponse<String?>> {
        return webClient.get()
            .uri { uriBuilder ->
                CustomUtilObject.buildToUri(
                    uriBuilder,
                    "/service1/tk/v1/request-test/return-text-html",
                    null
                )
            }
            .exchangeToMono { response ->
                response.bodyToMono(String::class.java)
                    .map { responseBody ->
                        CustomDataClasses.WebClientResponse(
                            response,
                            responseBody
                        )
                    }
            }
    }
}
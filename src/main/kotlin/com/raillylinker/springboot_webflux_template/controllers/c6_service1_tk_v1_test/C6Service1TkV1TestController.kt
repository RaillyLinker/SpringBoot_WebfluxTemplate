package com.raillylinker.springboot_webflux_template.controllers.c6_service1_tk_v1_test

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType
import org.springframework.http.codec.multipart.FilePart
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@Tag(name = "/service1/tk/v1/test APIs", description = "C6 : 테스트 API 컨트롤러")
@Controller
@RequestMapping("/service1/tk/v1/test")
class C6Service1TkV1TestController(
    private val service: C6Service1TkV1TestService
) {
    // <멤버 변수 공간>


    // ---------------------------------------------------------------------------------------------
    // <매핑 함수 공간>
    @Operation(
        summary = "N1 : 이메일 발송 테스트",
        description = "이메일 발송 테스트\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작"
    )
    @PostMapping(
        path = ["/send-email"],
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE],
        produces = [MediaType.ALL_VALUE]
    )
    @ResponseBody
    fun api1(
        @Parameter(hidden = true)
        serverHttpResponse: ServerHttpResponse,
        @ModelAttribute
        inputVoMono: Mono<Api1InputVo>
    ): Mono<Void> {
        return service.api1(
            serverHttpResponse, inputVoMono
        )
    }

    data class Api1InputVo(
        @Schema(description = "수신자 이메일 배열", required = true, example = "[\"test1@gmail.com\"]")
        @JsonProperty("receiverEmailAddressList")
        val receiverEmailAddressList: List<String>,
        @Schema(description = "참조자 이메일 배열", required = false, example = "[\"test2@gmail.com\"]")
        @JsonProperty("carbonCopyEmailAddressList")
        val carbonCopyEmailAddressList: List<String>?,
        @Schema(description = "발신자명", required = true, example = "Railly Linker")
        @JsonProperty("senderName")
        val senderName: String,
        @Schema(description = "제목", required = true, example = "테스트 이메일")
        @JsonProperty("subject")
        val subject: String,
        @Schema(description = "메세지", required = true, example = "테스트 이메일을 송신했습니다.")
        @JsonProperty("message")
        val message: String,
        @Schema(description = "첨부 파일 리스트", required = false)
        @JsonProperty("multipartFileList")
        val multipartFileList: List<FilePart>?
    )

    ////
    @Operation(
        summary = "N2 : HTML 이메일 발송 테스트",
        description = "HTML 로 이루어진 이메일 발송 테스트\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작"
    )
    @PostMapping(
        path = ["/send-html-email"],
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE],
        produces = [MediaType.ALL_VALUE]
    )
    @ResponseBody
    fun api2(
        @Parameter(hidden = true)
        serverHttpResponse: ServerHttpResponse,
        @ModelAttribute
        inputVoMono: Mono<Api2InputVo>
    ): Mono<Void> {
        return service.api2(
            serverHttpResponse, inputVoMono
        )
    }

    data class Api2InputVo(
        @Schema(description = "수신자 이메일 배열", required = true, example = "[\"test1@gmail.com\"]")
        @JsonProperty("receiverEmailAddressList")
        val receiverEmailAddressList: List<String>,
        @Schema(description = "참조자 이메일 배열", required = false, example = "[\"test2@gmail.com\"]")
        @JsonProperty("carbonCopyEmailAddressList")
        val carbonCopyEmailAddressList: List<String>?,
        @Schema(description = "발신자명", required = true, example = "Railly Linker")
        @JsonProperty("senderName")
        val senderName: String,
        @Schema(description = "제목", required = true, example = "테스트 이메일")
        @JsonProperty("subject")
        val subject: String,
        @Schema(description = "메세지", required = true, example = "테스트 이메일을 송신했습니다.")
        @JsonProperty("message")
        val message: String,
        @Schema(description = "첨부 파일 리스트", required = false)
        @JsonProperty("multipartFileList")
        val multipartFileList: List<FilePart>?
    )


    ////
    @Operation(
        summary = "N3 : Naver API SMS 발송 샘플",
        description = "Naver API 를 사용한 SMS 발송 샘플\n\n" +
                "Service 에서 사용하는 Naver SMS 발송 유틸 내의 개인정보를 변경해야 사용 가능\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작"
    )
    @PostMapping(
        path = ["/naver-sms-sample"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.ALL_VALUE]
    )
    @ResponseBody
    fun api3(
        @Parameter(hidden = true)
        serverHttpResponse: ServerHttpResponse,
        @RequestBody
        inputVo: Api3InputVo
    ): Mono<Void> {
        return service.api3(
            serverHttpResponse,
            inputVo
        )
    }

    data class Api3InputVo(
        @Schema(description = "SMS 수신측 휴대전화 번호", required = true, example = "82)010-1111-1111")
        @JsonProperty("phoneNumber")
        val phoneNumber: String,
        @Schema(description = "SMS 메세지", required = true, example = "테스트 메세지 발송입니다.")
        @JsonProperty("smsMessage")
        val smsMessage: String
    )


    ////
    @Operation(
        summary = "N4 : 액셀 파일을 받아서 해석 후 데이터 반환",
        description = "액셀 파일을 받아서 해석 후 데이터 반환\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작"
    )
    @PostMapping(
        path = ["/read-excel"],
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @ResponseBody
    fun api4(
        @Parameter(hidden = true)
        serverHttpResponse: ServerHttpResponse,
        @ModelAttribute
        inputVoMono: Mono<Api4InputVo>
    ): Mono<Api4OutputVo> {
        return service.api4(
            serverHttpResponse, inputVoMono
        )
    }

    data class Api4InputVo(
        @Schema(description = "가져오려는 시트 인덱스 (0부터 시작)", required = true, example = "0")
        @JsonProperty("sheetIdx")
        val sheetIdx: Int,
        @Schema(description = "가져올 행 범위 시작 인덱스 (0부터 시작)", required = true, example = "0")
        @JsonProperty("rowRangeStartIdx")
        val rowRangeStartIdx: Int,
        @Schema(description = "가져올 행 범위 끝 인덱스 null 이라면 전부 (0부터 시작)", required = false, example = "10")
        @JsonProperty("rowRangeEndIdx")
        val rowRangeEndIdx: Int?,
        @Schema(description = "가져올 열 범위 인덱스 리스트 null 이라면 전부 (0부터 시작)", required = false, example = "[0, 1, 2]")
        @JsonProperty("columnRangeIdxList")
        val columnRangeIdxList: List<Int>?,
        @Schema(description = "결과 컬럼의 최소 길이 (길이를 넘으면 그대로, 미만이라면 \"\" 로 채움)", required = false, example = "5")
        @JsonProperty("minColumnLength")
        val minColumnLength: Int?,
        @Schema(description = "액셀 파일", required = true)
        @JsonProperty("excelFile")
        val excelFile: FilePart
    )

    data class Api4OutputVo(
        @Schema(description = "행 카운트", required = true, example = "1")
        @JsonProperty("rowCount")
        val rowCount: Int,
        @Schema(description = "분석한 객체를 toString 으로 표현한 데이터 String", required = true, example = "[[\"데이터1\", \"데이터2\"]]")
        @JsonProperty("dataString")
        val dataString: String
    )


    ////
    @Operation(
        summary = "N5 : 액셀 파일 쓰기",
        description = "데이터를 기반으로 액셀 파일을 만들어 files/temp 폴더에 저장\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작"
    )
    @GetMapping(
        path = ["/write-excel"],
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.ALL_VALUE]
    )
    @ResponseBody
    fun api5(
        @Parameter(hidden = true)
        serverHttpResponse: ServerHttpResponse,
    ): Mono<Void> {
        return service.api5(
            serverHttpResponse
        )
    }


    ////
    @Operation(
        summary = "N6 : HTML 을 기반으로 PDF 를 생성",
        description = "준비된 HTML 1.0(strict), CSS 2.1 을 기반으로 PDF 를 생성 후 files/temp 폴더에 저장\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작"
    )
    @GetMapping(
        path = ["/html-to-pdf"],
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.ALL_VALUE]
    )
    @ResponseBody
    fun api6(
        @Parameter(hidden = true)
        serverHttpResponse: ServerHttpResponse,
    ): Mono<Void> {
        return service.api6(
            serverHttpResponse
        )
    }
}
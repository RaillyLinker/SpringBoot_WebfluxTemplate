package com.raillylinker.springboot_webflux_template.controllers.c7_service1_tk_v1_databaseTest

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@Tag(name = "/service1/tk/v1/database-test APIs", description = "C7 : Database 에 대한 테스트 API 컨트롤러")
@RestController
@RequestMapping("/service1/tk/v1/database-test")
class C7Service1TkV1DatabaseTestController(
    private val service: C7Service1TkV1DatabaseTestService
) {
    // <멤버 변수 공간>


    // ---------------------------------------------------------------------------------------------
    // <매핑 함수 공간>
    @Operation(
        summary = "N1 : DB Row 입력 테스트 API",
        description = "테스트 테이블에 Row 를 입력합니다.\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작"
    )
    @PostMapping(
        path = ["/row"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @ResponseBody
    fun api1(
        @Parameter(hidden = true)
        serverHttpResponse: ServerHttpResponse,
        @RequestBody
        inputVo: Api1InputVo
    ): Mono<Api1OutputVo> {
        return service.api1(
            serverHttpResponse,
            inputVo
        )
    }

    data class Api1InputVo(
        @Schema(description = "글 본문", required = true, example = "테스트 텍스트입니다.")
        @JsonProperty("content")
        val content: String
    )

    data class Api1OutputVo(
        @Schema(description = "글 고유번호", required = true, example = "1234")
        @JsonProperty("uid")
        val uid: Long,
        @Schema(description = "글 본문", required = true, example = "테스트 텍스트입니다.")
        @JsonProperty("content")
        val content: String,
        @Schema(description = "자동 생성 숫자", required = true, example = "21345")
        @JsonProperty("randomNum")
        val randomNum: Int,
        @Schema(description = "글 작성일", required = true, example = "2022-10-11T02:21:36.779")
        @JsonProperty("createDate")
        val createDate: String,
        @Schema(description = "글 수정일", required = true, example = "2022-10-11T02:21:36.779")
        @JsonProperty("updateDate")
        val updateDate: String
    )


//    ////
//    @Operation(
//        summary = "N2 : DB Rows 삭제 테스트 API",
//        description = "테스트 테이블의 모든 Row 를 모두 삭제합니다.\n\n" +
//                "(api-result-code)\n\n" +
//                "0 : 정상 동작"
//    )
//    @DeleteMapping("/rows")
//    fun api2(
//        @Parameter(hidden = true)
//        httpServletResponse: HttpServletResponse
//    ) {
//        service.api2(httpServletResponse)
//    }
//
//
//    ////
//    @Operation(
//        summary = "N3 : DB Row 삭제 테스트",
//        description = "테스트 테이블의 Row 하나를 삭제합니다.\n\n" +
//                "(api-result-code)\n\n" +
//                "0 : 정상 동작"
//    )
//    @DeleteMapping("/row/{index}")
//    fun api3(
//        @Parameter(hidden = true)
//        httpServletResponse: HttpServletResponse,
//        @Parameter(name = "index", description = "글 인덱스", example = "1")
//        @PathVariable("index")
//        index: Long
//    ) {
//        service.api3(httpServletResponse, index)
//    }


    ////
    @Operation(
        summary = "N4 : DB Rows 조회 테스트",
        description = "테스트 테이블의 모든 Rows 를 반환합니다.\n\n" +
                "(api-result-code)\n\n" +
                "0 : 정상 동작"
    )
    @GetMapping(
        path = ["/rows"],
        consumes = [MediaType.ALL_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @ResponseBody
    fun api4(
        @Parameter(hidden = true)
        serverHttpResponse: ServerHttpResponse
    ): Mono<Api4OutputVo> {
        return service.api4(
            serverHttpResponse
        )
    }

    data class Api4OutputVo(
        @Schema(description = "아이템 리스트", required = true)
        @JsonProperty("testEntityVoList")
        val testEntityVoList: List<TestEntityVo>
    ) {
        @Schema(description = "아이템")
        data class TestEntityVo(
            @Schema(description = "글 고유번호", required = true, example = "1234")
            @JsonProperty("uid")
            val uid: Long,
            @Schema(description = "글 본문", required = true, example = "테스트 텍스트입니다.")
            @JsonProperty("content")
            val content: String,
            @Schema(description = "자동 생성 숫자", required = true, example = "21345")
            @JsonProperty("randomNum")
            val randomNum: Int,
            @Schema(description = "글 작성일", required = true, example = "2022-10-11T02:21:36.779")
            @JsonProperty("createDate")
            val createDate: String,
            @Schema(description = "글 수정일", required = true, example = "2022-10-11T02:21:36.779")
            @JsonProperty("updateDate")
            val updateDate: String
        )
    }


//    ////
//    @Operation(
//        summary = "N5 : DB 테이블의 random_num 컬럼 근사치 기준으로 정렬한 리스트 조회 API",
//        description = "테이블의 row 중 random_num 컬럼과 num 파라미터의 값의 근사치로 정렬한 리스트 반환\n\n" +
//                "(api-result-code)\n\n" +
//                "0 : 정상 동작"
//    )
//    @GetMapping("/rows/order-by-random-num-nearest")
//    fun api5(
//        @Parameter(hidden = true)
//        httpServletResponse: HttpServletResponse,
//        @Parameter(name = "num", description = "근사값 정렬의 기준", example = "1")
//        @RequestParam("num")
//        num: Int
//    ): Api5OutputVo? {
//        return service.api5(httpServletResponse, num)
//    }
//
//    data class Api5OutputVo(
//        @Schema(description = "아이템 리스트", required = true)
//        @JsonProperty("testEntityVoList")
//        val testEntityVoList: List<TestEntityVo>
//    ) {
//        @Schema(description = "아이템")
//        data class TestEntityVo(
//            @Schema(description = "글 고유번호", required = true, example = "1234")
//            @JsonProperty("uid")
//            val uid: Long,
//            @Schema(description = "글 본문", required = true, example = "테스트 텍스트입니다.")
//            @JsonProperty("content")
//            val content: String,
//            @Schema(description = "자동 생성 숫자", required = true, example = "21345")
//            @JsonProperty("randomNum")
//            val randomNum: Int,
//            @Schema(description = "글 작성일", required = true, example = "2022-10-11T02:21:36.779")
//            @JsonProperty("createDate")
//            val createDate: String,
//            @Schema(description = "글 수정일", required = true, example = "2022-10-11T02:21:36.779")
//            @JsonProperty("updateDate")
//            val updateDate: String,
//            @Schema(description = "기준과의 절대거리", required = true, example = "34")
//            @JsonProperty("distance")
//            val distance: Int
//        )
//    }
//
//
//    ////
//    @Operation(
//        summary = "N6 : DB 테이블의 row_create_date 컬럼 근사치 기준으로 정렬한 리스트 조회 API",
//        description = "테이블의 row 중 row_create_date 컬럼과 dateString 파라미터의 값의 근사치로 정렬한 리스트 반환\n\n" +
//                "(api-result-code)\n\n" +
//                "0 : 정상 동작"
//    )
//    @GetMapping("/rows/order-by-create-date-nearest")
//    fun api6(
//        @Parameter(hidden = true)
//        httpServletResponse: HttpServletResponse,
//        @Parameter(name = "dateString", description = "원하는 날짜", example = "2022-10-11T02:21:36.779")
//        @RequestParam("dateString")
//        dateString: String
//    ): Api6OutputVo? {
//        return service.api6(httpServletResponse, dateString)
//    }
//
//    data class Api6OutputVo(
//        @Schema(description = "아이템 리스트", required = true)
//        @JsonProperty("testEntityVoList")
//        val testEntityVoList: List<TestEntityVo>
//    ) {
//        @Schema(description = "아이템")
//        data class TestEntityVo(
//            @Schema(description = "글 고유번호", required = true, example = "1234")
//            @JsonProperty("uid")
//            val uid: Long,
//            @Schema(description = "글 본문", required = true, example = "테스트 텍스트입니다.")
//            @JsonProperty("content")
//            val content: String,
//            @Schema(description = "자동 생성 숫자", required = true, example = "21345")
//            @JsonProperty("randomNum")
//            val randomNum: Int,
//            @Schema(description = "글 작성일", required = true, example = "2022-10-11T02:21:36.779")
//            @JsonProperty("createDate")
//            val createDate: String,
//            @Schema(description = "글 수정일", required = true, example = "2022-10-11T02:21:36.779")
//            @JsonProperty("updateDate")
//            val updateDate: String,
//            @Schema(description = "기준과의 절대차이(초)", required = true, example = "34")
//            @JsonProperty("timeDiffSec")
//            val timeDiffSec: Long
//        )
//    }
//
//
//    ////
//    @Operation(
//        summary = "N7 : DB Rows 조회 테스트 (페이징)",
//        description = "테스트 테이블의 Rows 를 페이징하여 반환합니다.\n\n" +
//                "(api-result-code)\n\n" +
//                "0 : 정상 동작"
//    )
//    @GetMapping("/rows/paging")
//    fun api7(
//        @Parameter(hidden = true)
//        httpServletResponse: HttpServletResponse,
//        @Parameter(name = "page", description = "원하는 페이지(1 부터 시작)", example = "1")
//        @RequestParam("page")
//        page: Int,
//        @Parameter(name = "pageElementsCount", description = "페이지 아이템 개수", example = "10")
//        @RequestParam("pageElementsCount")
//        pageElementsCount: Int
//    ): Api7OutputVo? {
//        return service.api7(httpServletResponse, page, pageElementsCount)
//    }
//
//    data class Api7OutputVo(
//        @Schema(description = "아이템 전체 개수", required = true, example = "100")
//        @JsonProperty("totalElements")
//        val totalElements: Long,
//        @Schema(description = "아이템 리스트", required = true)
//        @JsonProperty("testEntityVoList")
//        val testEntityVoList: List<TestEntityVo>
//    ) {
//        @Schema(description = "아이템")
//        data class TestEntityVo(
//            @Schema(description = "글 고유번호", required = true, example = "1234")
//            @JsonProperty("uid")
//            val uid: Long,
//            @Schema(description = "글 본문", required = true, example = "테스트 텍스트입니다.")
//            @JsonProperty("content")
//            val content: String,
//            @Schema(description = "자동 생성 숫자", required = true, example = "23456")
//            @JsonProperty("randomNum")
//            val randomNum: Int,
//            @Schema(description = "글 작성일", required = true, example = "2022-10-11T02:21:36.779")
//            @JsonProperty("createDate")
//            val createDate: String,
//            @Schema(description = "글 수정일", required = true, example = "2022-10-11T02:21:36.779")
//            @JsonProperty("updateDate")
//            val updateDate: String
//        )
//    }
//
//
//    ////
//    @Operation(
//        summary = "N8 : DB Rows 조회 테스트 (네이티브 쿼리 페이징)",
//        description = "테스트 테이블의 Rows 를 네이티브 쿼리로 페이징하여 반환합니다.\n\n" +
//                "num 을 기준으로 근사치 정렬도 수행합니다.\n\n" +
//                "(api-result-code)\n\n" +
//                "0 : 정상 동작"
//    )
//    @GetMapping("/rows/native-paging")
//    fun api8(
//        @Parameter(hidden = true)
//        httpServletResponse: HttpServletResponse,
//        @Parameter(name = "page", description = "원하는 페이지(1 부터 시작)", example = "1")
//        @RequestParam("page")
//        page: Int,
//        @Parameter(name = "pageElementsCount", description = "페이지 아이템 개수", example = "10")
//        @RequestParam("pageElementsCount")
//        pageElementsCount: Int,
//        @Parameter(name = "num", description = "근사값의 기준", example = "1")
//        @RequestParam("num")
//        num: Int
//    ): Api8OutputVo? {
//        return service.api8(httpServletResponse, page, pageElementsCount, num)
//    }
//
//    data class Api8OutputVo(
//        @Schema(description = "아이템 전체 개수", required = true, example = "100")
//        @JsonProperty("totalElements")
//        val totalElements: Long,
//        @Schema(description = "아이템 리스트", required = true)
//        @JsonProperty("testEntityVoList")
//        val testEntityVoList: List<TestEntityVo>
//    ) {
//        @Schema(description = "아이템")
//        data class TestEntityVo(
//            @Schema(description = "글 고유번호", required = true, example = "1")
//            @JsonProperty("uid")
//            val uid: Long,
//            @Schema(description = "글 본문", required = true, example = "테스트 텍스트입니다.")
//            @JsonProperty("content")
//            val content: String,
//            @Schema(description = "자동 생성 숫자", required = true, example = "21345")
//            @JsonProperty("randomNum")
//            val randomNum: Int,
//            @Schema(description = "글 작성일", required = true, example = "2022-10-11T02:21:36.779")
//            @JsonProperty("createDate")
//            val createDate: String,
//            @Schema(description = "글 수정일", required = true, example = "2022-10-11T02:21:36.779")
//            @JsonProperty("updateDate")
//            val updateDate: String,
//            @Schema(description = "기준과의 절대거리", required = true, example = "34")
//            @JsonProperty("distance")
//            val distance: Int
//        )
//    }
//
//
//    ////
//    @Operation(
//        summary = "N9 : DB Row 수정 테스트",
//        description = "테스트 테이블의 Row 하나를 수정합니다.\n\n" +
//                "(api-result-code)\n\n" +
//                "0 : 정상 동작\n\n" +
//                "1 : testTableUid 에 해당하는 정보가 DB에 없음\n\n"
//    )
//    @PatchMapping("/row/{testTableUid}")
//    fun api9(
//        @Parameter(hidden = true)
//        httpServletResponse: HttpServletResponse,
//        @Parameter(name = "testTableUid", description = "test 테이블의 uid", example = "1")
//        @PathVariable("testTableUid")
//        testTableUid: Long,
//        @RequestBody
//        inputVo: Api9InputVo
//    ): Api9OutputVo? {
//        return service.api9(httpServletResponse, testTableUid, inputVo)
//    }
//
//    data class Api9InputVo(
//        @Schema(description = "글 본문", required = true, example = "테스트 텍스트 수정글입니다.")
//        @JsonProperty("content")
//        val content: String
//    )
//
//    data class Api9OutputVo(
//        @Schema(description = "글 고유번호", required = true, example = "1234")
//        @JsonProperty("uid")
//        val uid: Long,
//        @Schema(description = "글 본문", required = true, example = "테스트 텍스트입니다.")
//        @JsonProperty("content")
//        val content: String,
//        @Schema(description = "자동 생성 숫자", required = true, example = "21345")
//        @JsonProperty("randomNum")
//        val randomNum: Int,
//        @Schema(description = "글 작성일", required = true, example = "2022-10-11T02:21:36.779")
//        @JsonProperty("createDate")
//        val createDate: String,
//        @Schema(description = "글 수정일", required = true, example = "2022-10-11T02:21:36.779")
//        @JsonProperty("updateDate")
//        val updateDate: String
//    )
//
//
//    ////
//    @Operation(
//        summary = "N10 : DB Row 수정 테스트 (네이티브 쿼리)",
//        description = "테스트 테이블의 Row 하나를 네이티브 쿼리로 수정합니다.\n\n" +
//                "(api-result-code)\n\n" +
//                "0 : 정상 동작\n\n" +
//                "1 : testTableUid 에 해당하는 정보가 DB에 없음\n\n"
//    )
//    @PatchMapping("/row/{testTableUid}/native-query")
//    fun api10(
//        @Parameter(hidden = true)
//        httpServletResponse: HttpServletResponse,
//        @Parameter(name = "testTableUid", description = "test 테이블의 uid", example = "1")
//        @PathVariable("testTableUid")
//        testTableUid: Long,
//        @RequestBody
//        inputVo: Api10InputVo
//    ) {
//        return service.api10(httpServletResponse, testTableUid, inputVo)
//    }
//
//    data class Api10InputVo(
//        @Schema(description = "글 본문", required = true, example = "테스트 텍스트 수정글입니다.")
//        @JsonProperty("content")
//        val content: String
//    )
//
//
//    ////
//    @Operation(
//        summary = "N11 : DB 정보 검색 테스트",
//        description = "글 본문 내용중 searchKeyword 가 포함된 rows 를 검색하여 반환합니다.\n\n" +
//                "(api-result-code)\n\n" +
//                "0 : 정상 동작"
//    )
//    @GetMapping("/search-content")
//    fun api11(
//        @Parameter(hidden = true)
//        httpServletResponse: HttpServletResponse,
//        @Parameter(name = "page", description = "원하는 페이지(1 부터 시작)", example = "1")
//        @RequestParam("page")
//        page: Int,
//        @Parameter(name = "pageElementsCount", description = "페이지 아이템 개수", example = "10")
//        @RequestParam("pageElementsCount")
//        pageElementsCount: Int,
//        @Parameter(name = "searchKeyword", description = "검색어", example = "테스트")
//        @RequestParam("searchKeyword")
//        searchKeyword: String
//    ): Api11OutputVo? {
//        return service.api11(httpServletResponse, page, pageElementsCount, searchKeyword)
//    }
//
//    data class Api11OutputVo(
//        @Schema(description = "아이템 전체 개수", required = true, example = "100")
//        @JsonProperty("totalElements")
//        val totalElements: Long,
//        @Schema(description = "아이템 리스트", required = true)
//        @JsonProperty("testEntityVoList")
//        val testEntityVoList: List<TestEntityVo>
//    ) {
//        @Schema(description = "아이템")
//        data class TestEntityVo(
//            @Schema(description = "글 고유번호", required = true, example = "1")
//            @JsonProperty("uid")
//            val uid: Long,
//            @Schema(description = "글 본문", required = true, example = "테스트 텍스트입니다.")
//            @JsonProperty("content")
//            val content: String,
//            @Schema(description = "자동 생성 숫자", required = true, example = "21345")
//            @JsonProperty("randomNum")
//            val randomNum: Int,
//            @Schema(description = "글 작성일", required = true, example = "2022-10-11T02:21:36.779")
//            @JsonProperty("createDate")
//            val createDate: String,
//            @Schema(description = "글 수정일", required = true, example = "2022-10-11T02:21:36.779")
//            @JsonProperty("updateDate")
//            val updateDate: String
//        )
//    }
//
//
//    ////
//    @Operation(
//        summary = "N12 : 트랜젝션 동작 테스트",
//        description = "정보 입력 후 Exception 이 발생했을 때 롤백되어 데이터가 저장되지 않는지를 테스트하는 API\n\n" +
//                "(api-result-code)\n\n" +
//                "0 : 정상 동작"
//    )
//    @PostMapping("/transaction-rollback-sample")
//    fun api12(
//        @Parameter(hidden = true)
//        httpServletResponse: HttpServletResponse
//    ) {
//        service.api12(httpServletResponse)
//    }
//
//
//    ////
//    @Operation(
//        summary = "N13 : 트랜젝션 비동작 테스트",
//        description = "트랜젝션 처리를 하지 않았을 때, DB 정보 입력 후 Exception 이 발생 했을 때 의 테스트 API\n\n" +
//                "(api-result-code)\n\n" +
//                "0 : 정상 동작"
//    )
//    @PostMapping("/no-transaction-exception-sample")
//    fun api13(
//        @Parameter(hidden = true)
//        httpServletResponse: HttpServletResponse
//    ) {
//        service.api13(httpServletResponse)
//    }
//
//
//    ////
//    @Operation(
//        summary = "N14 : DB Rows 조회 테스트 (중복 없는 네이티브 쿼리 페이징)",
//        description = "테스트 테이블의 Rows 를 네이티브 쿼리로 중복없이 페이징하여 반환합니다.\n\n" +
//                "num 을 기준으로 근사치 정렬도 수행합니다.\n\n" +
//                "(api-result-code)\n\n" +
//                "0 : 정상 동작"
//    )
//    @GetMapping("/rows/native-paging-no-duplication")
//    fun api14(
//        @Parameter(hidden = true)
//        httpServletResponse: HttpServletResponse,
//        @Parameter(name = "lastItemUid", description = "이전 페이지에서 받은 마지막 아이템의 Uid (첫 요청이면 null)", example = "1")
//        @RequestParam("lastItemUid")
//        lastItemUid: Long?,
//        @Parameter(name = "pageElementsCount", description = "페이지 아이템 개수", example = "10")
//        @RequestParam("pageElementsCount")
//        pageElementsCount: Int,
//        @Parameter(name = "num", description = "근사값의 기준", example = "1")
//        @RequestParam("num")
//        num: Int
//    ): Api14OutputVo? {
//        return service.api14(httpServletResponse, lastItemUid, pageElementsCount, num)
//    }
//
//    data class Api14OutputVo(
//        @Schema(description = "아이템 전체 개수", required = true, example = "100")
//        @JsonProperty("totalElements")
//        val totalElements: Long,
//        @Schema(description = "아이템 리스트", required = true)
//        @JsonProperty("testEntityVoList")
//        val testEntityVoList: List<TestEntityVo>
//    ) {
//        @Schema(description = "아이템")
//        data class TestEntityVo(
//            @Schema(description = "글 고유번호", required = true, example = "1")
//            @JsonProperty("uid")
//            val uid: Long,
//            @Schema(description = "글 본문", required = true, example = "테스트 텍스트입니다.")
//            @JsonProperty("content")
//            val content: String,
//            @Schema(description = "자동 생성 숫자", required = true, example = "21345")
//            @JsonProperty("randomNum")
//            val randomNum: Int,
//            @Schema(description = "글 작성일", required = true, example = "2022-10-11T02:21:36.779")
//            @JsonProperty("createDate")
//            val createDate: String,
//            @Schema(description = "글 수정일", required = true, example = "2022-10-11T02:21:36.779")
//            @JsonProperty("updateDate")
//            val updateDate: String,
//            @Schema(description = "기준과의 절대거리", required = true, example = "34")
//            @JsonProperty("distance")
//            val distance: Int
//        )
//    }
}
package com.raillylinker.springboot_webflux_template.controllers.c6_service1_tk_v1_test

import com.raillylinker.springboot_webflux_template.custom_dis.EmailSenderUtilDi
import com.raillylinker.springboot_webflux_template.custom_dis.NaverSmsUtilDi
import com.raillylinker.springboot_webflux_template.custom_objects.ExcelFileUtilObject
import com.raillylinker.springboot_webflux_template.custom_objects.PdfGenerator
import com.raillylinker.springboot_webflux_template.custom_objects.ThymeleafParserUtilObject
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.http.HttpStatus
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.HashMap

@Service
class C6Service1TkV1TestService(
    // (프로젝트 실행시 사용 설정한 프로필명 (ex : dev8080, prod80, local8080, 설정 안하면 default 반환))
    @Value("\${spring.profiles.active:default}") private var activeProfile: String,
    // 이메일 발송 유틸
    private val emailSenderUtilDi: EmailSenderUtilDi,
    // 네이버 메시지 발송 유틸
    private val naverSmsUtilDi: NaverSmsUtilDi
) {
    // <멤버 변수 공간>
    private val classLogger: Logger = LoggerFactory.getLogger(this::class.java)


    // ---------------------------------------------------------------------------------------------
    // <공개 메소드 공간>
    fun api1(
        serverHttpResponse: ServerHttpResponse,
        inputVoMono: Mono<C6Service1TkV1TestController.Api1InputVo>
    ): Mono<Void> {
        return inputVoMono.flatMap { inputVo ->
            // 첨부 파일 리스트를 File 리스트로 변환
            val attachedFilesMono: Mono<List<File>> = Flux.fromIterable(inputVo.multipartFileList.orEmpty())
                .flatMap { filePart ->
                    val (name, extension) = filePart.filename().split(".", limit = 2)
                    val file = File.createTempFile(name, ".$extension")
                    filePart.transferTo(file).thenReturn(file)
                }
                .collectList()
            attachedFilesMono.flatMap { attachedFiles ->
                emailSenderUtilDi.sendMessageMail(
                    inputVo.senderName,
                    inputVo.receiverEmailAddressList.toTypedArray(),
                    inputVo.carbonCopyEmailAddressList?.toTypedArray(),
                    inputVo.subject,
                    inputVo.message,
                    attachedFiles
                )

                for (file in attachedFiles) {
                    file.delete()
                }

                serverHttpResponse.setStatusCode(HttpStatus.OK)
                serverHttpResponse.headers.set("api-result-code", "0")
                serverHttpResponse.setComplete()
            }
        }
    }

    ////
    fun api2(
        serverHttpResponse: ServerHttpResponse,
        inputVoMono: Mono<C6Service1TkV1TestController.Api2InputVo>
    ): Mono<Void> {
        return inputVoMono.flatMap { inputVo ->
            // 첨부 파일 리스트를 File 리스트로 변환
            val attachedFilesMono: Mono<List<File>> = Flux.fromIterable(inputVo.multipartFileList.orEmpty())
                .flatMap { filePart ->
                    val (name, extension) = filePart.filename().split(".", limit = 2)
                    val file = File.createTempFile(name, ".$extension")
                    filePart.transferTo(file).thenReturn(file)
                }
                .collectList()
            attachedFilesMono.flatMap { attachedFiles ->
                emailSenderUtilDi.sendThymeLeafHtmlMail(
                    inputVo.senderName,
                    inputVo.receiverEmailAddressList.toTypedArray(),
                    inputVo.carbonCopyEmailAddressList?.toTypedArray(),
                    inputVo.subject,
                    "template_c6_n2/html_email_sample",
                    hashMapOf(
                        Pair("message", inputVo.message)
                    ),
                    null,
                    hashMapOf(
                        "html_email_sample_css" to ClassPathResource("static/resource_c6_n2/html_email_sample.css"),
                        "image_sample" to ClassPathResource("static/resource_c6_n2/image_sample.jpg")
                    ),
                    attachedFiles
                )

                for (file in attachedFiles) {
                    file.delete()
                }

                serverHttpResponse.setStatusCode(HttpStatus.OK)
                serverHttpResponse.headers.set("api-result-code", "0")
                serverHttpResponse.setComplete()
            }
        }
    }

    ////
    fun api3(serverHttpResponse: ServerHttpResponse, inputVo: C6Service1TkV1TestController.Api3InputVo): Mono<Void> {
        val phoneNumberSplit = inputVo.phoneNumber.split(")") // ["82", "010-0000-0000"]

        // 국가 코드 (ex : 82)
        val countryCode = phoneNumberSplit[0]

        // 전화번호 (ex : "01000000000")
        val phoneNumber = (phoneNumberSplit[1].replace("-", "")).replace(" ", "")

        // SMS 전송
        return naverSmsUtilDi.sendSms(
            NaverSmsUtilDi.SendSmsInputVo(
                countryCode,
                phoneNumber,
                inputVo.smsMessage
            )
        ).flatMap {
            serverHttpResponse.setStatusCode(HttpStatus.OK)
            serverHttpResponse.headers.set("api-result-code", "0")
            serverHttpResponse.setComplete()
        }
    }

    ////
    fun api4(
        serverHttpResponse: ServerHttpResponse,
        inputVoMono: Mono<C6Service1TkV1TestController.Api4InputVo>
    ): Mono<C6Service1TkV1TestController.Api4OutputVo> {
        return inputVoMono.flatMap { inputVo ->
            inputVo.excelFile.content().reduce(DataBuffer::write)
                .map { dataBuffer ->
                    val excelData = ExcelFileUtilObject.readExcel(
                        dataBuffer.asInputStream(),
                        inputVo.sheetIdx,
                        inputVo.rowRangeStartIdx,
                        inputVo.rowRangeEndIdx,
                        inputVo.columnRangeIdxList,
                        inputVo.minColumnLength
                    )

                    // 리사이즈된 이미지를 ByteArrayResource로 만들어서 반환
                    serverHttpResponse.setStatusCode(HttpStatus.OK)
                    serverHttpResponse.headers.set("api-result-code", "0")
                    C6Service1TkV1TestController.Api4OutputVo(
                        excelData?.size ?: 0,
                        excelData.toString()
                    )
                }
        }
    }

    ////
    fun api5(serverHttpResponse: ServerHttpResponse): Mono<Void> {
        // 파일 저장 디렉토리 경로
        val saveDirectoryPathString = "./files/temp"
        val saveDirectoryPath = Paths.get(saveDirectoryPathString).toAbsolutePath().normalize()
        // 파일 저장 디렉토리 생성
        Files.createDirectories(saveDirectoryPath)

        // 요청 시간을 문자열로
        val timeString = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH_mm_ss_SSS"))

        // 확장자 포함 파일명 생성
        val saveFileName = "temp_${timeString}.xlsx"

        // 파일 저장 경로와 파일명(with index) 을 합친 path 객체
        val fileTargetPath = saveDirectoryPath.resolve(saveFileName).normalize()
        val file = fileTargetPath.toFile()

        val inputExcelSheetDataMap: HashMap<String, List<List<String>>> = hashMapOf()
        inputExcelSheetDataMap["testSheet1"] = listOf(
            listOf("1-1", "1-2", "1-3"),
            listOf("2-1", "2-2", "2-3"),
            listOf("3-1", "3-2", "3-3")
        )
        inputExcelSheetDataMap["testSheet2"] = listOf(
            listOf("1-1", "1-2"),
            listOf("2-1", "2-2")
        )

        ExcelFileUtilObject.writeExcel(file.outputStream(), inputExcelSheetDataMap)

        serverHttpResponse.setStatusCode(HttpStatus.OK)
        serverHttpResponse.headers.set("api-result-code", "0")
        return serverHttpResponse.setComplete()
    }

    ////
    fun api6(serverHttpResponse: ServerHttpResponse): Mono<Void> {
        // thymeLeaf 엔진으로 파싱한 HTML String 가져오기
        // 여기서 가져온 HTML 내에 기입된 static resources 의 경로는 절대경로가 아님
        val htmlString = ThymeleafParserUtilObject.parseHtmlFileToHtmlString(
            "template_c6_n6/html_to_pdf_sample", // thymeLeaf Html 이름 (ModelAndView 의 사용과 동일)
            // thymeLeaf 에 전해줄 데이터 Map
            mapOf(
                "title" to "PDF 변환 테스트"
            )
        )

        // htmlString 을 PDF 로 변환하여 저장
        // XHTML 1.0(strict), CSS 2.1 (@page 의 size 는 가능)
        PdfGenerator.createPdfFileFromHtmlString(
            "./files/temp",
            "temp(${
                LocalDateTime.now().format(
                    DateTimeFormatter.ofPattern("yyyy-MM-dd-HH_mm-ss-SSS")
                )
            }).pdf",
            htmlString,
            arrayListOf(
                "/static/resource_global/fonts/for_itext/NanumGothic.ttf",
                "/static/resource_global/fonts/for_itext/NanumMyeongjo.ttf"
            )
        )

        serverHttpResponse.setStatusCode(HttpStatus.OK)
        serverHttpResponse.headers.set("api-result-code", "0")
        return serverHttpResponse.setComplete()
    }
}
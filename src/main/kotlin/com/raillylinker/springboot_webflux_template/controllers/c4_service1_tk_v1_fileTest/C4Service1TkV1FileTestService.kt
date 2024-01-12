package com.raillylinker.springboot_webflux_template.controllers.c4_service1_tk_v1_fileTest

import com.raillylinker.springboot_webflux_template.custom_objects.CustomUtilObject
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.Resource
import org.springframework.http.HttpStatus
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.stereotype.Service
import org.springframework.util.FileCopyUtils
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import java.io.File
import java.io.FileOutputStream
import java.nio.file.Files
import java.nio.file.Paths
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.zip.ZipOutputStream

@Service
class C4Service1TkV1FileTestService(
    // (프로젝트 실행시 사용 설정한 프로필명 (ex : dev8080, prod80, local8080, 설정 안하면 default 반환))
    @Value("\${spring.profiles.active:default}") private var activeProfile: String
) {
    // <멤버 변수 공간>
    private val classLogger: Logger = LoggerFactory.getLogger(this::class.java)


    // ---------------------------------------------------------------------------------------------
    // <공개 메소드 공간>
    fun api1(
        serverHttpResponse: ServerHttpResponse,
        inputVoMono: Mono<C4Service1TkV1FileTestController.Api1InputVo>
    ): Mono<C4Service1TkV1FileTestController.Api1OutputVo> {
        return inputVoMono.flatMap { inputVo ->
            val baseDirectory = "./files/temp/"

            // 디렉토리가 없으면 생성
            val directory = Paths.get(baseDirectory)
            if (!Files.exists(directory)) {
                Files.createDirectories(directory)
            }

            val filePath = CustomUtilObject.resolveDuplicateFileName(
                Paths.get(
                    "$baseDirectory${inputVo.multipartFile.filename()}"
                )
            )

            // 비동기적으로 파일 저장
            val fileSaveProcessMono1 = inputVo.multipartFile.transferTo(
                filePath
            ).subscribeOn(Schedulers.boundedElastic())

            fileSaveProcessMono1.then(
                Mono.create { sink ->
                    serverHttpResponse.setStatusCode(HttpStatus.OK)
                    serverHttpResponse.headers.set("api-result-code", "0")
                    sink.success(
                        C4Service1TkV1FileTestController.Api1OutputVo(
                            "http://127.0.0.1:8080/service1/tk/v1/file-test/download-from-temp/${filePath.fileName}"
                        )
                    )
                }
            )
        }
    }

    ////
    fun api2(serverHttpResponse: ServerHttpResponse, fileName: String): Mono<Resource> {
        // 파일 절대 경로 및 파일명 (프로젝트 루트 경로에 있는 files/temp 폴더를 기준으로 함)
        val filePath = "./files/temp/$fileName"
        val serverFilePathObject = Paths.get(filePath)

        when {
            Files.notExists(serverFilePathObject) -> {
                // 파일이 없을 때
                serverHttpResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                serverHttpResponse.headers.set("api-result-code", "1")

                serverHttpResponse.setComplete()
            }

            Files.isDirectory(serverFilePathObject) -> {
                // 파일이 디렉토리일때
                serverHttpResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                serverHttpResponse.headers.set("api-result-code", "1")

                serverHttpResponse.setComplete()
            }
        }

        // 반환값에 전해줄 FIS
        val file = File(filePath)

        serverHttpResponse.setStatusCode(HttpStatus.OK)
        serverHttpResponse.headers.set("api-result-code", "0")
        serverHttpResponse.headers.set("Content-Disposition", "attachment; filename=\"" + file.name + "\"")

        return Mono.just(
            // fileInputStream 을 Resource 타입으로 변형하여 반환
            ByteArrayResource(FileCopyUtils.copyToByteArray(file.inputStream()))
        )
    }

    // todo : reactor 코드 개선
    ////
    fun api3(serverHttpResponse: ServerHttpResponse): Mono<Void> {
        // 프로젝트 루트 경로 (프로젝트 settings.gradle 이 있는 경로)
        val projectRootAbsolutePathString: String = File("").absolutePath

        // 파일 경로 리스트
        val filePathList = listOf(
            "$projectRootAbsolutePathString/src/main/resources/static/resource_c4_n3/1.txt",
            "$projectRootAbsolutePathString/src/main/resources/static/resource_c4_n3/2.xlsx",
            "$projectRootAbsolutePathString/src/main/resources/static/resource_c4_n3/3.png",
            "$projectRootAbsolutePathString/src/main/resources/static/resource_c4_n3/4.mp4"
        )

        val baseDirectory = "./files/temp/"

        // 디렉토리가 없으면 생성
        val saveDirectoryPathString = Paths.get(baseDirectory)
        if (!Files.exists(saveDirectoryPathString)) {
            Files.createDirectories(saveDirectoryPathString)
        }

        // 확장자 포함 파일명 생성
        val fileTargetPath = CustomUtilObject.resolveDuplicateFileName(
            Paths.get(
                "${baseDirectory}zipped.zip"
            )
        )

        // 압축 파일 생성
        val zipOutputStream = ZipOutputStream(FileOutputStream(fileTargetPath.toFile()))

        for (filePath in filePathList) {
            val file = File(filePath)
            if (file.exists()) {
                CustomUtilObject.addToZip(file, file.name, zipOutputStream)
            }
        }

        zipOutputStream.close()

        serverHttpResponse.setStatusCode(HttpStatus.OK)
        serverHttpResponse.headers.set("api-result-code", "0")
        return serverHttpResponse.setComplete()
    }

    ////
    fun api3Dot1(serverHttpResponse: ServerHttpResponse): Mono<Void> {
        // 프로젝트 루트 경로 (프로젝트 settings.gradle 이 있는 경로)
        val projectRootAbsolutePathString: String = File("").absolutePath

        // 압축 대상 디렉토리
        val sourceDir = File("$projectRootAbsolutePathString/src/main/resources/static/resource_c4_n3")

        // 파일 저장 디렉토리 경로
        val saveDirectoryPathString = "./files/temp"
        val saveDirectoryPath = Paths.get(saveDirectoryPathString).toAbsolutePath().normalize()
        // 파일 저장 디렉토리 생성
        Files.createDirectories(saveDirectoryPath)

        // 확장자 포함 파일명 생성
        val fileTargetPath = saveDirectoryPath.resolve(
            "zipped_${LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH_mm_ss_SSS"))}.zip"
        ).normalize()

        // 압축 파일 생성
        ZipOutputStream(FileOutputStream(fileTargetPath.toFile())).use { zipOutputStream ->
            CustomUtilObject.compressDirectoryToZip(sourceDir, sourceDir.name, zipOutputStream)
        }

        serverHttpResponse.setStatusCode(HttpStatus.OK)
        serverHttpResponse.headers.set("api-result-code", "0")
        return serverHttpResponse.setComplete()
    }

    ////
    fun api4(serverHttpResponse: ServerHttpResponse): Mono<Void> {
        // 프로젝트 루트 경로 (프로젝트 settings.gradle 이 있는 경로)
        val projectRootAbsolutePathString: String = File("").absolutePath
        val filePathString =
            "$projectRootAbsolutePathString/src/main/resources/static/resource_c4_n4/test.zip"

        // 파일 저장 디렉토리 경로
        val saveDirectoryPathString = "./files/temp"
        val saveDirectoryPath = Paths.get(saveDirectoryPathString).toAbsolutePath().normalize()
        // 파일 저장 디렉토리 생성
        Files.createDirectories(saveDirectoryPath)

        // 요청 시간을 문자열로
        val timeString = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH_mm_ss_SSS"))

        // 확장자 포함 파일명 생성
        val saveFileName = "unzipped_${timeString}/"

        val fileTargetPath = saveDirectoryPath.resolve(saveFileName).normalize()

        CustomUtilObject.unzipFile(filePathString, fileTargetPath)

        serverHttpResponse.setStatusCode(HttpStatus.OK)
        serverHttpResponse.headers.set("api-result-code", "0")
        return serverHttpResponse.setComplete()
    }

    ////
    fun api5(serverHttpResponse: ServerHttpResponse, delayTimeSecond: Long): Mono<Resource> {
        // 프로젝트 루트 경로 (프로젝트 settings.gradle 이 있는 경로)
        val projectRootAbsolutePathString: String = File("").absolutePath

        // 파일 절대 경로 및 파일명 (프로젝트 루트 경로에 있는 files/temp 폴더를 기준으로 함)
        val filePath = "$projectRootAbsolutePathString/src/main/resources/static/resource_c4_n5/client_image_test.jpg"

        // 반환값에 전해줄 FIS
        val file = File(filePath)

        serverHttpResponse.setStatusCode(HttpStatus.OK)
        serverHttpResponse.headers.set("api-result-code", "0")
        serverHttpResponse.headers.set("Content-Disposition", "attachment; filename=\"" + file.name + "\"")

        return Mono.delay(Duration.ofSeconds(delayTimeSecond)).then(
            Mono.just(
                // fileInputStream 을 Resource 타입으로 변형하여 반환
                ByteArrayResource(FileCopyUtils.copyToByteArray(file.inputStream()))
            )
        )
    }
}
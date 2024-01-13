package com.raillylinker.springboot_webflux_template.controllers.c5_service1_tk_v1_mediaResourceProcess

import com.raillylinker.springboot_webflux_template.custom_objects.GifUtilObject
import com.raillylinker.springboot_webflux_template.custom_objects.ImageProcessUtilObject
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.ByteArrayResource
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import org.springframework.core.io.Resource
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.http.HttpStatus
import java.awt.image.BufferedImage
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import javax.imageio.ImageIO

@Service
class C5Service1TkV1MediaResourceProcessService(
    // (프로젝트 실행시 사용 설정한 프로필명 (ex : dev8080, prod80, local8080, 설정 안하면 default 반환))
    @Value("\${spring.profiles.active:default}") private var activeProfile: String
) {
    // <멤버 변수 공간>
    private val classLogger: Logger = LoggerFactory.getLogger(this::class.java)


    // ---------------------------------------------------------------------------------------------
    // <공개 메소드 공간>
    fun api1(
        serverHttpResponse: ServerHttpResponse,
        inputVoMono: Mono<C5Service1TkV1MediaResourceProcessController.Api1InputVo>
    ): Mono<Resource> {
        return inputVoMono.flatMap { inputVo ->
            // 이미지 파일의 확장자 확인
            val allowedExtensions = setOf("jpg", "jpeg", "bmp", "png", "gif")
            val fileName = inputVo.multipartImageFile.filename()
            val fileExtension = fileName.split(".").lastOrNull()?.lowercase(Locale.getDefault())

            if (fileExtension !in allowedExtensions) {
                serverHttpResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                serverHttpResponse.headers.set("api-result-code", "1")
                Mono.empty()
            } else {
                inputVo.multipartImageFile.content().reduce(DataBuffer::write)
                    .map { dataBuffer -> ByteArray(dataBuffer.readableByteCount()).apply { dataBuffer.read(this) } }
                    .map { byteArray ->
                        // 이미지 리사이징
                        val resizedImage = ImageProcessUtilObject.resizeImage(
                            byteArray,
                            inputVo.resizingWidth,
                            inputVo.resizingHeight,
                            inputVo.imageType
                        )

                        // 리사이즈된 이미지를 ByteArrayResource로 만들어서 반환
                        serverHttpResponse.setStatusCode(HttpStatus.OK)
                        serverHttpResponse.headers.set("api-result-code", "0")
                        serverHttpResponse.headers.set("Content-Disposition", "attachment; filename=\"$fileName\"")
                        ByteArrayResource(resizedImage)
                    }
            }
        }
    }


    ////
    fun api2(serverHttpResponse: ServerHttpResponse): Mono<Void> {
        // 프로젝트 루트 경로 (프로젝트 settings.gradle 이 있는 경로)
        val projectRootAbsolutePathString: String = File("").absolutePath

        val gifFilePathObject =
            Paths.get("$projectRootAbsolutePathString/src/main/resources/static/resource_c5_n2/test.gif")

        val frameSplit = ImageProcessUtilObject.gifToImageList(Files.newInputStream(gifFilePathObject))

        // 요청 시간을 문자열로
        val timeString = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH_mm_ss_SSS"))

        // 파일 저장 디렉토리 경로
        val saveDirectoryPathString = "./files/temp/$timeString"
        val saveDirectoryPath = Paths.get(saveDirectoryPathString).toAbsolutePath().normalize()
        // 파일 저장 디렉토리 생성
        Files.createDirectories(saveDirectoryPath)

        // 받은 파일 순회
        for (bufferedImageIndexedValue in frameSplit.withIndex()) {
            val bufferedImage = bufferedImageIndexedValue.value

            // 확장자 포함 파일명 생성
            val saveFileName = "${bufferedImageIndexedValue.index + 1}.png"

            // 파일 저장 경로와 파일명(with index) 을 합친 path 객체
            val fileTargetPath = saveDirectoryPath.resolve(saveFileName).normalize()

            // 파일 저장
            ImageIO.write(bufferedImage.frameBufferedImage, "png", fileTargetPath.toFile())
        }

        serverHttpResponse.setStatusCode(HttpStatus.OK)
        serverHttpResponse.headers.set("api-result-code", "0")
        return serverHttpResponse.setComplete()
    }


    ////
    fun api3(serverHttpResponse: ServerHttpResponse): Mono<Void> {
        // 프로젝트 루트 경로 (프로젝트 settings.gradle 이 있는 경로)
        val projectRootAbsolutePathString: String = File("").absolutePath

        // 파일 절대 경로 및 파일명
        val bufferedImageList = ArrayList<BufferedImage>()
        for (idx in 1..15) {
            val imageFilePathString =
                "$projectRootAbsolutePathString/src/main/resources/static/resource_c5_n3/gif_frame_images/${idx}.png"
            bufferedImageList.add(
                ImageIO.read(
                    Paths.get(imageFilePathString).toFile()
                )
            )
        }

        val saveDirectoryPathString = "./files/temp"
        val saveDirectoryPath = Paths.get(saveDirectoryPathString).toAbsolutePath().normalize()
        // 파일 저장 디렉토리 생성
        Files.createDirectories(saveDirectoryPath)
        val resultFileName = "${LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH_mm_ss_SSS"))}.gif"
        val fileTargetPath = saveDirectoryPath.resolve(resultFileName).normalize()

        val gifFrameList: ArrayList<GifUtilObject.GifFrame> = arrayListOf()
        for (bufferedImage in bufferedImageList) {
            gifFrameList.add(
                GifUtilObject.GifFrame(
                    bufferedImage,
                    30
                )
            )
        }

        ImageProcessUtilObject.imageListToGif(
            gifFrameList,
            fileTargetPath.toFile().outputStream()
        )

        serverHttpResponse.setStatusCode(HttpStatus.OK)
        serverHttpResponse.headers.set("api-result-code", "0")
        return serverHttpResponse.setComplete()
    }

    ////
    fun api4(
        serverHttpResponse: ServerHttpResponse,
        inputVoMono: Mono<C5Service1TkV1MediaResourceProcessController.Api4InputVo>
    ): Mono<Resource> {

        return inputVoMono.flatMap { inputVo ->
            val imageFile = inputVo.multipartImageFile

            // 이미지 파일의 확장자 확인
            val allowedExtensions = setOf("gif")
            val fileName = imageFile.filename()
            val fileExtension = fileName.split(".").lastOrNull()?.lowercase(Locale.getDefault())

            if (fileExtension !in allowedExtensions) {
                serverHttpResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                serverHttpResponse.headers.set("api-result-code", "1")
                Mono.empty()
            } else {
                // 리사이징
                imageFile.content().reduce(DataBuffer::write)
                    .map { dataBuffer ->
                        val resizedImage = ImageProcessUtilObject.resizeGifImage(
                            dataBuffer.asInputStream(),
                            inputVo.resizingWidth,
                            inputVo.resizingHeight
                        )

                        // 리사이즈된 이미지를 ByteArrayResource로 만들어서 반환
                        serverHttpResponse.setStatusCode(HttpStatus.OK)
                        serverHttpResponse.headers.set("api-result-code", "0")
                        serverHttpResponse.headers.set("Content-Disposition", "attachment; filename=\"$fileName\"")
                        ByteArrayResource(resizedImage)
                    }
            }
        }
    }

}
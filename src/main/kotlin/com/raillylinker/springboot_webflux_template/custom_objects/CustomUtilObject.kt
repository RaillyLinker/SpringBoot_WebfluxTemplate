package com.raillylinker.springboot_webflux_template.custom_objects

import org.springframework.core.io.FileSystemResource
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.util.UriBuilder
import org.thymeleaf.context.Context
import org.thymeleaf.spring6.SpringTemplateEngine
import org.thymeleaf.templatemode.TemplateMode
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.net.URI
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.regex.Pattern
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream
import kotlin.reflect.full.declaredMemberProperties

object CustomUtilObject {
    // (ThymeLeaf 엔진으로 랜더링 한 HTML String 을 반환)
    fun parseHtmlFileToHtmlString(justHtmlFileNameWithOutSuffix: String, variableDataMap: Map<String, Any?>): String {
        // 타임리프 resolver 설정
        val templateResolver = ClassLoaderTemplateResolver()
        templateResolver.prefix = "templates/" // static/templates 경로 아래에 있는 파일을 읽는다
        templateResolver.suffix = ".html" // .html로 끝나는 파일을 읽는다
        templateResolver.templateMode = TemplateMode.HTML // 템플릿은 html 형식

        // 스프링 template 엔진을 thymeleafResolver 를 사용하도록 설정
        val templateEngine = SpringTemplateEngine()
        templateEngine.setTemplateResolver(templateResolver)

        // 템플릿 엔진에서 사용될 변수 입력
        val context = Context()
        context.setVariables(variableDataMap)

        // 지정한 html 파일과 context 를 읽어 String 으로 반환
        return templateEngine.process(justHtmlFileNameWithOutSuffix, context)
    }

    // (Object 변수를 MultiValueMap 로 변환합니다.)
    fun <T : Any> objectToMultiValueStringMap(obj: T): MultiValueMap<String, String> {
        val formData = LinkedMultiValueMap<String, String>()
        obj::class.declaredMemberProperties.forEach { prop ->
            when (val value = prop.getter.call(obj)) {
                is List<*> -> {
                    for (va in value) {
                        if (va != null) {
                            formData.add(prop.name, va.toString())
                        }
                    }
                }

                else -> {
                    if (value != null) {
                        formData.add(prop.name, value.toString())
                    }
                }
            }
        }
        return formData
    }

    fun <T : Any> objectToMultiValueAnyMap(obj: T): MultiValueMap<String, Any> {
        val formData = LinkedMultiValueMap<String, Any>()
        obj::class.declaredMemberProperties.forEach { prop ->
            when (val value = prop.getter.call(obj)) {
                is List<*> -> {
                    for (va in value) {
                        if (va != null) {
                            if (va is FileSystemResource) {
                                formData.add(prop.name, va)
                            } else {
                                formData.add(prop.name, va.toString())
                            }
                        }
                    }
                }

                is FileSystemResource -> {
                    formData.add(prop.name, value)
                }

                else -> {
                    if (value != null) {
                        formData.add(prop.name, value.toString())
                    }
                }
            }
        }
        return formData
    }

    // (UriBuilder, path, queryParamVo 를 빌드하는 빌더 함수)
    fun <T : Any> buildToUri(uriBuilder: UriBuilder, path: String, queryParamVo: T?): URI {
        uriBuilder.path(path)

        if (queryParamVo != null) {
            queryParamVo::class.declaredMemberProperties.forEach { prop ->
                when (val value = prop.getter.call(queryParamVo)) {
                    is List<*> -> {
                        for (va in value) {
                            if (va != null) {
                                uriBuilder.queryParam(prop.name, va.toString())
                            }
                        }
                    }

                    else -> {
                        if (value != null) {
                            uriBuilder.queryParam(prop.name, value.toString())
                        }
                    }
                }
            }
        }

        return uriBuilder.build()
    }

    // (파일들을 ZipOutputStream 으로 추가)
    fun addToZip(file: File, fileName: String, zipOutputStream: ZipOutputStream) {
        FileInputStream(file).use { fileInputStream ->
            val zipEntry = ZipEntry(fileName)
            zipOutputStream.putNextEntry(zipEntry)
            val buffer = ByteArray(1024)
            var length: Int
            while (fileInputStream.read(buffer).also { length = it } > 0) {
                zipOutputStream.write(buffer, 0, length)
            }
            zipOutputStream.closeEntry()
        }
    }

    // (디렉토리 내 파일들을 ZipOutputStream 으로 추가)
    fun compressDirectoryToZip(directory: File, path: String, zipOutputStream: ZipOutputStream) {
        for (file in directory.listFiles() ?: emptyArray()) {
            if (file.isDirectory) {
                compressDirectoryToZip(file, "$path/${file.name}", zipOutputStream)
            } else {
                addToZip(file, "$path/${file.name}", zipOutputStream)
            }
        }
    }

    // (zip 파일을 압축 풀기)
    fun unzipFile(zipFilePath: String, destDirectory: Path) {
        ZipInputStream(FileInputStream(zipFilePath)).use { zipInputStream ->
            var entry: ZipEntry? = zipInputStream.nextEntry
            while (entry != null) {
                val newFile = destDirectory.resolve(entry.name).toFile()
                if (entry.isDirectory) {
                    newFile.mkdirs()
                } else {
                    newFile.parentFile.mkdirs() // Ensure directory structure is created
                    FileOutputStream(newFile).use { fos ->
                        val buffer = ByteArray(1024)
                        var length: Int
                        while (zipInputStream.read(buffer).also { length = it } > 0) {
                            fos.write(buffer, 0, length)
                        }
                    }
                }
                zipInputStream.closeEntry()
                entry = zipInputStream.nextEntry
            }
        }
    }

    // (byteArray 를 Hex String 으로 반환)
    fun bytesToHex(bytes: ByteArray): String {
        val builder = StringBuilder()
        for (b in bytes) {
            builder.append(String.format("%02x", b))
        }
        return builder.toString()
    }

    // (랜덤 영문 대소문자 + 숫자 문자열 생성)
    fun getRandomString(length: Int): String {
        val charset = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return (1..length)
            .map { charset.random() }
            .joinToString("")
    }

    // (이메일 적합성 검증)
    fun isValidEmail(email: String): Boolean {
        var err = false
        if (Pattern.compile("^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$").matcher(email).matches()) {
            err = true
        }
        return err
    }

    // (degree 를 radian 으로)
    fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }

    // (radian 을 degree 로)
    fun rad2deg(rad: Double): Double {
        return rad * 180 / Math.PI
    }

    // (파일 저장 시 중복 처리)
    // 같은 파일이 존재하면 (1), (2) 와 같이 숫자를 붙임
    fun resolveDuplicateFileName(filePath: Path): Path {
        var index = 1
        var resolvedPath = filePath
        while (Files.exists(resolvedPath)) {
            val fileName = filePath.fileName.toString()
            val extensionIndex = fileName.lastIndexOf('.')
            val baseName = if (extensionIndex != -1) fileName.substring(0, extensionIndex) else fileName
            val extension = if (extensionIndex != -1) fileName.substring(extensionIndex) else ""
            val duplicateName = "$baseName($index)$extension"
            resolvedPath = filePath.resolveSibling(duplicateName)
            index++
        }
        return resolvedPath
    }

    // (중복되지 않는 폴더 경로 가져오기)
    // 같은 폴더가 존재하면 (1), (2) 와 같이 숫자를 붙임
    fun getUniqueDirectoryPath(basePath: String): Path {
        var directoryPath = basePath
        var counter = 1

        while (File(directoryPath).exists()) {
            directoryPath = "${basePath}(${counter++})/"
        }

        return Paths.get(directoryPath)
    }
}
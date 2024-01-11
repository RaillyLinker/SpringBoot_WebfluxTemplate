package com.raillylinker.springboot_webflux_template.custom_dis

import com.raillylinker.springboot_webflux_template.custom_classes.CustomDataClasses
import com.raillylinker.springboot_webflux_template.data_sources.network_sources.SensApigwNtrussComRequestApis
import org.apache.commons.codec.binary.Base64
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

// [Naver SMS 발송 유틸 객체]
@Service
class NaverSmsUtilDi(
    @Value("\${customConfig.naverSms.accessKey}")
    private var accessKey: String,
    @Value("\${customConfig.naverSms.secretKey}")
    private var secretKey: String,
    @Value("\${customConfig.naverSms.serviceId}")
    private var serviceId: String,
    @Value("\${customConfig.naverSms.phoneNumber}")
    private var phoneNumber: String
) {
    // <멤버 변수 공간>


    // ---------------------------------------------------------------------------------------------
    // <공개 메소드 공간>
    fun sendSms(inputVo: SendSmsInputVo): Mono<CustomDataClasses.WebClientResponse<Unit?>> {
        val time = System.currentTimeMillis()

        // timeStamp 시그니쳐 생성
        val message = StringBuilder()
            .append("POST")
            .append(" ")
            .append("/sms/v2/services/$serviceId/messages")
            .append("\n")
            .append(time.toString())
            .append("\n")
            .append(accessKey)
            .toString()
        val mac = Mac.getInstance("HmacSHA256")
        mac.init(SecretKeySpec(secretKey.toByteArray(charset("UTF-8")), "HmacSHA256"))

        return SensApigwNtrussComRequestApis.postSmsV2ServicesNaverSmsServiceIdMessages(
            SensApigwNtrussComRequestApis.PostService1TkV1RequestTestPostRequestApplicationJsonRequestPathParamVo(
                serviceId
            ),
            SensApigwNtrussComRequestApis.PostService1TkV1RequestTestPostRequestApplicationJsonRequestHeaderVo(
                time.toString(),
                accessKey,
                Base64.encodeBase64String(mac.doFinal(message.toByteArray(charset("UTF-8")))),
            ),
            SensApigwNtrussComRequestApis.PostService1TkV1RequestTestPostRequestApplicationJsonRequestBodyVo(
                "SMS",
                "COMM",
                inputVo.countryCode,
                phoneNumber,
                inputVo.content,
                listOf(
                    SensApigwNtrussComRequestApis.PostService1TkV1RequestTestPostRequestApplicationJsonRequestBodyVo.MessageVo(
                        inputVo.phoneNumber,
                        inputVo.content
                    )
                )
            )
        )
    }


    // ---------------------------------------------------------------------------------------------
    // <비공개 메소드 공간>


    // ---------------------------------------------------------------------------------------------
    // <중첩 클래스 공간>
    data class SendSmsInputVo(
        // 국가 코드 (ex : 82)
        val countryCode: String,
        // 전화번호 (ex : 01000000000)
        var phoneNumber: String,
        // 문자 본문
        var content: String
    )
}
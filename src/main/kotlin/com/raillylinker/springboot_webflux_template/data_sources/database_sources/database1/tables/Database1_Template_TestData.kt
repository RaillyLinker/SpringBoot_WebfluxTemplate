package com.raillylinker.springboot_webflux_template.data_sources.database_sources.database1.tables

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table(name = "test_data", schema = "template")
class Database1_Template_TestData(
    @Column("content")
    // 테스트 본문
    var content: String,

    @Column("random_num")
    // 테스트 랜덤 번호
    var randomNum: Int,

    @CreatedDate
    @Column("row_create_date")
    // 행 생성일
    var rowCreateDate: LocalDateTime,

    @LastModifiedDate
    @Column("row_update_date")
    // 행 수정일
    var rowUpdateDate: LocalDateTime,

    @Column("row_activate")
    // 행 활성 여부
    var rowActivate: Boolean
) {
    @Id
    @Column("uid")
    // 행 고유값
    var uid: Long? = null


    // ---------------------------------------------------------------------------------------------
    // <중첩 클래스 공간>


}
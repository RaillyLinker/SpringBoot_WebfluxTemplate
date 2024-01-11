package com.raillylinker.springboot_webflux_template.data_sources.database_sources.database1.repositories

import com.raillylinker.springboot_webflux_template.data_sources.database_sources.database1.tables.Database1_Template_TestData
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface Database1_Template_TestDataRepository : ReactiveCrudRepository<Database1_Template_TestData, Long> {
//    fun findAllByRowActivateOrderByRowCreateDate(
//        rowActivate: Boolean,
//        pageable: Pageable
//    ): Page<Database1_Template_TestData>
//
//    fun countByRowActivate(rowActivate: Boolean): Long

}
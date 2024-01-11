package com.raillylinker.springboot_webflux_template.configurations

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.http.HttpMethod
import org.springframework.web.reactive.config.*
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerResponse
import org.thymeleaf.spring6.ISpringWebFluxTemplateEngine
import org.thymeleaf.spring6.SpringWebFluxTemplateEngine
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver
import org.thymeleaf.spring6.view.reactive.ThymeleafReactiveViewResolver
import org.thymeleaf.templatemode.TemplateMode
import org.thymeleaf.templateresolver.ITemplateResolver

@Configuration
@EnableWebFlux
class WebConfig(
    // (Cors 설정)
    @Value("\${customConfig.corsList:}#{T(java.util.Collections).emptyList()}")
    private var corsList: List<String>
) : WebFluxConfigurer, ApplicationContextAware {
    private var context: ApplicationContext? = null

    override fun setApplicationContext(applicationContext: ApplicationContext) {
        this.context = applicationContext
    }

    // [Thymeleaf 설정]
    override fun configureViewResolvers(registry: ViewResolverRegistry) {
        registry.viewResolver(thymeleafReactiveViewResolver())
    }

    @Bean
    fun thymeleafReactiveViewResolver(): ThymeleafReactiveViewResolver {
        val viewResolver = ThymeleafReactiveViewResolver()
        viewResolver.templateEngine = thymeleafTemplateEngine()
        return viewResolver
    }

    @Bean
    fun thymeleafTemplateEngine(): ISpringWebFluxTemplateEngine {
        val templateEngine = SpringWebFluxTemplateEngine()
        templateEngine.setTemplateResolver(thymeleafTemplateResolver())
        return templateEngine
    }

    @Bean
    fun thymeleafTemplateResolver(): ITemplateResolver {
        val resolver = SpringResourceTemplateResolver()
        resolver.setApplicationContext(context!!)
        resolver.prefix = "classpath:templates/"
        resolver.suffix = ".html"
        resolver.templateMode = TemplateMode.HTML
        resolver.isCacheable = false
        resolver.checkExistence = false
        resolver.characterEncoding = "UTF-8"
        resolver.order = 1
        return resolver
    }

    @Bean
    fun staticResourceRouter(): RouterFunction<ServerResponse?>? {
        return RouterFunctions.resources("/**", ClassPathResource("static/"))
    }

    // [Cors 설정]
    override fun addCorsMappings(registry: CorsRegistry) {
        val allPathRegistry = registry.addMapping("/**") // 아래 설정을 적용할 요청 경로 (ex : "/somePath/**", "/path")
        if (corsList.isEmpty()) {
            allPathRegistry.allowedOriginPatterns("*") // 모든 요청을 허용하려면 allowedOrigins 를 지우고 이것을 사용
        } else {
            allPathRegistry.allowedOrigins(*corsList.toTypedArray()) // 자원 공유를 허용 할 URL 리스트
        }
        allPathRegistry.allowedMethods(
            HttpMethod.POST.name(), HttpMethod.GET.name(),
            HttpMethod.PUT.name(), HttpMethod.DELETE.name(),
            HttpMethod.OPTIONS.name()
        ) // 클라이언트에서 발신 가능한 메소드 (ex : "GET", "POST")
        allPathRegistry.allowedHeaders("*") // 클라이언트에서 발신 가능한 헤더 (ex : "name", "addr")
    }

}
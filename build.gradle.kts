import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.2.1"
    id("io.spring.dependency-management") version "1.1.4"
    kotlin("jvm") version "1.9.22"
    kotlin("plugin.spring") version "1.9.22"
}

group = "com.raillylinker"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

repositories {
    mavenCentral()
}

dependencies {
    // (기본)
    implementation("org.springframework.boot:spring-boot-starter:3.2.0")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.9.21")
    testImplementation("org.springframework.boot:spring-boot-starter-test:3.2.0")

    // (Webflux)
    implementation("org.springframework.boot:spring-boot-starter-webflux:3.2.1")

    // (ThymeLeaf)
    // : 웹 뷰 라이브러리
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf:3.2.0")

    // (Swagger)
    // : API 자동 문서화
    implementation("org.springdoc:springdoc-openapi-starter-webflux-ui:2.3.0")

    // (GSON)
    // : Json - Object 라이브러리
    implementation("com.google.code.gson:gson:2.10.1")

    // (Spring email)
    // : 스프링 이메일 발송
    implementation("org.springframework.boot:spring-boot-starter-mail:3.2.0")

    // (Spring Admin Client)
    // : Spring Actuator 포함
    implementation("de.codecentric:spring-boot-admin-starter-client:3.2.1")

    // (Apache Common Codec)
    implementation("commons-codec:commons-codec:1.16.0")

    // (Excel File Read Write)
    // : 액셀 파일 입출력 라이브러리
    implementation("org.apache.poi:poi:5.2.5")
    implementation("org.apache.poi:poi-ooxml:5.2.5")
    implementation("sax:sax:2.0.1")

	// (HTML 2 PDF)
	// : HTML -> PDF 변환 라이브러리
	implementation("org.xhtmlrenderer:flying-saucer-pdf:9.3.1")

    // (Kafka)
    implementation("org.springframework.kafka:spring-kafka:3.1.1")

    // (Database R2DBC)
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc:3.2.1")
    implementation("io.asyncer:r2dbc-mysql:1.0.6")

//	// (JWT)
//	// : JWT 인증 토큰 라이브러리
//	runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.3")
//	implementation("io.jsonwebtoken:jjwt-api:0.12.3")
//	runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.3")

//	// todo redis
//	// todo security
//
//	// (snakeyaml)
//	// : Gradle 종속성 에러 해결
//	implementation("org.yaml:snakeyaml:2.2")
//
//	// (Jackson)
//	implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.16.0")
//
//	// (SpringBoot AOP)
//	implementation("org.springframework.boot:spring-boot-starter-aop:3.2.0")
//
//	// (WebSocket)
//	// : 웹소켓
//	implementation("org.springframework.boot:spring-boot-starter-websocket:3.2.0")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "21"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

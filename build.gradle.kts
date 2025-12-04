plugins {
    kotlin("jvm") version "1.9.25" apply false
    kotlin("plugin.spring") version "1.9.25" apply false
    kotlin("plugin.jpa") version "1.9.25" apply false
    id("org.springframework.boot") version "3.5.8" apply false
    id("io.spring.dependency-management") version "1.1.7" apply false
}

// 모든 서브 모듈(common, core, batch, worker)에 공통 적용
subprojects {
    apply(plugin = "kotlin")
    apply(plugin = "kotlin-spring")
    apply(plugin = "kotlin-jpa") // core 때문에 필요 (나머지엔 영향 없음)
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")

    group = "com.settlement"
    version = "0.0.1-SNAPSHOT"

    repositories {
        mavenCentral()
    }

    dependencies {
        // 모든 모듈이 기본적으로 가지는 의존성
        "implementation"("org.jetbrains.kotlin:kotlin-reflect")
        "implementation"("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
        "implementation"("com.fasterxml.jackson.module:jackson-module-kotlin")
        "testImplementation"("org.springframework.boot:spring-boot-starter-test")
        "testImplementation"("org.jetbrains.kotlin:kotlin-test-junit5")
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs += "-Xjsr305=strict"
            jvmTarget = "21" // 사용하시는 자바 버전
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}

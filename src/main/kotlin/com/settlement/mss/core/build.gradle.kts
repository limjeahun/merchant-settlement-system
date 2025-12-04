dependencies {
    implementation(project(":common")) // common 모듈 가져다 쓰기

    // DB 관련 (JPA, MySQL)
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("com.mysql:mysql-connector-j")
}
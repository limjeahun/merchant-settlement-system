dependencies {
    implementation(project(":core")) // core (DB/도메인) 가져다 쓰기
    implementation(project(":common"))
    implementation("org.springframework.boot:spring-boot-starter-batch")
    implementation("org.springframework.kafka:spring-kafka") // Producer용
}
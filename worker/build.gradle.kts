dependencies {
    implementation(project(":core")) // core (DB/도메인) 가져다 쓰기
    implementation(project(":common"))

    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    implementation("org.springframework.boot:spring-boot-starter-web") // RestClient용
    implementation("org.springframework.kafka:spring-kafka") // Consumer용
    implementation("org.springframework.boot:spring-boot-starter-mail")
}
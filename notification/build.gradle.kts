plugins {
    kotlin("jvm") version "1.8.0"
    kotlin("plugin.spring") version "1.8.0"

}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.amqp:spring-rabbit")
    implementation("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    implementation("org.springframework.boot:spring-boot-starter-mail:3.1.3")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf:3.1.3")
    implementation("org.thymeleaf.extras:thymeleaf-extras-java8time:3.0.4.RELEASE")
    implementation("org.springframework.cloud:spring-cloud-starter-config")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}
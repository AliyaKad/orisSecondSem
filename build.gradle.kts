plugins {
    id("java")
    id("application")
    id("war")
    id("org.springframework.boot") version "3.3.0"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "ru.itis.inf304"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

application {
    mainClass.set("ru.itis.inf304.Application")
}

dependencies {
    // Spring Boot Starters
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-mail")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    runtimeOnly("org.postgresql:postgresql:42.7.5")

    implementation("com.sun.mail:javax.mail:1.6.2")

    implementation("com.fasterxml.jackson.core:jackson-databind:2.18.3")

    implementation("org.springframework.security:spring-security-taglibs:6.4.4")

    implementation("ch.qos.logback:logback-classic:1.4.11")
    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
}

tasks.test {
    useJUnitPlatform()
}
plugins {
    java
    id("org.springframework.boot") version "3.1.1"
    id("io.spring.dependency-management") version "1.1.0"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-log4j2")
    implementation("org.springframework.pulsar:spring-pulsar-reactive-spring-boot-starter:0.2.0")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
}
configurations {
    all {
        exclude(group = "org.springframework.boot", module = "spring-boot-starter-logging")
    }
}


tasks.bootRun {
    jvmArgs(
            "--add-opens", "java.base/sun.net=ALL-UNNAMED"
    )
}

tasks.withType<Test> {
    useJUnitPlatform()
}

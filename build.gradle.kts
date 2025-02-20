plugins {
    id("java")
    id("application")
}

group = "org.kafka"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation ("org.apache.kafka:kafka-clients:3.7.1")
    implementation ("log4j:log4j:1.2.17")

}


tasks.register("runProducer", JavaExec::class) {
    mainClass.set("org.kafka.Producer")
    classpath = sourceSets["main"].runtimeClasspath
}

tasks.register("runConsumer", JavaExec::class) {
    mainClass.set("org.kafka.Consumer")
    classpath = sourceSets["main"].runtimeClasspath
}


tasks.test {
    useJUnitPlatform()
}
plugins {
    id("java")
    id("application")
}

group = "fr.kafka"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation ("org.apache.kafka:kafka-clients:3.7.1")
    implementation ("org.slf4j:slf4j-simple:1.7.36")
    implementation ("org.apache.kafka:kafka-streams:3.4.0")

}


tasks.register("runProducer", JavaExec::class) {
    mainClass.set("fr.kafka.exo2.Producer")
    classpath = sourceSets["main"].runtimeClasspath
}

tasks.register("runConsumer", JavaExec::class) {
    mainClass.set("fr.kafka.exo2.Consumer")
    classpath = sourceSets["main"].runtimeClasspath
}

tasks.test {
    useJUnitPlatform()
}
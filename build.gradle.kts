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
    implementation("org.apache.kafka:kafka-clients:3.7.1")
    implementation("org.slf4j:slf4j-simple:1.7.36")
    implementation("org.apache.kafka:kafka-streams:3.4.0")
    implementation("org.apache.activemq:activemq-client:5.18.3")
}

// Lancé avec ./gradlew runProducer
tasks.register("runProducer", JavaExec::class) {
    mainClass.set("fr.kafka.exo2.Producer")
    classpath = sourceSets["main"].runtimeClasspath
}

// Lancé avec ./gradlew runConsumer
tasks.register("runConsumer", JavaExec::class) {
    mainClass.set("fr.kafka.exo2.Consumer")
    classpath = sourceSets["main"].runtimeClasspath
}

// Lancé avec ./gradlew runEx3Consumer
tasks.register("runEx3Consumer", JavaExec::class) {
    mainClass.set("fr.kafka.exo3.consommateur.TemperatureStreamProcessor")
    classpath = sourceSets["main"].runtimeClasspath
}

// Lancé avec ./gradlew runEx3Producteur
tasks.register("runEx3Producteur", JavaExec::class) {
    mainClass.set("fr.kafka.exo3.producteur.TemperatureThread")
    classpath = sourceSets["main"].runtimeClasspath
}

// Lancé avec ./gradlew runMain
tasks.register("runMain", JavaExec::class) {
    mainClass.set("fr.kafka.Main")
    classpath = sourceSets["main"].runtimeClasspath
}

// Lancé avec ./gradlew runJMS
tasks.register("runJMS", JavaExec::class) {
    mainClass.set("fr.kafka.exo2.JMS")
    classpath = sourceSets["main"].runtimeClasspath
}

tasks.test {
    useJUnitPlatform()
}
import org.gradle.api.tasks.testing.logging.TestExceptionFormat

plugins {
    kotlin("jvm") version "1.4.0"
    id("de.dplatz.clear") version "0.3"
    id("org.openjfx.javafxplugin") version "0.0.9"
}

javafx {
    modules("javafx.controls", "javafx.fxml")
}

val junitVersion: String = "5.6.2"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.openjfx:javafx:14.0.2.1")
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-params:$junitVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
        exceptionFormat = TestExceptionFormat.FULL
    }
}
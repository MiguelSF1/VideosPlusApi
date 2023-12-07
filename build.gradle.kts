plugins {
    id("java")
    application
}

application {
    mainClass = "org.example.Main"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("org.glassfish.jersey.core:jersey-server:3.1.3")
    implementation("org.glassfish.jersey.containers:jersey-container-servlet-core:3.1.3")
    implementation("org.glassfish.jersey.containers:jersey-container-jetty-http:3.1.3")
    implementation("org.glassfish.jersey.media:jersey-media-json-jackson:3.1.3")
    implementation("org.glassfish.jersey.inject:jersey-hk2:3.1.3")
    implementation("jakarta.ws.rs:jakarta.ws.rs-api:3.1.0")
    implementation("org.glassfish.jersey.media:jersey-media-multipart:3.1.3")
    implementation("org.slf4j:slf4j-simple:2.0.9")
    implementation("org.springframework.security:spring-security-crypto:5.5.8")
    implementation("org.glassfish.jaxb:jaxb-runtime:3.0.2")
    implementation("org.eclipse.jetty:jetty-server:11.0.18")
    implementation("org.eclipse.jetty:jetty-servlets:11.0.18")
    compileOnly("jakarta.servlet:jakarta.servlet-api:5.0.0")
    implementation ("org.mariadb.jdbc:mariadb-java-client:2.1.2")
}

tasks.test {
    useJUnitPlatform()
}

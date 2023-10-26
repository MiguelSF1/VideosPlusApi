plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("org.glassfish.jersey.core:jersey-server:2.27")
    implementation("org.glassfish.jersey.containers:jersey-container-servlet-core:2.27")
    implementation("org.glassfish.jersey.containers:jersey-container-jetty-http:2.27")
    implementation("org.glassfish.jersey.media:jersey-media-json-jackson:2.27")
    implementation("org.glassfish.jersey.inject:jersey-hk2:2.27")
    implementation("org.slf4j:slf4j-api:1.7.25")
    testImplementation("org.slf4j:slf4j-simple:1.7.25")
    implementation("org.eclipse.jetty:jetty-server:9.4.6.v20170531")
    implementation("org.eclipse.jetty:jetty-servlet:9.4.6.v20170531")
    implementation("javax.ws.rs:javax.ws.rs-api:2.1.1")
    implementation ("org.mariadb.jdbc:mariadb-java-client:2.1.2")
}

tasks.test {
    useJUnitPlatform()
}
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	java
	`maven-publish`	
	kotlin("jvm") version "1.8.20"
	kotlin("plugin.allopen") version "1.8.20"
	kotlin("plugin.noarg") version "1.8.20"
}

allOpen {
	annotation("javax.persistence.Entity")
	annotation("javax.persistence.MappedSuperclass")
	annotation("javax.persistence.Embeddable")
}

noArg {
	annotation("javax.persistence.Entity")
	annotation("javax.persistence.MappedSuperclass")
	annotation("javax.persistence.Embeddable")
	annotation("com.github.nayasis.kotlin.basica.annotation.NoArg")
	invokeInitializers = true
}

group = "com.github.nayasis"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

configurations.all {
	resolutionStrategy.cacheChangingModulesFor(0, "seconds")
	resolutionStrategy.cacheDynamicVersionsFor(5, "minutes")
}

java {
	registerFeature("support") {
		usingSourceSet(sourceSets["main"])
	}
	withJavadocJar()
	withSourcesJar()
}

repositories {
	mavenLocal()
	mavenCentral()
	jcenter()
	maven { url = uri("https://jitpack.io") }
}

dependencies {

	implementation("com.github.nayasis:basica-kt:0.2.22")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.0-Beta")
	implementation("io.github.microutils:kotlin-logging:3.0.5")
	implementation("au.com.console:kassava:2.1.0")
	compileOnly("com.oracle.database.jdbc:ojdbc6:11.2.0.4")

	testImplementation("io.kotest:kotest-runner-junit5:5.6.1")
	testImplementation("io.kotest:kotest-assertions-core:5.6.1")
	testImplementation("com.h2database:h2:2.1.214")

	testImplementation("ch.qos.logback:logback-classic:1.4.6")

}

tasks.withType<Test>().configureEach {
	useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf(
			"-Xjsr305=strict"
		)
		jvmTarget = "1.8"
	}
}

publishing {
	publications {
		create<MavenPublication>("maven") {
			from(components["java"])
		}
	}
}
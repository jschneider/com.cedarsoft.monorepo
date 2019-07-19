import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

description = """Gradle Deployment test project. Project name: ${project.name}"""

buildscript {
  repositories {
    jcenter()
    maven { url = uri("https://dl.bintray.com/kotlin/kotlin-eap") }
  }

  dependencies {
    classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.41")
    classpath("org.jetbrains.kotlin:kotlin-serialization:1.3.41")
    classpath("org.jetbrains.kotlin:kotlin-frontend-plugin:0.0.45")
  }
}

group = "com.cedarsoft.tests"
version = "0.0.2-SNAPSHOT"

repositories {
  mavenCentral()
  jcenter()

  maven {
    url = uri("https://oss.sonatype.org/content/repositories/releases/")
    name = "OSS Sonatype Releases"
  }

  maven {
    url = uri("https://oss.sonatype.org/content/repositories/snapshots/")
    name = "OSS Sonatype Snapshots"
  }

  maven {
    url = uri("http://dl.bintray.com/kotlin/kotlinx")
    name = "Bintray KotlinX"
  }

  maven {
    url = uri("https://dl.bintray.com/kotlin/kotlin-eap")
  }
}


plugins {
  base
  idea
  java
  `java-library`
  kotlin("jvm") version "1.3.41"
  id("org.jetbrains.dokka") version "0.9.18"
  id("io.codearte.nexus-staging") version "0.21.0"

  `maven-publish`
  signing

  id("com.github.ben-manes.versions") version "0.21.0"
  id("com.github.johnrengelman.shadow") version "5.1.0"
  //id("fr.brouillard.oss.gradle.jgitver") version "0.5.0"
}

dependencies {
  api(kotlin("stdlib-jdk8"))
  api(kotlin("reflect"))

  testImplementation("org.junit.jupiter:junit-jupiter-api:5.4.0")
  testImplementation("org.junit.jupiter:junit-jupiter-params:5.4.0")
  testImplementation("org.junit.jupiter:junit-jupiter-engine:5.4.0")
  //testImplementation("org.junit.vintage:junit-vintage-engine:5.4.0")
}


//Kotlin compiler configuration
tasks.withType<KotlinCompile> {
  kotlinOptions.languageVersion = "1.3"
  kotlinOptions.jvmTarget = "1.8"
  kotlinOptions.allWarningsAsErrors = false
  kotlinOptions.verbose = true
  kotlinOptions.javaParameters = true
}

//Java compiler options
tasks.withType<JavaCompile>()
  .configureEach {
    options.compilerArgs = listOf("-Xlint:all", "-Xlint:-serial", "-parameters")
    options.encoding = "UTF-8"
  }

//Use JUnit
tasks.withType<Test>()
  .configureEach {
    useJUnitPlatform() {
      includeEngines("junit-jupiter", "junit-vintage")
    }

    filter {
      includeTestsMatching("*Test")
      isFailOnNoMatchingTests = false
    }
  }

tasks {
  dokka {
    outputFormat = "javadoc"
    outputDirectory = "$buildDir/javadoc"
  }
}

configure<JavaPluginExtension> {
  sourceCompatibility = JavaVersion.VERSION_1_8
  targetCompatibility = JavaVersion.VERSION_1_8
}

idea {
  //Add target dir to exclude dirs
  module {
    val targetDir = file("target")
    this.excludeDirs = mutableSetOf(targetDir).apply {
      addAll(excludeDirs)
    }
  }
}

tasks.register<Jar>("sourcesJar") {
  from(sourceSets.main.get().allSource)
  archiveClassifier.set("sources")
}

tasks.register<Jar>("javadocJar") {
  from(tasks.javadoc)
  archiveClassifier.set("javadoc")
}

publishing {
  repositories {
    maven {
      name = if (isSnapshot) "SonatypeOsssnapshots" else "SonatypeOssSstaging"
      url = if (isSnapshot)
        uri("https://oss.sonatype.org/content/repositories/snapshots/") else
        uri("https://oss.sonatype.org/service/local/staging/deploy/maven2/")

      credentials {
        username = project.findProperty("ossUsername") as? String
        password = project.findProperty("oddPassword") as? String
      }
    }
  }

  publications {
    create<MavenPublication>("maven") {
      groupId = "com.cedarsoft.tests"
      artifactId = "gradle-deployment"

      from(components["java"])

      artifact(tasks["sourcesJar"])
      artifact(tasks["javadocJar"])

      repositories {

      }

      pom {
        name.set("My Name")
        description.set("A concise description of my library")
        url.set("http://cedarsoft.com")

        licenses {
          license {
            name.set("The Apache License, Version 2.0")
            url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
          }
        }
        developers {
          developer {
            id.set("jschneider")
            name.set("Johannes Schneider")
            email.set("js@cedarsoft.com")
          }
        }

        scm {
          connection.set("scm:git:git://example.com/my-library.git")
          developerConnection.set("scm:git:ssh://example.com/my-library.git")
          url.set("http://example.com/my-library/")
        }
      }
    }
  }
}

signing {
  useGpgCmd()

  //Only sign if *not* snapshot
  if (!isSnapshot) {
    sign(publishing.publications["maven"])
  }
  isRequired = !isSnapshot
}

nexusStaging {
  username = project.findProperty("ossUsername") as? String
  password = project.findProperty("oddPassword") as? String
  packageGroup = "com.cedarsoft"
  stagingProfileId = "3755bb868da9a4"
}

inline val Project.isSnapshot: Boolean
  get() = version.toString().endsWith("-SNAPSHOT")

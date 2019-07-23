import org.jetbrains.dokka.gradle.DokkaTask

allprojects {
  group = "com.cedarsoft.open"
}

subprojects {
  //Relevant for publishing to Maven Central
  apply(plugin = "maven-publish")
  apply(plugin = "signing")

  //Sources jar and javadoc jar for deployment to Maven Central
  (extensions.findByName("sourceSets") as SourceSetContainer?)?.let {
    tasks.register<Jar>("sourcesJar") {
      from(it.named<SourceSet>("main").get().allSource)
      archiveClassifier.set("sources")
    }

    tasks.register<Jar>("javadocJar") {
      from(tasks.named<DokkaTask>("dokka"))
      archiveClassifier.set("javadoc")
    }
  }

  extensions.configure<PublishingExtension>("publishing") {
    repositories {
      maven {
        name = if (isSnapshot) "SonatypeOsssnapshots" else "SonatypeOssSstaging"
        url = if (isSnapshot) {
          uri("https://oss.sonatype.org/content/repositories/snapshots/")
        } else {
          uri("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
        }

        credentials {
          //Environment variable: ORG_GRADLE_PROJECT_MAVEN_REPO_USER
          username = project.findProperty("MAVEN_REPO_USER") as? String
          //Environment variable: ORG_GRADLE_PROJECT_MAVEN_REPO_PASS
          password = project.findProperty("MAVEN_REPO_PASS") as? String
        }
      }
    }

    publications {
      create<MavenPublication>("maven") {
        from(components["java"])

        artifact(tasks["sourcesJar"])
        artifact(tasks["javadocJar"])

        repositories {
        }

        pom {
          name.set("${project.name}")
          description.set("Path: ${project.path}")
          url.set("http://cedarsoft.com")

          licenses {
            license {
              name.set("GPLv3 with Classpath Exception")
              url.set("https://www.cedarsoft.org/gpl3ce")
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
            connection.set("scm:git:git@github.com:jschneider/com.cedarsoft.monorepo.git")
            developerConnection.set("scm:git:git@github.com:jschneider/com.cedarsoft.monorepo.git")
            url.set("https://github.com/jschneider/com.cedarsoft.monorepo")
          }

          versionMapping {
            usage("java-api") {
              fromResolutionOf("runtimeClasspath")
            }
            usage("java-runtime") {
              fromResolutionResult()
            }
          }
        }
      }
    }
  }

  //Signing must be called after publication
  extensions.configure<SigningExtension>("signing") {
    useGpgCmd()
    //Only sign if *not* snapshot
    if (!isSnapshot) {
      sign(extensions.getByName<PublishingExtension>("publishing").publications["maven"])
    }
    isRequired = !isSnapshot
  }

}

inline val Project.isSnapshot: Boolean
  get() = version.toString().endsWith("-SNAPSHOT")

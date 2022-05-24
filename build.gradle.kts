allprojects {
  group = "com.cedarsoft.open"
}

subprojects {
  if (isIntermediate) {
    return@subprojects
  }

  if (true) {
    //Relevant for publishing to Maven Central
    apply(plugin = "maven-publish")
    apply(plugin = "signing")

    extensions.configure<PublishingExtension>("publishing") {
      configureMavenReposForPublish(project)

      if (path == ":internal:open:unit:unit") {
        //No configuration for unit necessary.
      } else {
        publications {
          create<MavenPublication>("maven") {
            val javaComponent = components.findByName("java")

            if (javaComponent != null) {
              from(javaComponent)

              artifact(tasks["sourcesJar"])
              artifact(tasks["javadocJar"])
            } else {
              from(components["kotlin"])
            }

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
                  fromResolutionResult()
                }
                usage("java-runtime") {
                  fromResolutionResult()
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
    }
  }
}

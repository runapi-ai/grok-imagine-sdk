plugins {
  `java-library`
  `maven-publish`
}

description = "RunAPI Grok Imagine Java SDK for Grok Imagine workflows."

java {
  withSourcesJar()
  withJavadocJar()
}

dependencies {
  api("ai.runapi:runapi-core:0.1.0")

  testImplementation(platform("org.junit:junit-bom:5.10.3"))
  testImplementation("org.junit.jupiter:junit-jupiter")
}

publishing {
  publications {
    create<MavenPublication>("mavenJava") {
      from(components["java"])
      artifactId = "runapi-grok-imagine"
      pom {
        name = "RunAPI Grok Imagine Java SDK"
        description = "RunAPI Grok Imagine Java SDK for Grok Imagine workflows."
        url = "https://runapi.ai/models/grok-imagine"
        licenses {
          license {
            name = "Apache License, Version 2.0"
            url = "https://www.apache.org/licenses/LICENSE-2.0"
          }
        }
        developers {
          developer {
            id = "runapi"
            name = "RunAPI"
            email = "contact@runapi.ai"
          }
        }
        scm {
          url = "https://github.com/runapi-ai/grok-imagine-sdk"
          connection = "scm:git:https://github.com/runapi-ai/grok-imagine-sdk.git"
          developerConnection = "scm:git:ssh://git@github.com/runapi-ai/grok-imagine-sdk.git"
        }
      }
    }
  }
}

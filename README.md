# Reactive debugger agent

This is a java agent that allows you to collect 
information about data flowing through a reactive stream.

# Using the agent in your projects

The library is published to the GitHub packages repository.

## From Gradle

1. Create a personal access token at least with the
   `read:packages` scope
   (see [here](https://docs.github.com/en/packages/learn-github-packages/about-github-packages#authenticating-to-github-packages) 
   for details) as the credential, suppose that your username 
   is `<GITHUB_USERNAME>`, and the token is `<GITHUB_TOKEN>`.

2. Choose one of the following ways to store the credential:

- Create a `github.properties` file under the root folder of your project (and add `github.properties` in `.gitignore`), 
  with the following content:
```
gpr.user=<GITHUB_USERNAME>
gpr.token=<GITHUB_TOKEN>
```
- Config system environment with command lines, e.g. for Linux/macOS:

```
export GITHUB_USERNAME=<GITHUB_USERNAME>
export GITHUB_TOKEN=<GITHUB_TOKEN>
```

3. Add the Github Packages Registry with authentication in `build.gradle`:

```groovy
def githubProperties = new Properties()
githubProperties.load(new FileInputStream(rootProject.file("github.properties")))
repositories {
    mavenCentral()

    maven {
        name = "GitHubPackages"
        url = uri("https://maven.pkg.github.com/alexeslikov/reactive-debugger-agent")
        credentials {
            username = githubProperties["gpr.user"] ?: System.getenv("GITHUB_USERNAME")
            password = githubProperties["gpr.key"] ?: System.getenv("GITHUB_TOKEN")
        }
    }
}
```
4. Add the dependency to gumtree modules as needed in `build.gradle`, e.g.:

```groovy
dependencies {
    implementation 'org.jetbrains.intellij.deps:reactive-debugger-agent:0.0.1'
}
```

## From Maven

1. Create a personal access token at least with the `read:packages` 
   scope (see [here](https://docs.github.com/en/packages/learn-github-packages/about-github-packages#authenticating-to-github-packages) for details) 
   as the credential, suppose that your username
   is `<GITHUB_USERNAME>`, and the token is `<GITHUB_TOKEN>`.

2. Add Github Packages Registry with authentication
   in `~/.m2/settings.xml`, or create one if it doesn't exist:

```xml
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
                      http://maven.apache.org/xsd/settings-1.0.0.xsd">

    <activeProfiles>
        <activeProfile>github</activeProfile>
    </activeProfiles>

    <profiles>
        <profile>
            <id>github</id>
            <repositories>
                <repository>
                    <id>central</id>
                    <url>https://repo1.maven.org/maven2</url>
                </repository>
                <repository>
                    <id>github</id>
                    <url>https://maven.pkg.github.com/alexeslikov/reactive-debugger-agent</url>
                    <snapshots>
                        <enabled>true</enabled>
                    </snapshots>
                </repository>
            </repositories>
        </profile>
    </profiles>

    <servers>
        <server>
            <id>github</id>
            <username><GITHUB_USERNAME></username>
            <password><GITHUB_TOKEN></password>
        </server>
    </servers>
</settings>
```

3. Add the dependency to agent as needed in `pom.xml`, e.g.:

```xml
<dependency>
    <groupId>org.jetbrains.intellij.deps</groupId>
    <artifactId>reactive-debugger-agent</artifactId>
    <version>0.0.1</version>
</dependency>
```


## Attaching the agent to a JVM

To attach agent simply specify run a JVM with the parameter:

`-agentpath:/path/to/agent/library`.


You may also install an agent on the currently 
running Java virtual machine and re-process existing classes, using:

```java
    ReactiveDebuggerAgent.INSTANCE.init();
    ReactiveDebuggerAgent.INSTANCE.processExistingClasses();
```

## Limitations

`ReactiveDebuggerAgent` is implemented as a Java Agent and
uses
[ByteBuddy](https://bytebuddy.net/#/)
to perform the self-attach. Self-attach may not work on some
JVMs, please refer to ByteBuddy's documentation for more
details.

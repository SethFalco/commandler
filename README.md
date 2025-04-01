# Commandler [![](https://img.shields.io/maven-central/v/org.elypia.commandler/core)](https://search.maven.org/search?q=g:org.elypia.commandler) [![](https://gitlab.com/SethFalco/commandler/badges/main/pipeline.svg)](https://gitlab.com/SethFalco/commandler)

## About

Commandler (**Comm**nd H**andler**) is a command handling framework for Java. It takes a lot of inspiration from Spring, so if you're coming from there it should be immediately familiar, and uses the same standards to provide dependency injection, validation, and cron jobs, etc.

## Getting Started

Commandler is split into multiple packages to give you control over what logic and dependencies you have in your application(s).

It's recommended to use the `newb` package to start with. This is an opinionated package that provides sensible runtime dependencies and default implementations. When or if your requirements become more complex, you can always replace `newb` with the `core` package to choose your own CDI or validation implementations.

### Import

Visit [Commandler on Maven Central](https://search.maven.org/search?q=g:org.elypia.commandler), and follow the instructions for your build system of choice to add Commandler to your project.

### Your First Command

This example assumes you depend on the following packages:

* `newb` - an opinionated distribution of Commandler with sensible defaults
* `console` - command-line interface support for Commandler

You must define a `beans.xml` file for the CDI API to find your Java beans. You can just copy and paste this to start with!

**`main/resources/META-INF/beans.xml`**
```xml
<beans xmlns="http://xmlns.jcp.org/xml/ns/javaee" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/beans_2_0.xsd"
       bean-discovery-mode="all" version="2.0">
  <scan></scan>
</beans>
```

**`main/java/org/example/bot/Main.java`**
```java
public class Main {

    /** Create the Commandler instance and runs all integrations. */
    public static void main(String[] args) {
        Commandler commandler = Commandler.create();
        commandler.run();
    }
}
```

To implement your first command, you must create a class which by convention will be named with the `Controller` suffix, for example `DebugController`. A controller contains a series of related commands for the chatbot.

It must be annotated with a controller annotation, such as `@StandardController`. This indicates to Commandler and the CDI container that it's a controller.

**`main/java/org/example/bot/DebugController.java`**
```java
@StandardController("debug")
public class DebugController {
    
    @StandardCommand("ping")
    public String ping() {
        return "pong!";
    }
}
```

You've now made your first command! If you run your application, you'll be able to invoke this command in the command-line interface. By default, all commands are done by specifying a prefix, the controller's alias, then the command's alias. In this case:

* Default prefix is `$`, Commandler ships with this value.
* Controller's alias is `debug`.
* Command's alias is `ping`.

```
$debug ping
pong!
```

However, for some commands like `ping`, having to mention the controller's alias might feel burdensome. For common or simple commands, you can define a command as "static". This makes it possible to use the command without specifying the controller's alias.

**`main/java/org/example/bot/DebugController.java`**
```diff
  @StandardController("debug")
  public class DebugController {
    
-     @StandardCommand("ping")
+     @StandardCommand(value = "ping", isStatic = true)
      public String ping() {
          return "pong!";
      }
  }
```

Now it's possible to do the commands through both of the following ways, and both yield the same result:

* `$debug ping`
* `$ping`

### Internationalization

Commandler also provides i18n (internationalization) support out of the box for controller names, command names, and parameter names. These are to populate your help pages and documentation across locales. It's also just a lot cleaner than inlining copy into your annotations.

**`main/resources/org/example/project/i18n/CommandlerMessages.properties`**
```properties
org.example.project.DebugController.name=Debug
org.example.project.DebugController.description=Tools for the developer.
org.example.project.DebugController-ping.name=ping!
org.example.project.DebugController-ping.description=Check if the chatbot is responsive.
```

**`main/resources/org/example/project/i18n/CommandlerMessages_nl.properties`**
```properties
org.example.project.DebugController.name=Debug
org.example.project.DebugController.description=Hulpmiddelen voor developers.
org.example.project.DebugController-ping.name=ping!
org.example.project.DebugController-ping.description=Test of de chatbot responsief is.
```

By creating these resource bundles, the help command now has guidance for the user. If you configure or provide an implementation for the [`LocaleResolver`](https://deltaspike.apache.org/javadoc/1.8.0/org/apache/deltaspike/core/api/message/class-use/LocaleResolver.html) (defaults to the system locale), you can change which language to serve between invocations of the command.

## Why use Commandler?

Commandler uses standard APIs, and the knowledge is transferable to other Java frameworks. In other words, a chatbot can be a simple use case to get accustomed to the APIs and concepts that you'll use for commercial applications.

The controllers and commands you write are portable, so long as you don't tie them down to a vendor yourself in your implementation. To add support for a new platform, you can register enough `Integration` and the command will _just work_ ™.

You can use CommandlerDoc to export all guidance for your controllers and commands, which can be used to generate a better documentation than a help command. 

## Tips

### CDI Java Beans

To use Commandler you must define `beans.xml` in your classpath, even if you don't configure it. However, it's often helpful to populate the `<scan>` tag with exclusions.

The `<exclude>` tag is used to tell the CDI container to skip processing certain package paths in the archive it's present in. If you build your project with a plugin like [shadow](https://github.com/GradleUp/shadow), you'll have a single standalone archive that contains _everything_ in your classpath in a single archive.

That's a lot to scan! In large projects, it can have a minor impact on start times of your application, and make your CDI container aware of Java beans that aren't relevant to your application.

If this becomes a problem, add the relevant package name/path to your `beans.xml` file:

```xml
<beans xmlns="http://xmlns.jcp.org/xml/ns/javaee" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/beans_2_0.xsd"
       bean-discovery-mode="all" version="2.0">
  <scan>
    <exclude name="org.example.**"/>
  </scan>
</beans>
```

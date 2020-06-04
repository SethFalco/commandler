# Commandler [![Matrix]][matrix-community] [![Discord]][discord-guild] [![Maven Central]][maven-page] [![Docs]][documentation] [![Build]][gitlab] [![Coverage]][gitlab] [![Donate]][elypia-donate]
The [Gradle]/[Maven] import string can be found at the maven-central badge above!

## About
Command Handler
Commandler (**Comm**nd H**andler**) is a command handling framework for Java.
It's purpose is to try keep as much of the reusable code as possible into a core
project which can be the heart of any chatbot.  
You'll be able to get started quickly making clean, reliable, and reusable code 
with the framework which will help manage dependency injection, validation, and
even generate a website with all relevent documentation for your commands.

### Why use Commandler?
Commandler makes it a lot easier to get started making anything that requires command
handling, namely chatbots. It's designed using standard enterprise Java APIs
like CDI and DeltaSpike to give a modern Java framework feel so should feel
familiar to users of Java/Jakarta EE or Spring.

* The controllers and commands you write are portable, so long as they aren't tied
down to any API specific functionality you can just plop them over to another project,
or register another `Integration` and see the command port over to another service.
* Commandler under the hood is using familiar APIs such as CDI, 
Hibernate, and DeltaSpike, which should massively reduce the learning curve,
or if these things are new to you, give you enterprise knowledge through a smaller framework.
* Using `Commandlerdoc` you can export your controllers/command as it's own website
which may offer users a better experience when reading your chatbots documentation. 

## Getting Started
Commandler is split into several small modules in order give very fine control
over what logic and dependencies you have in your application(s).

It's recommended to use `org.elypia.commandler:newb` to get started; this pulls 
together the recommended modules and libraries so you can jump straight into
development without worrying too much about the runtime dependencies or configuration.  
This uses Weld, Hibernate, and provides YAML support with an `application.yml` by default.

When or if your requirements grow and you might want to change the CDI or validation
implementation then you can go back to `org.elypia.commandler:core` and depend
on whatever libraries or runtimes you want.

### Example Code
For this example we're going to assume that you're depending on the 
`org.elypia.commandler:newb` and `org.elypia.commandler:console` modules.
The `console` module will add support for performing commands in command line which
is a great way to gain familiarity with the API and test things out quickly.

**./META-INF/beans.xml**
```xml
<beans xmlns="http://xmlns.jcp.org/xml/ns/javaee" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/beans_2_0.xsd"
       bean-discovery-mode="all" version="2.0">
</beans>
```
> You need to have a beans.xml file for the CDI API to find your Java beans. 
> You can just copy and paste this at first!

**Main.java**
```java
public class Main {

    /**
    * This creates the Commandler instance and then runs all integrations.
    * 
    * Get more information on Commandler here: 
    * https://gitlab.com/Elypia/commandler
    */
    public static void main(String[] args) {
        Commandler commandler = Commandler.create();
        commandler.run();
    }
}
```

**UtilityController.java**
```java
@StandardController("utils")
public class UtilityController implements Controller {
    
    @StandardCommand("boop")
    public String boop() {
        return "boop!";
    }
}
```
> Here we're creating our first controller and command. By default, it'll activate 
> whenever we type `$utils boop` in console and return the text `boop!`.  
> Commandler also provides i18n (internationalization) support out of the box, 
> you can omit hardcoding documentation or aliases in the annotations and instead opt
> to put them in a `CommandlerMessages` resource bundle instead which will use the
> configured `LocaleResolver` implementation (defaults to system locale) to get localized
> strings per event.

## Open-Source
This project is licenced under the Apache 2.0 project, don't be afraid to
derive or reference from this project all you want!

**All non-code files including videos, models, audio, bitmaps, vectors, and 
animations such as gifs, are not under the aforementioned license; all rights
are reserved by Elypia CIC.**

## Support
Should any problems occur, come visit us over on [Discord][discord-guild]! We're always around and
there are ample developers that would be willing to help; if it's a problem with
the library itself then we'll make sure to get it sorted.

[matrix-community]: https://matrix.to/#/+elypia:matrix.org "Matrix Invite"
[discord-guild]: https://discord.com/invite/hprGMaM "Discord Invite"
[maven-page]: https://search.maven.org/search?q=g:org.elypia.commandler "Maven Central"
[documentation]: https://elypia.gitlab.io/commandler "Commandler Documentation"
[gitlab]: https://gitlab.com/Elypia/commandler/commits/master "Repository on GitLab"
[elypia-donate]: https://elypia.org/donate "Donate to Elypia"
[Gradle]: https://gradle.org/ "Depend via Gradle"
[Maven]: https://maven.apache.org/ "Depend via Maven"
[elypia]: https://elypia.org/ "Elypia Homepage"

[Matrix]: https://img.shields.io/matrix/elypia-general:matrix.org?logo=matrix "Matrix Shield"
[Discord]: https://discord.com/api/guilds/184657525990359041/widget.png "Discord Shield"
[Maven Central]: https://img.shields.io/maven-central/v/org.elypia.commandler/core "Download Shield"
[Docs]: https://img.shields.io/badge/docs-commandler-blue.svg "Commandler Documentation Shield"
[Build]: https://gitlab.com/Elypia/commandler/badges/master/pipeline.svg "GitLab Build Shield"
[Coverage]: https://gitlab.com/Elypia/commandler/badges/master/coverage.svg "GitLab Coverage Shield"
[Donate]: https://img.shields.io/badge/donate-elypia-blueviolet "Donate Shield"

## Commandler
[![Bintray Download](https://api.bintray.com/packages/elypia/Commandler/Commandler/images/download.svg)](https://bintray.com/elypia/Commandler/Commandler/_latestVersion)
[![Documentation](https://img.shields.io/badge/Docs-Commandler-blue.svg)](https://commandler.elypia.com/)
[![GitLab Pipeline Status](https://gitlab.com/Elypia/Commandler/badges/master/pipeline.svg)](https://gitlab.com/Elypia/Commandler/commits/master)
[![GitLab Coverage Report](https://gitlab.com/Elypia/Commandler/badges/master/coverage.svg)](https://gitlab.com/Elypia/Commandler/commits/master)
[![Donate](https://img.shields.io/badge/Donate-PayPal-yellow.svg)](https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=WLA5BMC34SJVG)
[![Discord](https://discordapp.com/api/guilds/184657525990359041/widget.png)](https://discord.gg/hprGMaM)

### Importing
#### [Gradle](https://gradle.org/)
```gradle
implementation 'com.elypia:commandler:{VERSION}'
```

#### [Maven](https://maven.apache.org/)
```xml
<dependency>
  <groupId>com.elypia</groupId>
  <artifactId>commandler</artifactId>
  <version>{VERSION}</version>
</dependency>
```

### About
Commandler is a command handling framework for [Java Discord API (JDA)](https://github.com/DV8FromTheWorld/JDA) designed for Discord bots to have functional, reliable and flexible parsing and validation under the hood with a [Spring Boot](https://github.com/spring-projects/spring-boot) like syntax. This ensures _you_, the developer, can focus on making the functionality you want with a unified command handler already set up to allow you to manage things like permissions, types of channels and the scope to search for Discord entities.

### QuickStart
Start by creating your application and JDA instance as you normally would. Refer to the JDA quickstart if you're also new to using JDA. Once you have logged into your JDA client (bot), create an instance of Commandler.
```java
public class CommandlerBot {

    public static void main(String[] args) throws LoginException {
        JDA jda = new JDABuilder(AccountType.BOT).setToken(args[0]).buildAsync();
        Commandler commandler = new Commandler(jda);

        commandler.registerModules(new ExampleModule());
    }
}
```

Now that your application launches and the bot can login, create a module. By default commands are formatted as:  
`{prefix}{module} {command} {params...}`  
This can be changed by specifying a `Confiler` (config) object by calling `new Commandler(JDA, Confiler);`.
```java
@Module(name = "Example Module for Demo", aliases = {"example", "ex"}, description = "This module is made for demonstration and examples.")
public class ExampleModule extends CommandHandler {

    @Static
    @Command(aliases = "ping", help = "pong")
    public String ping() {
        return "pong!";
    }
}
```

Now when the bot reads `!ex ping` - it will respond with "pong!", but since we have specified that the command is `@Static` it will also return "pong!" if `!ping` is executed also, it still belongs to the `Example` module.

### Support
Should any problems occur, come visit us over on [Discord](https://discord.gg/hprGMaM)! We're always around and there are ample developers that would be willing to help; if it's a problem with the library itself then we'll make sure to get it sorted.

This project is _heavily_ relied on by [Alexis, the Discord bot](https://discordapp.com/oauth2/authorize?client_id=230716794212581376&scope=bot). Feel free to check her out or join our guild so you can see Elypiai in action.

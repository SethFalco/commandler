# Commandler [![Discord](https://discordapp.com/api/guilds/184657525990359041/widget.png)](https://discord.gg/hprGMaM) [![Documentation](https://img.shields.io/badge/Docs-Commandler-blue.svg)](https://commandler.elypia.com/) [![GitLab Pipeline Status](https://gitlab.com/Elypia/Commandler/badges/master/pipeline.svg)](https://gitlab.com/Elypia/Commandler/commits/master) [![Codacy Badge](https://api.codacy.com/project/badge/Grade/4756f0a78c104040b93c8df85cd9f9ff)](https://www.codacy.com/app/Elypia/Commandler?utm_source=gitlab.com&amp;utm_medium=referral&amp;utm_content=Elypia/Commandler&amp;utm_campaign=Badge_Grade) [![Codacy Badge](https://api.codacy.com/project/badge/Coverage/4756f0a78c104040b93c8df85cd9f9ff)](https://www.codacy.com/app/Elypia/Commandler?utm_source=gitlab.com&utm_medium=referral&utm_content=Elypia/Commandler&utm_campaign=Badge_Coverage)

## Importing
### [Gradle](https://gradle.org/)
```gradle
implementation 'com.elypia.commandler:{ARTIFACT}:{VERSION}'
```

### [Maven](https://maven.apache.org/)
```xml
<dependency>
  <groupId>com.elypia.commandler</groupId>
  <artifactId>{ARTIFACT}</artifactId>
  <version>{VERSION}</version>
</dependency>
```

## **Artifacts**
| Artifact      | Description                                                                                        |
|---------------|----------------------------------------------------------------------------------------------------|
| `core`        | The core of commandler, ready to implement with any service.                                       |
| `file-config` | An extention for Commandler to allow configuring modules though files.                             |
| `doc`         | The documentation tool to generate a static website based on existing modules.                     |                                                              |
| `json`        | The data export tool, this will expore all groups, modules, and commands as JSON for external use. |
## About
Commandler is a command handling framework for Java designed to have functional, reliable and flexible parsing and validation under the hood with a like syntax. This ensures _you_, the developer, can focus on making the functionality you want with a unified command misuse already set up to allow you to manage things like permissions.  
Commandler is abstract and should be implemented with the desired service or API before use. This makes it's easy to work with between multiple different APIs.


## Support
Should any problems occur, come visit us over on [Discord](https://discord.gg/hprGMaM)! We're always around and there are ample developers that would be willing to help; if it's a problem with the library itself then we'll make sure to get it sorted.

This project is _heavily_ relied on by [Alexis, the Discord bot](https://discordapp.com/oauth2/authorize?client_id=230716794212581376&scope=bot). Feel free to check her out or join our guild so you can see Commandler in action.

# Commandler [![Discord][discord-members]][discord] [![Download][bintray-download]][bintray] [![Documentation][docs-shield]][docs] [![GitLab Pipeline Status][gitlab-build]][gitlab] [![Coverage][gitlab-coverage]][gitlab] 
The [Gradle][gradle]/[Maven][maven] import string can be found at the Download badge above!

**Declaration: This library is a work in progress and is not considered stable _YET_ outside of internal use.**

## Artifacts
| Artifact      | Description                                                                                              |
|---------------|----------------------------------------------------------------------------------------------------------|
| `core`        | The core of Commandler, ready to implement with any service or library.                                  |
| `file-config` | An extension for Commandler to allow configuring modules though files using [NightConfig][night-config]. |
| `doc`         | The documentation tool to generate a static website based on existing modules.                           |
| `json`        | The data export tool, this will export all groups, modules, and commands as JSON for external use.       |

## About
Commandler's a command handling framework for Java, it's purpose to to push as much of the reusable code and patterns
that's put to making a command handler into a core so that applications requiring it such as chat bots or command
processors are able to stick to just creating the functionality they are intended for without having to worry about 
parameter parsing, validation, building responses; as well as makes an abstract core to start a command handler or 
chat bot for any service, or even multiple services in a single application.

Commandler is abstract and should be implemented with the desired service or API before use. This makes it's easy to
work with between multiple different APIs.

## Support
Should any problems occur, come visit us over on [Discord][discord]! We're always around and there are
ample developers that would be willing to help; if it's a problem with the library itself then we'll
make sure to get it sorted.

[discord]: https://discord.gg/hprGMaM "Discord Invite"
[discord-members]: https://discordapp.com/api/guilds/184657525990359041/widget.png "Discord Shield"
[bintray]: https://bintray.com/elypia/Commandler/core/_latestVersion "Bintray Latest Version"
[bintray-download]: https://api.bintray.com/packages/elypia/Commandler/core/images/download.svg "Bintray Download Shield"
[docs]: https://commandler.elypia.com/ "Commandler Documentation"
[docs-shield]: https://img.shields.io/badge/Docs-Commandler-blue.svg "Commandler Documentation Shield"
[gitlab]: https://gitlab.com/Elypia/commandler/commits/master "Repository on GitLab"
[gitlab-build]: https://gitlab.com/Elypia/commandler/badges/master/pipeline.svg "GitLab Build Shield"
[gitlab-coverage]: https://gitlab.com/Elypia/commandler/badges/master/coverage.svg "GitLab Coverage Shield"

[gradle]: https://gradle.org/ "Depend via Gradle"
[maven]: https://maven.apache.org/ "Depend via Maven"

[elypia]: https://elypia.com/ "Elypia Homepage"
[night-config]: https://github.com/TheElectronWill/Night-Config "GitHub Repo for Night-Config"

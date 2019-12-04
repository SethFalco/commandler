# Commandler [![discord-members]][Discord] [![bintray-download]][bintray] [![docs-shield]][docs] [![gitlab-build]][gitlab] [![gitlab-coverage]][gitlab] [![donate-shield]][elypia-donate]
The [Gradle]/[Maven] import string can be found at the Download badge above!

## About
Commandler is a command handling framework for Java. It's purpose is to try keep as much
of the reusable code as possible into a core project which can be the heart of any chatbot.  
You'll be able to get started quickly making clean, reliable, and reusable code 
with the framework which will help manage dependency injection, validation, and
even generate a website with all relevent documentation you define!

### Why use Commandler?
Commandler makes it a lot easier to get started making anything that requires command
handling, namely chatbots. It's designed not to be too bloaty or take control
away from you while putting together a stack of libraries and flow to work with.

* The controllers and commands you write are portable, so long as they aren't tied
down to any API specific functionality you can just plop them over to another project,
or register another `Integration` and see the command port over to another service.
* Commandler under the hood is using familiar APIs such as Apache Commons, 
Hibernate, and Google Guice, which should massively reduce the learning curve.
* Using `Commandlerdoc` you can export your controllers/command as it's own website
which may offer users a better experience when reading your chatbots documentation. 

## Open-Source
This project is licenced under the Apache 2.0 project, don't be afraid to
derive or reference from this project all you want!

**All non-code files including videos, models, audio, bitmaps, vectors, and 
animations such as gifs, are not under the aforementioned license; all rights
are reserved by Elypia CIC.**

## Support
Should any problems occur, come visit us over on [Discord]! We're always around and
there are ample developers that would be willing to help; if it's a problem with
the library itself then we'll make sure to get it sorted.

[Discord]: https://discord.gg/hprGMaM "Discord Invite"
[bintray]: https://bintray.com/elypia/Commandler/core/_latestVersion "Bintray Latest Version"
[docs]: https://elypia.gitlab.io/commandler "Commandler Documentation"
[gitlab]: https://gitlab.com/Elypia/commandler/commits/master "Repository on GitLab"
[elypia-donate]: https://elypia.org/donate "Donate to Elypia"
[Gradle]: https://gradle.org/ "Depend via Gradle"
[Maven]: https://maven.apache.org/ "Depend via Maven"
[elypia]: https://elypia.org/ "Elypia Homepage"

[discord-members]: https://discordapp.com/api/guilds/184657525990359041/widget.png "Discord Shield"
[bintray-download]: https://api.bintray.com/packages/elypia/Commandler/core/images/download.svg "Bintray Download Shield"
[docs-shield]: https://img.shields.io/badge/Docs-Commandler-blue.svg "Commandler Documentation Shield"
[gitlab-build]: https://gitlab.com/Elypia/commandler/badges/master/pipeline.svg "GitLab Build Shield"
[gitlab-coverage]: https://gitlab.com/Elypia/commandler/badges/master/coverage.svg "GitLab Coverage Shield"
[donate-shield]: https://img.shields.io/badge/Elypia-Donate-blueviolet "Donate Shield"

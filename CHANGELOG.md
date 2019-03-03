# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased](https://gitlab.com/Elypia/Commandler/tree/dev)
### Changes
- Pulled `PageBuilder` out and created a subproject for called `Commandlerdoc`, specifically for generating the static webpage.
- Removed `com.elypia.commandler.jda`, as that will now be a seperate in a seperate repository: `com.elypia.jdacommandler`.
- Removed `com.elypia.commandler.console`, as we probably shouldn't create a whole mock module for testing when we can instantiate that in the tests themselves.
- Now supports additional types of data loaders than just `@Annotations`.
- Now provides a test runner implementation to test modules during runtime.
- Loads from a package from a class by reference, rather than a package string.
- Created own exceptions for more relevent exception names and messages.

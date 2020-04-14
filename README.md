# octopus-deploy-plugin (gradle plugin)
![GitHub](https://img.shields.io/github/license/Liftric/octopus-deploy-plugin)
![GitHub release (latest by date)](https://img.shields.io/github/v/release/Liftric/octopus-deploy-plugin)
[![CircleCI](https://circleci.com/gh/Liftric/octopus-deploy-plugin/tree/master.svg?style=svg)](https://circleci.com/gh/Liftric/octopus-deploy-plugin/tree/master)

Requirements:
 * octopus cli
 * git (for commits calculation)
 
# Usage

## naming
Octopus deploy expects the name and version in the following format: `<name>.<version>.<extension>`

For Java archives, `<name>-<version>.<extension>` is conventional, so should be changed to get picked up properly by octopus.
Otherwise the first version number part will be parsed as part of the name.

## configuration

## buildSrc

# gradle-allure-plugin
[![Download](https://api.bintray.com/packages/d10xa/maven/ru.d10xa%3Agradle-allure-plugin/images/download.svg)](https://bintray.com/d10xa/maven/ru.d10xa%3Agradle-allure-plugin/_latestVersion)
[![Build Status](https://travis-ci.org/d10xa/gradle-allure-plugin.svg?branch=master)](https://travis-ci.org/d10xa/gradle-allure-plugin)

A Gradle plugin that create [Allure](http://allure.qatools.ru/) report for spock tests.

## Usage

```groovy
buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath "ru.d10xa:gradle-allure-plugin:0.2.2"
    }
}

apply plugin: 'ru.d10xa.allure'

allure {
    geb = true // optional
}
```

## Configuration

- `geb` (boolean) default false.
Adds [allure-spock-geb](https://github.com/d10xa/allure-spock-geb)
dependency for screenshot and html attachments.
Specifications must extend geb.spock.GebReportingSpec class

## Tasks

### `allureReport`

Creates html report for your tests.

Add following snippet to build script if you want to create allure report after every test execution

```groovy
tasks.withType(Test)*.finalizedBy allureReport
```

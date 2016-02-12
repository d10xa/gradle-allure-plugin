# gradle-allure-plugin
[![Download](https://api.bintray.com/packages/d10xa/maven/ru.d10xa%3Agradle-allure-plugin/images/download.svg)](https://bintray.com/d10xa/maven/ru.d10xa%3Agradle-allure-plugin/_latestVersion)
[![Build Status](https://travis-ci.org/d10xa/gradle-allure-plugin.svg?branch=master)](https://travis-ci.org/d10xa/gradle-allure-plugin)

A Gradle plugin that create [Allure](http://allure.qatools.ru/) report for spock tests.

## Usage

```groovy
buildscript {
    repositories {
        jcenter()
        maven { url 'https://dl.bintray.com/d10xa/maven' }
    }
    dependencies {
        classpath "ru.d10xa:gradle-allure-plugin:0.2.0"
    }
}

apply plugin: 'ru.d10xa.allure'

allure {
    geb = true // optional
}
```

## Tasks

### `allureReport`

Creates html report for your tests.

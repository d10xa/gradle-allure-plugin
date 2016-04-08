# Gradle Allure Plugin
[![Download](https://api.bintray.com/packages/d10xa/maven/ru.d10xa%3Agradle-allure-plugin/images/download.svg)](https://bintray.com/d10xa/maven/ru.d10xa%3Agradle-allure-plugin/_latestVersion)
[![Build Status](https://travis-ci.org/d10xa/gradle-allure-plugin.svg?branch=master)](https://travis-ci.org/d10xa/gradle-allure-plugin)
[![jitpack](https://jitpack.io/v/ru.d10xa/gradle-allure-plugin.svg)](https://jitpack.io/#ru.d10xa/gradle-allure-plugin)

Gradle Allure Plugin allows you to integrate 
[Allure](http://allure.qatools.ru/) into spock, testng and junit tests.

[Examples](https://github.com/d10xa/gradle-allure-plugin-examples)

## Usage

```groovy
buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath "ru.d10xa:gradle-allure-plugin:0.5.0"
    }
}

apply plugin: 'ru.d10xa.allure'

allure {
    aspectjweaver = true
    
    junit = true
    testNG = false
    spock = false
    
    geb = false
    
    allureResultsDir = "$buildDir/allure-results"
    allureReportDir = "$buildDir/allure-report"
    
    allureVersion = "1.4.22"
    aspectjVersion = "1.8.9"
}
```

## Configuration

- `aspectjweaver` (boolean) default false.
Adds `-javaagent` to tests

- `junit` (boolean) default false.
Enables allure for junit without @RunWith usage. 
(Adds [allure-junit-aspectj-adaptor](https://github.com/d10xa/allure-junit-aspectj-adaptor)) based on AspectJ. 
Require `aspectjweaver`

- `testNG` (boolean) default false.
Enables allure for testNG. 
Require `aspectjweaver`

- `spock` (boolean) default false.
Enables allure for spock. 

- `geb` (boolean) default false.
Adds [allure-spock-geb](https://github.com/d10xa/allure-spock-geb)
dependency for screenshot and html attachments.
Specifications must extend geb.spock.GebReportingSpec class

- `allureResultsDir` (string) default "$buildDir/allure-results".
Test results will be placed to this directory. 

- `allureReportDir` (string) default "$buildDir/allure-report".
Html report will be generated to this directory 
(if task's property `to` not defined) 

- `allureVersion` (string). 
Version of allure-bundle and allure-adaptors

- `aspectjVersion` (string).
Version of `org.aspectj:aspectjweaver`

- `configuration` (string) default 'testCompile'.
Apply adaptors to this configuration. 
For example, you can select the configuration 'compile' 
and use the adaptor classes in your source code.

## Tasks

### `allureReport`

Creates html report for tests.

Add following snippet to build script if you want to create allure report after every test execution

```groovy
tasks.withType(Test)*.finalizedBy allureReport
```

If you don't need this task(for example in child modules) - just delete it
```groovy
tasks.remove allureReport
```

Customize task's parameters
```groovy
allureReport {
    from(
            "${project(':moduleA').buildDir}/allure-results",
            "${project(':moduleB').buildDir}/my-allure-results",
    )
    to '$buildDir/nice-report'
}
```

Or create your own task
```groovy
task customAllureReport(type: ru.d10xa.allure.AllureReportTask){
}
```

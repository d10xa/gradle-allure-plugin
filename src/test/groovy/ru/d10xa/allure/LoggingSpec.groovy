package ru.d10xa.allure

import groovy.transform.Memoized
import org.gradle.testkit.runner.GradleRunner
import ru.d10xa.allure.extension.GradlePluginClasspath
import ru.d10xa.allure.extension.TestProjectDir
import spock.lang.Shared
import spock.lang.Specification

class LoggingSpec extends Specification {

    @TestProjectDir(dir = "logging")
    @Shared
    File testProjectDirectory

    @GradlePluginClasspath
    @Shared
    List<File> pluginClasspath

    def setupSpec() {
        new File(testProjectDirectory, "build/allure-results").mkdirs()
    }

    @Memoized
    def getBuildResult() {
        GradleRunner.create()
                .withProjectDir(testProjectDirectory)
                .withArguments('allureReport', '--debug')
                .withPluginClasspath(pluginClasspath)
                .buildAndFail()
    }

    def 'nonexistent directory logging'() {
        when:
        def output = buildResult.output

        then:
        output.contains('logging/build/nonexistent-folder is not directory')
    }

    def 'empty directory logging'() {
        when:
        def output = buildResult.output

        then:
        output.contains('logging/build/allure-results is empty directory')
    }

}

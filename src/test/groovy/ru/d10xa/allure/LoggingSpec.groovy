package ru.d10xa.allure

import groovy.transform.Memoized
import org.gradle.testkit.runner.GradleRunner
import ru.d10xa.allure.extension.GradlePluginClasspath
import ru.d10xa.allure.extension.TestProjectDir
import spock.lang.Shared
import spock.lang.Specification

import static org.gradle.testkit.runner.TaskOutcome.SUCCESS

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
                .withTestKitDir(new File(testProjectDirectory.parentFile.absolutePath, '.gradle'))
                .withArguments('allureReport', '--debug')
                .withPluginClasspath(pluginClasspath)
                .build()
    }

    def 'build not fails without allure results'() {
        expect:
        buildResult.task(":allureReport").outcome == SUCCESS
        buildResult.output.contains(
                "No allure results found. The report is not generated")
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

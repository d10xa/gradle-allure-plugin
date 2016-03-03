package ru.d10xa.allure

import org.gradle.testkit.runner.GradleRunner
import ru.d10xa.allure.extension.GradlePluginClasspath
import ru.d10xa.allure.extension.TestProjectDir
import spock.lang.Shared
import spock.lang.Specification

import static org.gradle.testkit.runner.TaskOutcome.SUCCESS

class MultiModuleSpec extends Specification {

    @TestProjectDir(dir = "aggregate-multi-module")
    @Shared
    File testProjectDirectory

    @GradlePluginClasspath
    List<File> pluginClasspath

    def 'create report for modules'() {
        when:
        def result = GradleRunner.create()
                .withProjectDir(testProjectDirectory)
                .withArguments("allureReport", "--stacktrace")
                .withPluginClasspath(pluginClasspath)
                .build()

        then:
        result.task(":allureReport").outcome == SUCCESS
    }

}

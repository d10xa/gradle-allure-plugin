package ru.d10xa.allure

import groovy.transform.Memoized
import org.gradle.testkit.runner.GradleRunner
import ru.d10xa.allure.extension.GradlePluginClasspath
import ru.d10xa.allure.extension.TestProjectDir
import spock.lang.Shared
import spock.lang.Specification

import static org.gradle.testkit.runner.TaskOutcome.SUCCESS

class AllureCustomTestTaskSpec extends Specification {

    @TestProjectDir(dir = "report-custom-test-task")
    @Shared
    File testProjectDirectory

    @GradlePluginClasspath
    List<File> pluginClasspath

    @Memoized
    def getBuildResult() {
        GradleRunner.create()
                .withProjectDir(testProjectDirectory)
                .withArguments("mytest")
                .withPluginClasspath(pluginClasspath)
                .build()
    }

    def 'test task declared after plugin applying'() {
        expect:
        buildResult.task(":mytest").outcome == SUCCESS
    }

}

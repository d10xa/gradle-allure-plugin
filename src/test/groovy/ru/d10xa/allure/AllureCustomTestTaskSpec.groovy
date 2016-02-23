package ru.d10xa.allure

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

    def 'create test task after apply plugin'() {
        when:
        def result = GradleRunner.create()
                .withProjectDir(testProjectDirectory)
                .withArguments("mytest")
                .withPluginClasspath(pluginClasspath)
                .build()

        then:
        result.task(":mytest").outcome == SUCCESS
    }

}

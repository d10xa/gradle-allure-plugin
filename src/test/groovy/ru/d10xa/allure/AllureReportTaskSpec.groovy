package ru.d10xa.allure

import org.gradle.testkit.runner.GradleRunner
import ru.d10xa.allure.extension.GradlePluginClasspath
import ru.d10xa.allure.extension.TestProjectDir
import spock.lang.Shared
import spock.lang.Specification

import static org.gradle.testkit.runner.TaskOutcome.SUCCESS

class AllureReportTaskSpec extends Specification {

    @TestProjectDir(dir = "allure-report-task")
    @Shared
    File testProjectDirectory

    @GradlePluginClasspath
    List<File> pluginClasspath

    def 'results and report directory exists'() {
        when:
        def result = GradleRunner.create()
                .withProjectDir(testProjectDirectory)
                .withArguments('test', 'allureReport')
                .withPluginClasspath(pluginClasspath)
                .build()

        then:
        result.output.contains('test executed')
        result.task(":test").outcome == SUCCESS
        new File(testProjectDirectory, "build/allure-results").list().size() == 1
        new File(testProjectDirectory, "build/allure-report/index.html").exists()
    }

}

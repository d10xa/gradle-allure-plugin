package ru.d10xa.allure

import groovy.transform.Memoized
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
    @Shared
    List<File> pluginClasspath

    @Memoized
    def getBuildResult() {
        GradleRunner.create()
                .withProjectDir(testProjectDirectory)
                .withTestKitDir(new File(testProjectDirectory.parentFile.absolutePath, '.gradle'))
                .withArguments('test', 'allureReport')
                .withPluginClasspath(pluginClasspath)
                .build()
    }

    def 'task successfully executed'() {
        expect:
        buildResult.task(":test").outcome == SUCCESS
        buildResult.output.contains('test executed')
    }

    def 'results directory contain single test suite'() {
        when:
        def testSuites = new File(testProjectDirectory, "build/allure-results")
                .list()
                .findAll { it.endsWith "-testsuite.xml" }

        then:
        testSuites.size() == 1
    }

    def 'report exists'() {
        when:
        def indexHtml = new File(testProjectDirectory, "build/allure-report/index.html")
        def testCases = new File(testProjectDirectory, "build/allure-report/data")
                .list()
                .findAll { it.endsWith "-testcase.json" }

        then:
        indexHtml.exists()
        testCases.size() == 1
    }

}

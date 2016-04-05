package ru.d10xa.allure

import groovy.transform.Memoized
import org.gradle.testkit.runner.GradleRunner
import ru.d10xa.allure.extension.GradlePluginClasspath
import ru.d10xa.allure.extension.TestProjectDir
import spock.lang.Shared
import spock.lang.Specification

import static org.gradle.testkit.runner.TaskOutcome.SUCCESS

class JunitSpec extends Specification {

    @TestProjectDir(dir = "junit")
    @Shared
    File testProjectDirectory

    @GradlePluginClasspath
    @Shared
    List<File> pluginClasspath

    @Memoized
    def getBuildResult() {
        GradleRunner.create()
                .withProjectDir(testProjectDirectory)
                .withArguments('test', 'allureReport')
                .withPluginClasspath(pluginClasspath)
                .build()
    }

    def 'task successfully executed'() {
        expect:
        buildResult.task(":test").outcome == SUCCESS
    }

    def 'html attachment found in report data'() {
        when:
        int attachmentsCount = new File(testProjectDirectory, 'build/allure-report/data')
                .list()
                .findAll { it.endsWith '-attachment.html' }
                .size()

        then:
        attachmentsCount == 1
    }

}

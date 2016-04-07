package ru.d10xa.allure

import groovy.transform.Memoized
import org.gradle.testkit.runner.GradleRunner
import ru.d10xa.allure.extension.GradlePluginClasspath
import ru.d10xa.allure.extension.TestProjectDir
import spock.lang.Shared
import spock.lang.Specification

import static org.gradle.testkit.runner.TaskOutcome.SUCCESS

class AllureGebDependencySpec extends Specification {

    @TestProjectDir(dir = "allure-geb-extension")
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
                .withArguments('test', 'customAllureReport')
                .withPluginClasspath(pluginClasspath)
                .build()
    }

    def 'success execution'() {
        expect:
        buildResult.task(":test").outcome == SUCCESS
    }

    def 'html attachment found in results'() {
        when:
        String html = new File(testProjectDirectory, 'build/allure-results')
                .listFiles()
                .find { it.name.endsWith '-attachment.html' }
                .text

        then:
        html.contains 'Geb - Very Groovy Browser Automation'
    }

    def 'html attachment found in report data'() {
        when:
        int attachmentsCount = new File(testProjectDirectory, 'build/my-allure-report/data')
                .list()
                .findAll { it.endsWith '-attachment.html' }
                .size()

        then:
        attachmentsCount == 1
    }

}

package ru.d10xa.allure

import org.gradle.testkit.runner.GradleRunner
import ru.d10xa.allure.extension.GradlePluginClasspath
import ru.d10xa.allure.extension.TestProjectDir
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import static org.gradle.testkit.runner.TaskOutcome.SUCCESS

@Unroll
class JunitSpec extends Specification {

    @TestProjectDir(dir = "junit", clean = true)
    File testProjectDirectory

    @GradlePluginClasspath
    @Shared
    List<File> pluginClasspath

    @Unroll
    def 'html attachment found in report data with gradle verison #gradleVersion'() {
        when:
        def buildResult = GradleRunner.create()
                .withProjectDir(testProjectDirectory)
                .withTestKitDir(new File(testProjectDirectory.parentFile.absolutePath, '.gradle'))
                .withArguments('test', 'allureReport')
                .withGradleVersion(gradleVersion)
                .withPluginClasspath(pluginClasspath)
                .build()

        int attachmentsCount = new File(testProjectDirectory, 'build/allure-report/data')
                .list()
                .findAll { it.endsWith '-attachment.html' }
                .size()

        then:
        buildResult.task(":test").outcome == SUCCESS
        attachmentsCount == 1

        where:
        gradleVersion << ['2.8', '2.9', '2.10', '2.12', '2.13', '2.14']
    }

}

package ru.d10xa.allure

import org.gradle.testkit.runner.GradleRunner
import ru.d10xa.allure.extension.GradlePluginClasspath
import ru.d10xa.allure.extension.TestProjectDir
import spock.lang.Shared
import spock.lang.Specification

class CleanSpec extends Specification {

    @TestProjectDir(dir = "clean-tasks")
    @Shared
    File testProjectDirectory

    @GradlePluginClasspath
    @Shared
    List<File> pluginClasspath

    def 'cleanAllureReport task'() {
        given:
        reportDir.mkdirs()

        when:
        GradleRunner.create()
                .withProjectDir(testProjectDirectory)
                .withTestKitDir(new File(testProjectDirectory.parentFile.absolutePath, '.gradle'))
                .withArguments('cleanAllureReport')
                .withPluginClasspath(pluginClasspath)
                .build()

        then:
        !reportDir.exists()
    }

    def 'cleanTest task'() {
        given:
        resultsDir.mkdirs()

        when:
        GradleRunner.create()
                .withProjectDir(testProjectDirectory)
                .withTestKitDir(new File(testProjectDirectory.parentFile.absolutePath, '.gradle'))
                .withArguments('cleanTest')
                .withPluginClasspath(pluginClasspath)
                .build()

        then:
        !resultsDir.exists()
    }

    private File getReportDir() {
        new File(testProjectDirectory, "build/allure-report-custom")
    }

    private File getResultsDir() {
        new File(testProjectDirectory, "build/allure-results-custom")
    }

}

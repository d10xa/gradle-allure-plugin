package ru.d10xa.allure

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
    List<File> pluginClasspath

    def 'results and report directory exists'() {
        when:
        def result = GradleRunner.create()
                .withProjectDir(testProjectDirectory)
                .withArguments('test')
                .withPluginClasspath(pluginClasspath)
                .build()

        then:
        result.task(":test").outcome == SUCCESS
    }

}

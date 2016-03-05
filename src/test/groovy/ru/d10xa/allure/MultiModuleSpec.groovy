package ru.d10xa.allure

import org.gradle.testkit.runner.GradleRunner
import ru.d10xa.allure.extension.GradlePluginClasspath
import ru.d10xa.allure.extension.TestProjectDir
import spock.lang.Shared
import spock.lang.Specification

import static org.gradle.testkit.runner.TaskOutcome.SUCCESS

class MultiModuleSpec extends Specification {

    @TestProjectDir(dir = "multi-module")
    @Shared
    File testProjectDirectory

    @GradlePluginClasspath
    List<File> pluginClasspath

    def setupSpec() {
        File.metaClass.containsOnly = { String... dirs ->
            if (!delegate.directory) {
                return false
            }
            if (delegate.listFiles().size() != dirs.size()) {
                return false
            }
            if (!delegate.list().every { dirs.contains(it) }) {
                return false
            }
            return true
        }
    }

    def 'create report for modules'() {
        when:
        def result = GradleRunner.create()
                .withProjectDir(testProjectDirectory)
                .withArguments("allureReport", "--stacktrace")
                .withPluginClasspath(pluginClasspath)
                .build()

        then:

        result.task(":allureReport").outcome == SUCCESS

        file "build" containsOnly 'allure-report'
        file "moduleA/build" containsOnly 'allure-results'
        file "moduleB/build" containsOnly 'my-allure-results'

        file('build/allure-report/data')
                .list()
                .findAll { it.endsWith '-testcase.json' }.size() == 2

    }

    private File file(String path) {
        new File(testProjectDirectory, path)
    }

}

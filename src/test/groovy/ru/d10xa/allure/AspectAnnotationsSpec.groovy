package ru.d10xa.allure

import groovy.transform.Memoized
import org.gradle.testkit.runner.GradleRunner
import ru.d10xa.allure.extension.GradlePluginClasspath
import ru.d10xa.allure.extension.TestProjectDir
import spock.lang.Shared
import spock.lang.Specification

import static org.gradle.testkit.runner.TaskOutcome.SUCCESS

class AspectAnnotationsSpec extends Specification {

    @TestProjectDir(dir = "aspect-annotations")
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
                .withArguments('test')
                .withPluginClasspath(pluginClasspath)
                .build()
    }

    def 'task successfully executed'() {
        expect:
        buildResult.task(":test").outcome == SUCCESS
    }

    def 'results directory contain single attachment'() {
        when:
        File file = new File(testProjectDirectory, "build/allure-results")
                .listFiles()
                .findAll { it.name.endsWith "-attachment.txt" }
                .head()
        then:
        file.text == "content"
    }

}

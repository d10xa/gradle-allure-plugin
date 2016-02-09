package ru.d10xa.allure

import org.gradle.testkit.runner.GradleRunner
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification
import static org.gradle.testkit.runner.TaskOutcome.*

class AllurePluginSpec extends Specification {

    @Rule
    TemporaryFolder testProjectDir = new TemporaryFolder()

    @Rule
    GradlePluginClasspath gradlePluginClasspath = new GradlePluginClasspath()

    private File buildFile
    private File testFile

    def setup() {
        buildFile = testProjectDir.newFile("build.gradle");
        testProjectDir.newFolder("src", "test", "groovy").mkdirs()
        testFile = testProjectDir.newFile("src/test/groovy/Spec.groovy")
    }

    def 'test'() {
        given:
        buildFile << """
            plugins {
                id 'groovy'
                id 'ru.d10xa.allure'
            }
            repositories {
                jcenter()
            }
            dependencies {
                compile 'org.codehaus.groovy:groovy-all:2.4.5'
                testCompile 'org.spockframework:spock-core:1.0-groovy-2.4'
            }
            test {
                testLogging {
                    showStandardStreams = true
                }
            }
        """

        testFile << """
            import spock.lang.Specification

            public class Spec extends Specification {

                def 'spec'() {
                    println("test executed")

                    expect:
                    true
                }

            }
        """

        when:
        def result = GradleRunner.create()
                .withProjectDir(testProjectDir.root)
                .withArguments('test', 'allureReport')
                .withPluginClasspath(gradlePluginClasspath.get())
                .build()

        then:
        result.output.contains('test executed')
        result.task(":test").outcome == SUCCESS
        new File(testProjectDir.root, "build/allure-results").list().size() == 1
        new File(testProjectDir.root, "build/allure-report/index.html").exists()
    }

}

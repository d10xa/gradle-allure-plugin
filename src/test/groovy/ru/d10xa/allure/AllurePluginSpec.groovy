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
        testProjectDir.newFolder("src", "test", "java").mkdirs()
        testFile = testProjectDir.newFile("src/test/java/Test.java")
    }

    def 'test'() {
        given:
        buildFile << """
            plugins {
                id 'java'
                id 'ru.d10xa.allure'
            }
            repositories {
                jcenter()
            }
            dependencies {
                testCompile 'junit:junit:4.12'
            }
            test {
                testLogging {
                    showStandardStreams = true
                }
            }
            task helloWorld {
                doLast {
                    println 'Hello world!'
                }
            }
        """

        testFile << """
            public class Test {
                @org.junit.Test
                public void test() {
                    System.out.println("test executed");
                    org.junit.Assert.assertTrue(true);
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
        result.output.contains('allure!')
        result.task(":test").outcome == SUCCESS
    }
}

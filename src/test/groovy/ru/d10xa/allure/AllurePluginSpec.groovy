package ru.d10xa.allure

import org.gradle.testkit.runner.GradleRunner
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification
import static org.gradle.testkit.runner.TaskOutcome.*

class AllurePluginSpec extends Specification {

    @Rule
    public final TemporaryFolder testProjectDir = new TemporaryFolder()

    List<File> pluginClasspath

    private File buildFile
    private File testFile

    def setup() {
        buildFile = testProjectDir.newFile("build.gradle");
        testProjectDir.newFolder("src", "test", "java").mkdirs()
        testFile = testProjectDir.newFile("src/test/java/Test.java")

        def pluginClasspathResource = getClass().classLoader.findResource("plugin-classpath.txt")
        if (pluginClasspathResource == null) {
            throw new IllegalStateException("Did not find plugin classpath resource, run `testClasses` build task.")
        }

        pluginClasspath = pluginClasspathResource.readLines().collect { new File(it) }
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
                .withArguments('test','allureReport')
                .withPluginClasspath(pluginClasspath)
                .build()

        then:
        result.output.contains('test executed')
        result.output.contains('allure!')
        result.task(":test").outcome == SUCCESS
    }
}

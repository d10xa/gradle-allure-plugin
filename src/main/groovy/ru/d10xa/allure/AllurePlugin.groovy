package ru.d10xa.allure

import groovy.transform.CompileStatic
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import org.gradle.api.tasks.testing.Test

@CompileStatic
public class AllurePlugin implements Plugin<Project> {

    private static final
    String ALLURE_RESULTS_DIRECTORY_SYSTEM_PROPERTY = "allure.results.directory"

    private final static String CONFIGURATION_ASPECTJWEAVER = "aspectjweaverAgent"

    @Override
    public void apply(Project project) {
        project.configurations.create(AllureReportTask.CONFIGURATION_NAME)
        AllureExtension ext = configureAllureExtension(project)
        project.afterEvaluate {
            applyAllureSpockAdaptor(project, ext)
            applyTestNgAdaptor(project, ext)
            applyAllureJunitAdaptor(project, ext)

            applyAspectjweaver(project, ext)
            applyGeb(project, ext)
            configureTestTasks(project, ext)
            configureBundle(project, ext)
        }
        project.tasks.create(AllureReportTask.NAME, AllureReportTask)
    }

    private static AllureExtension configureAllureExtension(Project project) {
        project.extensions.create(AllureExtension.NAME, AllureExtension.class, project)
    }

    private static void applyAllureSpockAdaptor(Project project, AllureExtension ext) {
        if (ext.spock) {
            project.dependencies.add(
                    ext.configuration,
                    "ru.yandex.qatools.allure:allure-spock-1.0-adaptor:1.0")
        }
    }

    private static void applyAllureJunitAdaptor(Project project, AllureExtension ext) {
        if (ext.junit) {
            addD10xaRepository(project)
            project.dependencies.add(
                    ext.configuration,
                    "ru.d10xa:allure-junit-aspectj-adaptor:${ext.allureJunitAspectjAdaptorVersion}")
            project.dependencies.add(
                    ext.configuration,
                    "ru.yandex.qatools.allure:allure-junit-adaptor:${ext.allureVersion}")
        }
    }

    private static void applyAspectjweaver(Project project, AllureExtension ext) {
        if (ext.aspectjweaver) {
            def aspectjConfiguration = project.configurations.maybeCreate(
                    CONFIGURATION_ASPECTJWEAVER)

            project.dependencies.add(CONFIGURATION_ASPECTJWEAVER,
                    "org.aspectj:aspectjweaver:${ext.aspectjVersion}")

            project.tasks.withType(Test).each { test ->
                test.doFirst {
                    String javaAgent = "-javaagent:${aspectjConfiguration.singleFile}"
                    test.jvmArgs = [javaAgent] + test.jvmArgs
                }
                if (project.logger.debugEnabled) {
                    project.logger.debug "jvmArgs for task $test.name $test.jvmArgs"
                }
            }
        }
    }

    private static void applyGeb(Project project, AllureExtension ext) {
        if (ext.geb) {
            addD10xaRepository(project)
            project.dependencies.add(ext.configuration,
                    "ru.d10xa:allure-spock-geb:$ext.allureSpockGebExtensionVersion")
        }
    }

    private static void applyTestNgAdaptor(Project project, AllureExtension ext) {
        if (ext.testNG) {
            project.dependencies.add(ext.configuration,
                    "ru.yandex.qatools.allure:allure-testng-adaptor:$ext.allureVersion")
        }
    }

    private static void addD10xaRepository(Project project) {
        project.repositories.maven { MavenArtifactRepository repo ->
            repo.setUrl("https://dl.bintray.com/d10xa/maven")
        }
    }

    private static void configureTestTasks(Project project, AllureExtension ext) {
        project.tasks.withType(Test.class).each {
            it.outputs.files project.files(project.file(ext.allureResultsDir))
            it.systemProperty(ALLURE_RESULTS_DIRECTORY_SYSTEM_PROPERTY, ext.allureResultsDir)
        }
    }

    private static void configureBundle(Project project, AllureExtension ext) {
        project.dependencies.add(
                AllureReportTask.CONFIGURATION_NAME,
                "ru.yandex.qatools.allure:allure-bundle:$ext.allureVersion")
    }

}

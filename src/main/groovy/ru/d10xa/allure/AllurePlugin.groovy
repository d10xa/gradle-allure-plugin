package ru.d10xa.allure

import groovy.transform.CompileStatic
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.PluginCollection
import org.gradle.api.tasks.testing.Test

@CompileStatic
public class AllurePlugin implements Plugin<Project> {

    private static final String ALLURE_RESULTS_DIRECTORY_SYSTEM_PROPERTY = "allure.results.directory"

    private static final String CONFIGURATION_TEST_COMPILE = "testCompile"

    @Override
    public void apply(Project project) {
        project.configurations.create(AllureReportTask.CONFIGURATION_NAME)
        AllureExtension ext = configureAllureExtension(project)
        project.afterEvaluate {
            applyGeb(project, ext)
            configureResultsDirSystemProperty(project, ext)
        }
        configureBundle(project, ext)
        applyAllureSpockAdaptor(project, ext)
        project.tasks.create(AllureReportTask.NAME, AllureReportTask)
    }

    private static AllureExtension configureAllureExtension(Project project) {
        project.extensions.create(AllureExtension.NAME, AllureExtension.class, project)
    }

    private static void applyAllureSpockAdaptor(Project project, AllureExtension ext) {
        PluginCollection<JavaPlugin> javaPlugins = project.plugins.withType(JavaPlugin.class)
        if (!javaPlugins.empty) {
            project.dependencies.add(
                    CONFIGURATION_TEST_COMPILE,
                    "ru.yandex.qatools.allure:allure-spock-1.0-adaptor:1.0")
        }
    }

    private static void applyGeb(Project project, AllureExtension ext) {
        if (ext.geb) {
            addD10xaRepository(project)
            project.dependencies.add(CONFIGURATION_TEST_COMPILE, "ru.d10xa:allure-spock-geb:0.2.1")
        }
    }

    private static void addD10xaRepository(Project project) {
        project.repositories.maven { MavenArtifactRepository repo ->
            repo.setUrl("https://dl.bintray.com/d10xa/maven")
        }
    }

    private static void configureResultsDirSystemProperty(Project project, AllureExtension ext) {
        project.tasks.withType(Test.class).each {
            it.systemProperty(ALLURE_RESULTS_DIRECTORY_SYSTEM_PROPERTY, ext.allureResultsDir)
        }
    }

    private static void configureBundle(Project project, AllureExtension ext) {
        project.dependencies.add(
                AllureReportTask.CONFIGURATION_NAME,
                "ru.yandex.qatools.allure:allure-bundle:$ext.allureBundleVersion")
    }

}

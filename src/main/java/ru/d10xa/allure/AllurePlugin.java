package ru.d10xa.allure;

import org.gradle.api.Action;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.plugins.PluginCollection;
import org.gradle.api.tasks.testing.Test;

public class AllurePlugin implements Plugin<Project> {

    private static final String
            ALLURE_RESULTS_DIRECTORY_SYSTEM_PROPERTY = "allure.results.directory";

    private static final String CONFIGURATION_TEST_COMPILE = "testCompile";

    private Project project;

    private Configuration cfg;

    private AllureExtension ext;

    private Task allureReportTask;

    @Override
    public void apply(Project project) {
        this.project = project;
        PluginCollection<JavaPlugin> javaPlugins = this.project.getPlugins()
                .withType(JavaPlugin.class);
        this.cfg = project.getConfigurations().create(AllureReportTask.CONFIGURATION_NAME);
        this.ext = project.getExtensions()
                .create(AllureExtension.NAME, AllureExtension.class, project);
        this.project.afterEvaluate(new Action<Project>() {
            @Override
            public void execute(Project project) {
                if (ext.isGeb()) {
                    MavenRepositories.addRepository(
                            project.getRepositories(), MavenRepositories.D10XA_MAVEN);

                    dependency(
                            CONFIGURATION_TEST_COMPILE,
                            "ru.d10xa",
                            "allure-spock-geb",
                            "0.2.1");
                }
                for (Test test : project.getTasks().withType(Test.class)) {
                    test.systemProperty(
                            ALLURE_RESULTS_DIRECTORY_SYSTEM_PROPERTY,
                            ext.getAllureResultsDir());
                }
            }
        });

        dependency(
                AllureReportTask.CONFIGURATION_NAME,
                "ru.yandex.qatools.allure", "allure-bundle",
                this.ext.getAllureBundleVersion());

        if (!javaPlugins.isEmpty()) {
            addDependencies();
        }
        createAllureReportTask(project);
    }

    private void createAllureReportTask(Project project) {
        this.allureReportTask = project
                .getTasks()
                .create(AllureReportTask.NAME, AllureReportTask.class);
    }

    private void addDependencies() {
        dependency(
                CONFIGURATION_TEST_COMPILE,
                "ru.yandex.qatools.allure", "allure-spock-1.0-adaptor", "1.0");
    }

    private void dependency(String configuration, String group, String name, String version) {
        this.project.getDependencies()
                .add(configuration, String.format("%s:%s:%s", group, name, version));
    }

}

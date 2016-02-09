package ru.d10xa.allure;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.tasks.testing.Test;

public class AllurePlugin implements Plugin<Project> {

    private static final String ALLURE_RESULTS_DIRECTORY_SYSTEM_PROPERTY = "allure.results.directory";

    private Project project;

    private Configuration cfg;

    private AllureExtension ext;

    private Task allureReportTask;

    @Override
    public void apply(Project project) {
        this.project = project;
        this.cfg = project.getConfigurations().create(AllureReportTask.CONFIGURATION_NAME);
        this.ext = project.getExtensions().create(AllureExtension.NAME, AllureExtension.class, project);
        for (Test test : project.getTasks().withType(Test.class)) {
            test.systemProperty(ALLURE_RESULTS_DIRECTORY_SYSTEM_PROPERTY, this.ext.getAllureResultsDir());
        }

        addDependencies();

        this.allureReportTask = project.getTasks().create(AllureReportTask.NAME, AllureReportTask.class);
    }

    private void addDependencies() {
        dependency("allureReport", "ru.yandex.qatools.allure", "allure-bundle", this.ext.getAllureBundleVersion());
        dependency("testCompile", "ru.yandex.qatools.allure", "allure-spock-1.0-adaptor", "1.0");
    }

    private void dependency(String configuration, String group, String name, String version) {
        this.project.getDependencies().add(configuration, String.format("%s:%s:%s", group, name, version));
    }

}

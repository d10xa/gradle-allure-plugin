package ru.d10xa.allure;

import org.gradle.api.tasks.JavaExec;
import org.gradle.api.tasks.TaskAction;

public class AllureReportTask extends JavaExec {

    public static final String NAME = "allureReport";

    public static final String CONFIGURATION_NAME = "allureReport";

    @Override
    @TaskAction
    public void exec() {
        AllureExtension allure = getProject().getExtensions().findByType(AllureExtension.class);

        systemProperty("allure.results.directory", allure.getAllureResultsDir());
        args(allure.getAllureResultsDir(), allure.getAllureReportDir());
        setMain("ru.yandex.qatools.allure.AllureMain");
        classpath(getProject().getConfigurations().getByName("allureReport"));
        super.exec();
    }

}

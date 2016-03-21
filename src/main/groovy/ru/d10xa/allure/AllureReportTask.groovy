package ru.d10xa.allure

import groovy.transform.CompileStatic
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.JavaExec
import org.gradle.api.tasks.TaskAction

@CompileStatic
public class AllureReportTask extends JavaExec {

    public static final String NAME = "allureReport"

    public static final String ALLURE_MAIN_CLASS = "ru.yandex.qatools.allure.AllureMain"

    public static final String CONFIGURATION_NAME = "allureReport"

    private Set<Object> resultDirs

    private Object reportDir

    public AllureReportTask() {
        this.resultDirs = new LinkedHashSet<Object>()
    }

    @Override
    @TaskAction
    public void exec() {
        args(collectArguments())
        setMain(ALLURE_MAIN_CLASS)
        classpath(getProject().getConfigurations().getByName("allureReport"))
        super.exec()
    }

    @Input
    private List<String> collectArguments() {
        List<String> args = new ArrayList<String>(this.resultDirs.size() + 1)
        for (Object resultDir : this.resultDirs) {
            args.add(resultDir.toString());
        }
        if (args.empty) {
            args.add(getAllureExtension().getAllureResultsDir())
        }
        if (this.reportDir != null) {
            args.add(this.reportDir.toString())
        } else {
            args.add(getAllureExtension().getAllureReportDir())
        }
        args
    }

    private AllureExtension getAllureExtension() {
        return project.extensions.findByType(AllureExtension.class)
    }

    public void from(Object... results) {
        this.resultDirs = new LinkedHashSet<Object>()
        for (Object result : results) {
            this.resultDirs.add(result)
        }
    }

    public void to(Object reportDir) {
        this.reportDir = reportDir
    }

}
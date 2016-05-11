package ru.d10xa.allure

import groovy.transform.CompileStatic
import org.gradle.api.Project
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.JavaExec
import org.gradle.api.tasks.OutputDirectory
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

    @OutputDirectory
    private File getReportDir() {
        this.@reportDir ?
                project.file(this.@reportDir.toString()) :
                project.file(allureExtension.allureReportDir)
    }

    @Input
    private List<String> collectArguments() {
        def arguments = this.resultDirs.collect resultDirCollector

        if (logger.debugEnabled) {
            def dirs = arguments.collect { new File(it) }
            dirs.findAll { !it.directory }.each {
                logger.debug "resultsDir: $it is not directory"
            }
            dirs.findAll { it.directory && it.list().length == 0 }.each {
                logger.debug "resultsDir: $it is empty directory"
            }
        }

        if (arguments.empty) {
            arguments.add(allureExtension.allureResultsDir)
        }
        arguments.add(getReportDir()?.toString())
        arguments
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

    private static Closure<String> getResultDirCollector() {
        def closure = {
            if (it instanceof Project) {
                return it.extensions.getByType(AllureExtension).allureResultsDir
            }
            return it.toString()
        }
        return closure
    }

    private AllureExtension getAllureExtension() {
        return project.extensions.findByType(AllureExtension.class)
    }

}
